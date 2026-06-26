# RitmoTicket — Flujo de negocio y arquitectura (estado actual)

**Fecha:** 24 de junio de 2026  
**Alcance:** Funcionamiento del proyecto con **Kafka, Feign, JWT, Swagger (ms-catalogo) y HATEOAS en eventos**. Pendiente: API Gateway y despliegue por contenedor de cada MS.

---

## 1. Visión general

RitmoTicket es una plataforma de venta de entradas para conciertos. Está dividida en **microservicios independientes**, cada uno con **su propia base de datos PostgreSQL**. No comparten tablas; se coordinan por:

| Mecanismo | Cuándo se usa | Ejemplo |
|-----------|---------------|---------|
| **REST (HTTP)** | El cliente (Postman, app web) llama a un servicio | `POST /api/v1/compras` |
| **Feign (HTTP síncrono)** | Un MS necesita **respuesta inmediata** de otro | Compras pide precio a Boletos |
| **Kafka (eventos asíncronos)** | Un MS avisa a otros **sin esperar respuesta** | Catálogo publica evento → Boletos actualiza proyección |
| **Eureka** | Los MS se encuentran entre sí por nombre | `ms-boletos`, `ms-compras`, etc. |

**Infraestructura compartida (Docker):**

- PostgreSQL `:5433` — una BD por microservicio
- Kafka `:9092` — bus de eventos
- Kafka UI `:8080` — visualización de topics
- Eureka `:8761` — registro de servicios

---

## 2. Mapa de microservicios

| Microservicio | Puerto | Base de datos | Rol en el negocio |
|---------------|--------|---------------|-------------------|
| **eureka** | 8761 | — | Directorio: todos los MS se registran aquí |
| **ms-catalogo** | 9003 | `catalogo` | Eventos/conciertos del sistema (fuente de verdad) |
| **ms-artistas** | 9001 | `artistas` | Artistas y álbumes |
| **ms-recintos** | 9008 | `recintos` | Recintos, escenarios y sectores |
| **ms-boletos** | 9002 | `boletos` | Inventario de tickets (códigos, zonas, estados) |
| **ms-precios** | 9007 | `precios` | Tabla de precios por tipo de boleto |
| **ms-usuarios** | 9010 | `usuarios` | Clientes, administradores, organizadores; login JWT |
| **ms-compras** | 9004 | `compras` | Carritos, órdenes de compra, detalle |
| **ms-pagos** | 9006 | `pagos` | Cobros, transacciones, reembolsos |
| **ms-notificaciones** | 9005 | `notificaciones` | Correos, SMS, notificaciones |
| **ms-reportes** | 9009 | `reportes` | Reportes, estadísticas, auditoría |
| **common** | — | — | Librería compartida (excepciones, eventos Kafka) |

**Pendiente (no implementado aún):** API Gateway, Swagger en todos los MS, despliegue en contenedores por MS.

**Credenciales de prueba (seed):** `ana@administrador.cl` / `Ritmo@2026` (rol Administrador). Cliente de ejemplo: Carlos `carlos@cliente.cl` (`id_usuario = 7`).

---

## 2.1 Datos semilla alineados entre microservicios

Los scripts en `init-multi-db/` usan **IDs lógicos compartidos** (sin FK entre bases de datos). Referencias clave:

### Eventos (`catalogo.eventos` ↔ `boletos.proy_eventos`)

| id_evento | nombre_evento | id_artista | id_recinto | Recinto (ms-recintos) |
|-----------|---------------|------------|------------|------------------------|
| 1 | Gira Ven Aquí - Los Bunkers | 1 (Los Bunkers) | 1 | Estadio Nacional |
| 2 | Radical Optimism Tour - Dua Lipa | 2 (Dua Lipa) | 2 | Movistar Arena |
| 3 | Lollapalooza Chile 2026 | 5 (Metallica, headliner) | 4 | Espacio Riesco |
| 4 | Autopoiética Tour - Mon Laferte | 4 (Mon Laferte) | 3 | Teatro Biobío |

`id_artista` → `artistas.id_artista` | `id_recinto` → `recintos.id_recinto`

### Boletos de ejemplo (`boletos.boletos`)

| id_boleto | id_evento | id_zona | Código | Zona / precio |
|-----------|-----------|---------|--------|----------------|
| 1 | 1 | 2 | TKT-LB-001 | Cancha General — $45.000 |
| 2 | 1 | 2 | TKT-LB-002 | Cancha General — $45.000 |
| 3 | 2 | 2 | TKT-DL-501 | Cancha General — $45.000 (Reservado) |
| 4 | 2 | 1 | TKT-DL-502 | VIP Experience — $150.000 (Vendido) |
| 5 | 2 | 1 | TKT-DL-999 | VIP — Anulado |

### Compras y pagos de ejemplo (seed)

| id_compra | Usuario | Boletos | Total | id_pago | Estado pago |
|-----------|---------|---------|-------|---------|-------------|
| 1 | Carlos (7) | 1 + 2 | $90.000 | 1 | APROBADO |
| 2 | Camila (8) | 4 | $150.000 | 2 | APROBADO |
| 3 | Cristian (9) | 3 | $45.000 | 3 | PENDIENTE |

Las tablas `proy_*` en compras, pagos, precios, usuarios, reportes y notificaciones replican estos mismos IDs y estados (`COMPLETADA`, `APROBADO`, etc., como publica Kafka).

---

## 3. Dos momentos del ciclo de vida

El sistema tiene **dos fases** distintas:

### Fase A — Preparación del concierto (back-office)

La empresa configura el evento **antes** de que un cliente compre.

### Fase B — Compra de tickets (cliente final)

Un usuario elige entradas, paga y la compra queda confirmada.

---

## 4. Fase A — Preparación (ejemplo: concierto Los Bunkers)

Orden lógico de operaciones y servicios involucrados:

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│ ms-artistas │     │ ms-recintos │     │ ms-catalogo │
│  (artista)  │     │  (recinto)  │     │  (evento)   │
└──────┬──────┘     └──────┬──────┘     └──────┬──────┘
       │                   │                   │
       │    referencias    │    referencias    │
       └───────────────────┴───────────────────┘
                           │
                    POST evento
                           │
                           ▼ Kafka: catalogo.evento.created
                    ┌─────────────┐
                    │ ms-boletos  │  → proy_eventos (copia local del evento)
                    └──────┬──────┘
                           │
                    POST boletos (inventario)
                           │
                           ▼ Kafka: boletos.boleto.created
         ┌─────────────────┼─────────────────┐
         ▼                 ▼                 ▼
   ms-compras         ms-precios         ms-usuarios
   proy_boletos       proy_boletos       proy_boletos
```

### Paso A.1 — Datos maestros (opcional si ya existen en seed)

| Orden | Acción | Servicio | Comunicación |
|-------|--------|----------|--------------|
| 1 | Registrar artista | ms-artistas | REST directo |
| 2 | Registrar recinto y sectores | ms-recintos | REST directo |
| 3 | Definir precios base por tipo | ms-precios | REST directo |

### Paso A.2 — Crear el evento en catálogo

| Acción | Servicio | Qué pasa |
|--------|----------|----------|
| `POST /api/v1/eventos` | **ms-catalogo** | Guarda el concierto (nombre, fecha, `id_artista`, `id_recinto`) |
| Publicación Kafka | **ms-catalogo** → **ms-boletos** | Topic `catalogo.evento.created` (`EventoService` + `EventoEventProducer`) |
| Consumer | **ms-boletos** | Crea fila en `proy_eventos` (proyección local) |
| `GET /api/v1/eventos` | **ms-catalogo** | Enriquece respuesta con artista y recinto vía Feign (`ArtistaClient`, `RecintoClient`) |

**Por qué Kafka y no Feign (para proyección):** Boletos no necesita preguntarle a catálogo en cada request; mantiene una **copia mínima** del evento para emitir tickets sin depender de que catálogo esté online.

**Feign en lectura (catálogo):** Al listar o consultar un evento, catálogo llama a `ms-artistas` y `ms-recintos` para devolver objetos `artista` y `recinto` embebidos en `EventoResponseDTO`.

### Paso A.3 — Generar inventario de boletos

| Acción | Servicio | Qué pasa |
|--------|----------|----------|
| `POST /api/v1/boletos` | **ms-boletos** | Crea tickets (código, zona, estado `Disponible`) |
| Publicación Kafka | **ms-boletos** → varios | Topic `boletos.boleto.created` |
| Consumers | **ms-compras**, **ms-precios**, **ms-usuarios** | Actualizan `proy_boletos` en cada BD |

**Servicios que NO intervienen aún:** ms-pagos, ms-notificaciones, ms-reportes (hasta que haya compra/pago).

### Validaciones cruzadas (Feign, uso administrativo)

| Servicio origen | Feign hacia | Cuándo |
|-----------------|-------------|--------|
| ms-artistas | ms-catalogo | `GET /api/v1/eventos/existe/artista/{idArtista}` — antes de eliminar artista |
| ms-recintos | ms-catalogo | `GET /api/v1/eventos/existe/recinto/{idRecinto}` — antes de eliminar recinto |
| ms-catalogo | ms-artistas | `GET /api/v1/artistas/id/{id}` — enriquecer respuesta de eventos |
| ms-catalogo | ms-recintos | `GET /api/v1/recintos/id/{id}` — enriquecer respuesta de eventos |

Los dos primeros Feign son reglas de integridad administrativa. Los dos últimos mejoran la respuesta REST al cliente; **no participan** en el flujo de compra.

---

## 5. Fase B — Compra real de tickets (escenario completo)

**Personaje:** Carlos (usuario id `7`)  
**Objetivo:** 2 entradas Cancha General para Los Bunkers (boletos id `1` y `2`, $45.000 c/u)  
**Total esperado:** $90.000  
**Método de pago:** WebPay

> En el seed SQL, Carlos ya tiene la compra `id_compra = 1` con esos boletos. El ejemplo siguiente muestra cómo se crearía una compra nueva vía API.

### Diagrama de secuencia (flujo principal)

```mermaid
sequenceDiagram
    actor Cliente as Cliente (Postman/App)
    participant Usuarios as ms-usuarios
    participant Compras as ms-compras
    participant Boletos as ms-boletos
    participant Pagos as ms-pagos
    participant Kafka as Kafka
    participant Notif as ms-notificaciones
    participant Reportes as ms-reportes

    Cliente->>Usuarios: POST /auth/login (JWT)
    Usuarios-->>Cliente: Bearer token
    Cliente->>Compras: POST /compras (Authorization)
    Compras->>Boletos: Feign GET /boletos/precio/id/1 y /id/2
    Boletos-->>Compras: 45000 c/u
    Compras->>Compras: total = 90000, estado PENDIENTE
    Compras->>Kafka: compras.compra.created
    Kafka-->>Pagos: proy_compras
    Kafka-->>Notif: proy_compras
    Kafka-->>Reportes: proy_compras
    Compras->>Pagos: Feign POST /pagos
    Pagos->>Pagos: estado PENDIENTE, idCompra
    Pagos->>Kafka: pagos.pago.created
    Kafka-->>Compras: proy_pagos
    Kafka-->>Reportes: proy_pagos
    Compras-->>Cliente: 201 Compra PENDIENTE

    Cliente->>Pagos: PUT /pagos/aprobar/id/{idPago}
    Pagos->>Pagos: estado APROBADO
    Pagos->>Kafka: pagos.pago.updated
    Pagos->>Compras: Feign PUT /compras/confirmar/id/{id}
    Compras->>Compras: estado COMPLETADA
    Compras->>Kafka: compras.compra.updated
    Kafka-->>Notif: proy_compras actualizada
    Kafka-->>Reportes: proy_compras actualizada
    Pagos-->>Cliente: 200 Pago APROBADO
```

---

### Paso B.1 — Usuario (hoy: manual / seed)

| Servicio | Rol |
|----------|-----|
| **ms-usuarios** | Carlos ya existe en BD (`id_usuario = 7`) |

**Estado actual:** La compra acepta `idUsuario` en el JSON, pero **no valida** vía Feign que el usuario exista (no hay `UsuarioClient` activo). Eso es una mejora futura.

---

### Paso B.2 — Carrito (opcional)

| Acción | Servicio | Comunicación |
|--------|----------|--------------|
| `POST /api/v1/carritos/boletos/id/{idCarrito}` | **ms-compras** | REST del cliente |
| Consulta precio | **ms-compras** → **ms-boletos** | **Feign** `BoletoClient` |
| Actualiza total del carrito | **ms-compras** | Local |

El carrito es **opcional** en el flujo actual: también se puede comprar directo con `POST /compras` sin pasar por carrito.

---

### Paso B.3 — Crear la compra (núcleo del flujo)

**Request del cliente:**

```
POST http://localhost:9004/api/v1/compras
```

```json
{
  "idUsuario": 7,
  "metodoPago": "WEBPAY",
  "detalles": [
    { "idBoleto": 1, "cantidad": 1 },
    { "idBoleto": 2, "cantidad": 1 }
  ]
}
```

**Qué hace ms-compras por dentro:**

| # | Acción | Comunicación | Resultado |
|---|--------|--------------|-----------|
| 1 | Pide precio de cada boleto | **Feign** → ms-boletos | `45000` + `45000` |
| 2 | Calcula subtotales y total | Local | `90000` |
| 3 | Guarda compra | BD `compras` | estado `PENDIENTE` |
| 4 | Publica evento | **Kafka** `compras.compra.created` | Otros MS actualizan proyección |
| 5 | Pide crear pago | **Feign** → ms-pagos | Pago `PENDIENTE` con `idCompra` |

**Microservicios tocados en este paso:**

| Servicio | Cómo participa |
|----------|----------------|
| **ms-compras** | Orquestador principal |
| **ms-boletos** | Feign: devuelve precio |
| **ms-pagos** | Feign: recibe orden de cobro |
| **ms-notificaciones** | Kafka: actualiza `proy_compras` |
| **ms-reportes** | Kafka: actualiza `proy_compras` |
| **ms-pagos** | Kafka: consumer de compra (proyección) |

**No participan aún:** ms-usuarios, ms-catalogo, ms-artistas, ms-recintos, ms-precios (salvo que ya tengan `proy_boletos` del paso A).

---

### Paso B.4 — Aprobar el pago

**Request del cliente (o pasarela WebPay simulada):**

```
PUT http://localhost:9006/api/v1/pagos/aprobar/id/{idPago}
```

**Qué hace ms-pagos:**

| # | Acción | Comunicación | Resultado |
|---|--------|--------------|-----------|
| 1 | Marca pago | BD `pagos` | estado `APROBADO` |
| 2 | Publica evento | **Kafka** `pagos.pago.updated` | proy_pagos en compras/reportes |
| 3 | Confirma compra | **Feign** → ms-compras `PUT confirmar` | Compra `COMPLETADA` |
| 4 | (en compras) Publica evento | **Kafka** `compras.compra.updated` | notificaciones/reportes |

**Microservicios tocados:**

| Servicio | Cómo participa |
|----------|----------------|
| **ms-pagos** | Aprueba cobro y llama a compras |
| **ms-compras** | Feign: confirma compra + Kafka updated |
| **ms-notificaciones** | Kafka: `proy_compras` con estado actualizado |
| **ms-reportes** | Kafka: `proy_compras` y `proy_pagos` |

---

### Paso B.5 — Notificación al cliente (estado actual vs ideal)

| Aspecto | Estado actual | Ideal futuro |
|---------|---------------|--------------|
| Datos de compra en notificaciones | ✅ vía Kafka → `proy_compras` | — |
| Envío automático de email/SMS | ❌ No hay consumer que dispare correo | Servicio escucha Kafka y envía |
| ms-notificaciones | CRUD manual de notificaciones | Automatizar tras `compra.updated` |

Hoy **ms-notificaciones** recibe la proyección pero **no envía** correo automáticamente al confirmar la compra.

---

### Paso B.6 — Reportes y estadísticas

| Servicio | Rol actual |
|----------|------------|
| **ms-reportes** | Recibe `proy_compras` y `proy_pagos` por Kafka |
| Generación automática de reporte | ❌ No disparada por eventos; CRUD manual |

Los datos están **listos localmente** para reportes; falta la lógica de negocio que genere PDF/estadísticas al cerrar una venta.

---

### Paso B.7 — Cambio de estado del boleto (parcial)

Cuando un boleto pasa de `Disponible` → `Reservado` / `Vendido`:

| Acción | Servicio | Comunicación |
|--------|----------|--------------|
| `PUT /api/v1/boletos/id/{id}` | **ms-boletos** | REST |
| Publicación | **Kafka** `boletos.boleto.updated` | |
| Consumers | ms-compras, ms-precios, ms-usuarios | Actualizan `proy_boletos.estado` |

**Estado actual:** Esto **no se ejecuta automáticamente** al confirmar la compra. Habría que llamar manualmente o implementar un consumer/listener que marque boletos vendidos (mejora futura).

---

## 6. Matriz de comunicaciones (resumen)

### Feign (HTTP síncrono — “necesito respuesta ahora”)

| Origen | Destino | Endpoint | Cuándo |
|--------|---------|----------|--------|
| ms-compras | ms-boletos | `GET /boletos/precio/id/{id}` | Calcular total de compra o carrito |
| ms-compras | ms-pagos | `POST /pagos` | Tras crear compra |
| ms-pagos | ms-compras | `PUT /compras/confirmar/id/{id}` | Tras aprobar pago |
| ms-artistas | ms-catalogo | `GET /api/v1/eventos/existe/artista/{idArtista}` | Validar antes de eliminar artista |
| ms-recintos | ms-catalogo | `GET /api/v1/eventos/existe/recinto/{idRecinto}` | Validar antes de eliminar recinto |
| ms-catalogo | ms-artistas | `GET /api/v1/artistas/id/{id}` | Enriquecer `EventoResponseDTO` |
| ms-catalogo | ms-recintos | `GET /api/v1/recintos/id/{id}` | Enriquecer `EventoResponseDTO` |

### Kafka (asíncrono — “aviso a quien le interese”)

| Topic | Productor | Consumidores |
|-------|-----------|--------------|
| `catalogo.evento.created` | ms-catalogo | ms-boletos |
| `catalogo.evento.updated` | ms-catalogo | ms-boletos |
| `catalogo.evento.deleted` | ms-catalogo | ms-boletos |
| `boletos.boleto.created` | ms-boletos | ms-compras, ms-precios, ms-usuarios |
| `boletos.boleto.updated` | ms-boletos | ms-compras, ms-precios, ms-usuarios |
| `compras.compra.created` | ms-compras | ms-pagos, ms-notificaciones, ms-reportes |
| `compras.compra.updated` | ms-compras | ms-pagos, ms-notificaciones, ms-reportes |
| `pagos.pago.created` | ms-pagos | ms-compras, ms-reportes |
| `pagos.pago.updated` | ms-pagos | ms-compras, ms-reportes |

### Sin comunicación directa en la compra

Estos servicios existen y tienen API propia, pero **no se invocan** en el flujo automático de compra actual:

| Servicio | Por qué no interviene (aún) |
|----------|----------------------------|
| ms-usuarios | No hay Feign de validación en compra |
| ms-catalogo | Solo vía Kafka en fase A (eventos) |
| ms-artistas / ms-recintos | Solo administración |
| ms-precios | Solo proyección Kafka de boletos |
| ms-notificaciones | Recibe datos pero no envía automáticamente |

---

## 7. Tablas de proyección (`proy_*`)

Cada microservicio guarda **solo una copia mínima** de datos de otros dominios:

| Servicio | Tabla local | Datos que replica | Fuente del evento |
|----------|-------------|-------------------|-------------------|
| ms-boletos | `proy_eventos` | id, nombre, fecha evento | catalogo.evento.* |
| ms-compras | `proy_boletos` | id, evento, zona, código, estado boleto | boletos.boleto.* |
| ms-compras | `proy_pagos` | id, monto, estado pago | pagos.pago.* |
| ms-pagos | `proy_compras` | id, total, estado compra | compras.compra.* |
| ms-notificaciones | `proy_compras` | id, total, estado | compras.compra.* |
| ms-reportes | `proy_compras`, `proy_pagos` | totales y estados | compras.* / pagos.* |
| ms-precios | `proy_boletos` | inventario local | boletos.boleto.* |
| ms-usuarios | `proy_boletos` | inventario local | boletos.boleto.* |

**Idea clave:** Evitar joins entre bases de datos. Cada MS consulta su propia proyección.

---

## 8. Orden cronológico completo (compra de concierto)

Lista única desde cero hasta ticket “vendido” en el **estado actual del código**:

| # | Fase | Acción | Servicio(s) | Tipo comunicación |
|---|------|--------|-------------|-------------------|
| 1 | Prep | Crear artista | ms-artistas | REST |
| 2 | Prep | Crear recinto | ms-recintos | REST |
| 3 | Prep | Crear evento | ms-catalogo | REST + Kafka → boletos |
| 4 | Prep | Emitir boletos | ms-boletos | REST + Kafka → compras, precios, usuarios |
| 5 | Prep | (Opcional) Precios | ms-precios | REST / proyección Kafka |
| 6 | Venta | Usuario registrado | ms-usuarios | REST (manual hoy) |
| 7 | Venta | (Opcional) Agregar al carrito | ms-compras + ms-boletos | REST + Feign |
| 8 | Venta | **Crear compra** | ms-compras + ms-boletos + ms-pagos | REST + Feign + Kafka |
| 9 | Venta | **Aprobar pago** | ms-pagos + ms-compras | REST + Feign + Kafka |
| 10 | Venta | (Manual) Marcar boleto vendido | ms-boletos | REST + Kafka |
| 11 | Post | (Manual) Crear notificación | ms-notificaciones | REST |
| 12 | Post | (Manual) Generar reporte | ms-reportes | REST |

Los pasos 10–12 son **mejoras pendientes** para un flujo 100 % automático.

---

## 9. Cómo entraría el cliente en producción (futuro)

Hoy Postman llama **directo** a cada puerto (`9004`, `9006`, etc.). En producción el diseño previsto es:

```
Cliente → API Gateway (JWT) → Eureka → Microservicio
```

**No implementado aún:** Swagger unificado en todos los MS. **API Gateway** en puerto **9000** — ver [05-Api-Gateway.md](./05-Api-Gateway.md) sección 10.

---

## 10. Checklist: qué está listo vs pendiente

| Capacidad | Estado |
|-----------|--------|
| Crear evento y proyectar en boletos | ✅ |
| Inventario de boletos y proyección | ✅ |
| Compra con precio vía Feign | ✅ |
| Pago automático al comprar vía Feign | ✅ |
| Confirmación de compra al aprobar pago | ✅ |
| Proyecciones Kafka en pagos/notificaciones/reportes | ✅ |
| Marcar boleto vendido al confirmar compra | ❌ |
| Email automático al cliente | ❌ |
| Reporte automático de venta | ❌ |
| Validar usuario en compra (Feign) | ❌ |
| Login / JWT (todos los MS) | ✅ |
| API Gateway (`:9000`, 13 rutas) | ✅ |
| Swagger en ms-catalogo (`EventoController`) | ✅ |
| HATEOAS en eventos, boletos y usuarios | ✅ |
| Datos semilla alineados entre MS | ✅ |

---

## 11. Ejemplo numérico rápido (Carlos compra 2 tickets)

```
1. (Seed) evento id=1 "Gira Ven Aquí - Los Bunkers", id_artista=1, id_recinto=1
2. (Seed) boletos.proy_eventos ← sincronizado con catálogo (ids 1–4)
3. (Seed) boletos id=1 y 2, evento 1, zona Cancha, $45.000 c/u
4. (Seed) proy_boletos en compras/precios/usuarios
5. POST compras (o seed compra id=1) → total $90.000, PENDIENTE
   ├─ Feign boletos  → precio 45000 por boleto
   ├─ Feign pagos    → pago id=1, PENDIENTE → luego APROBADO
   └─ Kafka          → notificaciones/reportes/pagos (proy_compras)
6. PUT pagos/aprobar/id/1 → pago APROBADO
   ├─ Feign compras  → compra COMPLETADA
   └─ Kafka          → compras.compra.updated, pagos.pago.updated
7. GET compras       → id=1, estado COMPLETADA
```

---

## 12. Documentos relacionados

- [KAFKA-IMPLEMENTACION.md](./KAFKA-IMPLEMENTACION.md) — Detalle técnico de eventos Kafka
- [MEJORAS-OPCIONALES.md](./MEJORAS-OPCIONALES.md) — Roadmap de mejoras futuras (HATEOAS, flujos automáticos, Gateway, etc.)
- [05-Api-Gateway.md](./05-Api-Gateway.md) — Patrón gateway + §10 RitmoTicket + colección Postman

---

*Documento generado para describir el funcionamiento de RitmoTicket con Kafka y Feign implementados.*
