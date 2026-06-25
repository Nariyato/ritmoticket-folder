# Implementación Kafka en RitmoTicket

**Fecha:** 24 de junio de 2026  
**Alcance:** Completar la arquitectura event-driven parcialmente implementada en el proyecto.

---

## 1. Contexto: qué problema resuelve Kafka aquí

RitmoTicket es un sistema de **microservicios** donde cada servicio tiene su **propia base de datos**. Eso significa que `ms-compras` no puede hacer un `JOIN` directo contra la tabla `boletos` de `ms-boletos`, ni `ms-pagos` contra `compras` de `ms-compras`.

La solución adoptada en este proyecto es el patrón **Event-Driven Architecture (EDA)** con Kafka:

1. Cuando algo importante ocurre en un servicio (origen), se **publica un evento**.
2. Los servicios que necesitan una copia local de esos datos (destino) **escuchan** el evento y actualizan una **tabla de proyección** (`proy_*`).

Así cada microservicio mantiene solo los datos mínimos que necesita para operar, sin acoplarse a la base de datos de otro.

---

## 2. Estado anterior vs. estado actual

| Componente | Antes | Ahora |
|------------|-------|-------|
| Eventos de catálogo → boletos | ✅ Funcionaba | ✅ Sin cambios |
| Eventos de boletos → otros servicios | ❌ Producer existía pero **no se llamaba** | ✅ Conectado en `BoletoService` |
| Eventos de compras → pagos/notificaciones/reportes | ❌ No existía | ✅ Implementado |
| Eventos de pagos → compras/reportes | ❌ No existía | ✅ Implementado |
| Tablas `proy_*` en servicios destino | ⚠️ Solo en SQL, sin sincronización automática | ✅ Sincronizadas vía Kafka |

---

## 3. Mapa completo de eventos

```
ms-catalogo  ──catalogo.evento.{created|updated|deleted}──►  ms-boletos
ms-boletos   ──boletos.boleto.{created|updated}────────────►  ms-compras, ms-precios, ms-usuarios
ms-compras   ──compras.compra.{created|updated}────────────►  ms-pagos, ms-notificaciones, ms-reportes
ms-pagos     ──pagos.pago.{created|updated}────────────────►  ms-compras, ms-reportes
```

### Topics Kafka creados o utilizados

|            Topic          |  Productor  |             Consumidores                 |
| ------------------------- | ----------- | ---------------------------------------- |
| `catalogo.evento.created` | ms-catalogo | ms-boletos                               |
| `catalogo.evento.updated` | ms-catalogo | ms-boletos                               |
| `catalogo.evento.deleted` | ms-catalogo | ms-boletos                               |
| `boletos.boleto.created`  | ms-boletos  | ms-compras, ms-precios, ms-usuarios      |
| `boletos.boleto.updated`  | ms-boletos  | ms-compras, ms-precios, ms-usuarios      |
| `compras.compra.created`  | ms-compras  | ms-pagos, ms-notificaciones, ms-reportes |
| `compras.compra.updated`  | ms-compras  | ms-pagos, ms-notificaciones, ms-reportes |
| `pagos.pago.created`      | ms-pagos    | ms-compras, ms-reportes                  |
| `pagos.pago.updated`      | ms-pagos    | ms-compras, ms-reportes                  |

---

## 4. Cambios realizados (detalle por módulo)

### 4.1 Módulo `common` — Contratos de eventos compartidos

**Archivos nuevos:**

- `CompraEvent.java`, `CompraCreatedEvent.java`, `CompraUpdatedEvent.java`
- `PagoEvent.java`, `PagoCreatedEvent.java`, `PagoUpdatedEvent.java`

**Por qué:** Los eventos viven en `common` porque varios microservicios los producen o consumen. Un DTO compartido garantiza que el JSON que publica `ms-compras` sea exactamente el que deserializa `ms-pagos`.

**Ejemplo de payload (`CompraCreatedEvent`):**

```json
{
  "idCompra": 42,
  "idUsuario": 7,
  "total": 90000.00,
  "estado": "PENDIENTE"
}
```

---

### 4.2 `ms-boletos` — Conectar el producer que ya existía

**Archivo modificado:** `BoletoService.java`

**Qué se hizo:** Se inyectó `BoletoEventProducer` y se llama en:

- `create()` → publica `boletos.boleto.created`
- `update()` → publica `boletos.boleto.updated`

**Por qué:** El producer (`BoletoEventProducer`) y los topics (`KafkaTopicConfig`) ya existían, pero el servicio tenía el comentario `// FALTA APLICAR LOS EVENTOS DEL KAFKA`. Sin esta conexión, crear o actualizar un boleto **no notificaba a nadie**.

**Código relevante (conceptual):**

```java
// Después de guardar un boleto nuevo:
boletoEventProducer.sendCreated(BoletoCreatedEvent.builder()
    .idBoleto(boleto.getIdBoleto())
    .idEvento(boleto.getEvento().getIdEvento())
    .idZona(boleto.getZona().getIdZona())
    .codigo(boleto.getCodigo())
    .estado(boleto.getEstado())
    .build());
```

---

### 4.3 `ms-compras`, `ms-precios`, `ms-usuarios` — Consumir eventos de boletos

**Qué se hizo en cada uno:**

|       Elemento         |                            Descripción                                        |
| ---------------------- | ----------------------------------------------------------------------------- |
| `ProyBoleto` (entidad) | Se agregaron campos `idEvento` e `idZona` para alinear con el SQL y el evento |
| `ProyBoletoRepository` | Repositorio JPA para la tabla `proy_boletos`                                  |
| `ProyBoletoService`    | Lógica de upsert (crear o actualizar proyección)                              |
| `BoletoEventConsumer`  | `@KafkaListener` en topics `boletos.boleto.created` y `.updated`              |

**Por qué:** Cuando un usuario agrega un boleto al carrito, `ms-compras` necesita saber si ese boleto existe y cuál es su estado (`Disponible`, `Reservado`, `Vendido`) **sin llamar por HTTP en cada operación**. La proyección local responde esa pregunta al instante.

---

### 4.4 `ms-compras` — Producer de compras y consumer de pagos

**Archivos nuevos:**

- `CompraEventProducer.java` — publica en `compras.compra.*`
- `KafkaTopicConfig.java` — declara los topics de compras
- `PagoEventConsumer.java` — escucha `pagos.pago.*`
- `ProyPagoRepository`, `ProyPagoService` — sincroniza tabla `proy_pagos`

**Archivo modificado:** `CompraService.java` — emite `CompraCreatedEvent` al guardar una compra.

**Por qué:**

- **Producer:** Cuando se crea una compra, pagos, notificaciones y reportes deben enterarse sin que compras conozca sus APIs internas.
- **Consumer de pagos:** Compras mantiene una proyección de pagos para validar si una compra pendiente ya fue pagada, sin consultar `ms-pagos` en cada request.

---

### 4.5 `ms-pagos` — Producer de pagos y consumer de compras

**Archivos nuevos:**

- `PagoEventProducer.java`, `KafkaTopicConfig.java`
- `CompraEventConsumer.java`
- `ProyCompraRepository`, `ProyCompraService`

**Archivo modificado:** `PagoService.java` — emite `PagoCreatedEvent` al guardar un pago.

**Por qué:** Pagos necesita datos mínimos de la compra (`total`, `estado`) para procesar el cobro. En lugar de depender solo de Feign (síncrono), la proyección `proy_compras` se actualiza automáticamente cuando compras publica un evento.

---

### 4.6 `ms-notificaciones` — Consumer de compras

**Archivos nuevos:** `ProyCompraRepository`, `ProyCompraService`, `CompraEventConsumer`

**Por qué:** Para enviar un correo del tipo *"Su compra #42 por $90.000 fue registrada"*, notificaciones necesita la proyección local de compras. Kafka le entrega esos datos en cuanto ocurren, sin polling ni llamadas repetidas.

---

### 4.8 `ms-catalogo` — Eventos y publicación Kafka

**Archivo principal:** `EventoService.java` (clase `@Service` única, sin interfaz `*Impl`)

**Qué hace:**

- CRUD de eventos en `catalogo.eventos`
- Publica `catalogo.evento.{created|updated|deleted}` vía `EventoEventProducer`
- Enriquece `GET` con Feign a `ms-artistas` y `ms-recintos` (`id_artista`, `id_recinto` deben existir en esos MS)

**API:** `POST/GET/PUT/DELETE /api/v1/eventos` (requiere JWT según rol)

---

### 4.9 `ms-reportes` — Consumers de compras y pagos

**Archivos nuevos:** `CompraEventConsumer.java`, `PagoEventConsumer.java`

**Archivo modificado:** `SyncService.java` — sincroniza `proy_compras` y `proy_pagos` desde los eventos.

**Por qué:** Reportes consolida información de múltiples dominios para estadísticas y auditoría. Kafka alimenta sus tablas de proyección de forma desacoplada.

---

## 5. Escenarios de ejemplo (utilidad práctica)

### Escenario A — Nuevo concierto en el catálogo

**Actores:** Administrador del sistema  
**Flujo:**

1. El admin crea el evento *"Gira Ven Aquí - Los Bunkers"* en `ms-catalogo` (`id_artista=1`, `id_recinto=1`).
2. `EventoService` guarda el evento y llama a `EventoEventProducer.sendCreated()`.
3. Kafka publica en `catalogo.evento.created`.
4. `ms-boletos` recibe el evento en `EventoEventConsumer.onEventoCreated()`.
5. `EventoProyeccionService` crea/actualiza un registro en `proy_eventos` (tabla local de boletos).

**Utilidad:** Boletos puede emitir tickets para ese concierto **sin consultar catálogo por HTTP** cada vez. Si catálogo cae temporalmente, boletos sigue operando con su proyección local.

```
[Admin] → POST /api/v1/eventos (catalogo)
              ↓
         Kafka: catalogo.evento.created
              ↓
         ms-boletos.proy_eventos ← fila idEvento=1 (ej. seed)
```

> **Datos semilla:** Los scripts `init-multi-db/03-catalogo.sql` y `02-boletos.sql` ya incluyen 4 eventos alineados (ids 1–4). Tras recrear las BDD, `proy_eventos` coincide con `catalogo.eventos` sin pasar por Kafka.

---

### Escenario B — Se genera inventario de boletos (cambio principal de esta implementación)

**Actores:** Operador de boletos  
**Flujo:**

1. Se crean boletos para el evento 1 (Los Bunkers) o evento 2 (Dua Lipa) en `ms-boletos` (`POST /api/v1/boletos`).
2. Por cada boleto, `BoletoService.create()` publica `boletos.boleto.created`.
3. Tres servicios reaccionan en paralelo:
   - **ms-compras** → inserta en `proy_boletos` (sabe qué boletos puede vender)
   - **ms-precios** → inserta en `proy_boletos` (referencia local de inventario)
   - **ms-usuarios** → inserta en `proy_boletos` (historial local si aplica)

**Utilidad:** Cuando un cliente agrega el boleto **#3** (`TKT-DL-501`, Dua Lipa, Cancha) al carrito, compras valida contra su proyección local que el boleto existe y cuál es su estado, sin una llamada Feign extra en ese momento.

**Seed de referencia:**

| id_boleto | id_evento | Código | Estado |
|-----------|-----------|--------|--------|
| 1 | 1 | TKT-LB-001 | Vendido |
| 2 | 1 | TKT-LB-002 | Vendido |
| 3 | 2 | TKT-DL-501 | Reservado |
| 4 | 2 | TKT-DL-502 | Vendido |

```
[Operador] → POST /api/v1/boletos (boletos) × 500
                 ↓
            Kafka: boletos.boleto.created (×500)
                 ↓
    ┌────────────┼────────────┐
    ↓            ↓            ↓
 compras     precios      usuarios
 proy_boletos proy_boletos proy_boletos
```

---

### Escenario C — Un cliente realiza una compra

**Actores:** Carlos (usuario id=7)  
**Flujo:**

1. Carlos confirma compra con boletos 1 y 2 (Los Bunkers) → `POST /api/v1/compras` en `ms-compras`.
2. `CompraService.guardar()` calcula total vía Feign (`BoletoClient`: 45.000 + 45.000), guarda la compra y publica `compras.compra.created`:

   ```json
   { "idCompra": 1, "idUsuario": 7, "total": 90000, "estado": "PENDIENTE" }
   ```

3. Consumidores reaccionan:
   - **ms-pagos** → `proy_compras` (sabe que hay una compra pendiente de cobro)
   - **ms-notificaciones** → `proy_compras` (puede preparar email de confirmación)
   - **ms-reportes** → `proy_compras` (métrica de ventas del día)

**Utilidad:** Pagos puede iniciar el cobro sabiendo el monto y estado **aunque la compra se haya creado hace milisegundos**, sin acoplamiento directo entre bases de datos.

---

### Escenario D — Se registra el pago

**Actores:** Carlos paga con WebPay  
**Flujo:**

1. `ms-pagos` registra o aprueba el pago → `POST /api/v1/pagos` o `PUT /api/v1/pagos/aprobar/id/{id}`.
2. `PagoService` publica `pagos.pago.created` o `pagos.pago.updated`:

   ```json
   { "idPago": 1, "monto": 90000, "metodo": "WEBPAY", "estado": "APROBADO" }
   ```

3. Consumidores:
   - **ms-compras** → actualiza `proy_pagos` (`id_pago` alineado con `pagos.id_pago`, no IDs ficticios 501+)
   - **ms-reportes** → actualiza `proy_pagos` (reporte financiero)

**Utilidad:** Reportes puede generar *"Ventas Mensuales - Mayo 2025"* cruzando proyecciones locales de compras y pagos, sin exportar CSVs manualmente ni hacer joins entre bases de datos.

---

### Escenario E — Cambio de estado de un boleto (reserva o venta)

**Actores:** Sistema de compras reserva un boleto  
**Flujo:**

1. `ms-boletos` actualiza boleto **#3** de `Reservado` → `Vendido` (`PUT /api/v1/boletos/id/3`).
2. `BoletoService.update()` publica `boletos.boleto.updated`.
3. `ms-compras`, `ms-precios` y `ms-usuarios` actualizan `estado` (y `id_evento` si aplica) en su `proy_boletos`.

**Utilidad:** Otro cliente que intente comprar el mismo boleto verá en la proyección de compras que ya no está disponible. Este es el evento más crítico según los comentarios en `BoletoUpdatedEvent`:

> *"Viajará constantemente por Kafka cada vez que un boleto pase de Disponible a Reservado o Vendido"*

---

## 6. Patrón de diseño utilizado

### Event-Carried State Transfer (ECST)

Cada evento lleva **los datos mínimos** que el consumidor necesita para actualizar su proyección, no solo un ID:

|         Evento       |          Datos incluidos         | Datos omitidos (a propósito) |
| -------------------- | -------------------------------- | ---------------------------- |
| `BoletoCreatedEvent` | id, evento, zona, código, estado | fecha emisión, reservas      |
| `BoletoUpdatedEvent` | id, evento, código, estado       | zona (no cambia normalmente) |
| `CompraCreatedEvent` | id, usuario, total, estado       | detalle de líneas            |
| `PagoCreatedEvent`   | id, monto, método, estado        | transacciones bancarias      |

Esto reduce llamadas Feign de "completar datos" después de recibir un evento.

### Upsert en proyecciones

Los servicios de proyección siguen el patrón:

```java
// Si existe → actualizar; si no → crear
ProyBoleto proy = repository.findById(id)
    .orElse(ProyBoleto.builder().idBoleto(id).build());
proy.setEstado(nuevoEstado);
repository.save(proy);
```

Esto hace los consumidores **idempotentes**: si Kafka reenvía un mensaje, el resultado es el mismo.

---

## 7. Eventos `updated` (estado actual)

| Evento | Producer conectado | Cuándo se emite |
|--------|-------------------|-----------------|
| `compras.compra.updated` | `CompraService.confirmar()` | Al aprobar pago y marcar compra `COMPLETADA` |
| `pagos.pago.updated` | `PagoService.aprobar()` | Al pasar pago a `APROBADO` |
| `catalogo.evento.updated` | `EventoService.actualizar()` | Al editar un evento |
| `boletos.boleto.updated` | `BoletoService.update()` | Al cambiar estado o datos del boleto |

Los consumers de `*.updated` ya estaban implementados; los producers de compra y pago se conectaron en el flujo de confirmación/aprobación.

---

## 8. Cómo probar manualmente

### Prerrequisitos

```bat
run-dockers.bat          REM Postgres + Kafka
launch.bat               REM Eureka + microservicios
```

Cargar datos iniciales con los scripts `init-multi-db/*.sql` (orden 00 → 10). Si las BDD ya existían con un seed antiguo, recrearlas o aplicar migración manual para alinear `id_recinto`, boletos y `proy_*`.

### Secuencia sugerida

1. **Crear evento** en catálogo → verificar fila en `boletos.proy_eventos`
2. **Crear boleto** en boletos → verificar filas en `compras.proy_boletos`, `precios.proy_boletos`, `usuarios.proy_boletos`
3. **Crear compra** en compras → verificar filas en `pagos.proy_compras`, `notificaciones.proy_compras`, `reportes.proy_compras`
4. **Crear pago** en pagos → verificar filas en `compras.proy_pagos`, `reportes.proy_pagos`

### Verificar Kafka UI

Abrir `http://localhost:8080` (Kafka UI del docker-compose) y confirmar que los topics reciben mensajes al ejecutar cada paso.

### Logs

Con perfil `dev`, buscar en consola:

```
Enviando evento Kafka → topic: boletos.boleto.created, key: 3
Evento recibido → boleto created, idBoleto: 3
```

---

## 9. Resumen ejecutivo

|         Pregunta          |                                         Respuesta                                               |
| ------------------------- | ----------------------------------------------------------------------------------------------- |
| **¿Qué se hizo?**         | Se completó la cadena Kafka: boletos → compras/precios/usuarios → pagos/notificaciones/reportes |
| **¿Por qué?**             | Cada microservicio necesita datos de otros sin acoplarse a sus bases de datos                   |
| **¿Qué faltaba?**         | El producer de boletos no estaba conectado; no existían eventos de compra/pago                  |
| **¿Beneficio inmediato?** | Las tablas `proy_*` se sincronizan solas al crear eventos, boletos, compras y pagos             |
| **¿Próximo paso?**        | Marcar boletos vendidos al confirmar compra; notificaciones automáticas; Gateway |

---

## 10. Archivos tocados (referencia rápida)

```
common/
  event/CompraEvent.java
  event/CompraCreatedEvent.java
  event/CompraUpdatedEvent.java
  event/PagoEvent.java
  event/PagoCreatedEvent.java
  event/PagoUpdatedEvent.java

ms-catalogo/
  service/EventoService.java                    (productor eventos + Feign enriquecimiento)
  event/EventoEventProducer.java

ms-boletos/
  service/BoletoService.java                    (modificado)

ms-compras/
  model/ProyBoleto.java                         (modificado)
  repository/ProyBoletoRepository.java
  repository/ProyPagoRepository.java
  service/ProyBoletoService.java
  service/ProyPagoService.java
  service/CompraService.java                    (modificado)
  event/BoletoEventConsumer.java
  event/PagoEventConsumer.java
  event/CompraEventProducer.java
  config/KafkaTopicConfig.java

ms-precios/
  service/PrecioService.java                    (CRUD local; sin Kafka producer)
  service/ProyBoletoService.java
  event/BoletoEventConsumer.java

ms-usuarios/
  model/ProyBoleto.java                         (modificado)
  repository/ProyBoletoRepository.java
  service/ProyBoletoService.java
  event/BoletoEventConsumer.java

ms-pagos/
  repository/ProyCompraRepository.java
  service/ProyCompraService.java
  service/PagoService.java                      (modificado)
  event/CompraEventConsumer.java
  event/PagoEventProducer.java
  config/KafkaTopicConfig.java

ms-notificaciones/
  repository/ProyCompraRepository.java
  service/ProyCompraService.java
  event/CompraEventConsumer.java

ms-reportes/
  service/SyncService.java                      (modificado)
  event/CompraEventConsumer.java
  event/PagoEventConsumer.java
```

---

*Documento generado como parte de la implementación Kafka en RitmoTicket.*
