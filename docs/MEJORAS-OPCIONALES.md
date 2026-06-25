# RitmoTicket — Mejoras opcionales y roadmap

Documento de referencia para ampliar el proyecto cuando quieras dejarlo más completo.  
**No es obligatorio** para el funcionamiento actual; prioriza según tu entrega o siguientes iteraciones.

---

## HATEOAS

| Microservicio | Estado | Mejora opcional |
|---------------|--------|-----------------|
| ms-catalogo (`EventoController`) | ✅ Implementado | Links condicionales según rol JWT; enriquecer con nombre de zona en boleto |
| ms-boletos (`BoletoController`) | ✅ Implementado | HATEOAS en `ProyEventoController`; link `verificar-evento` si se expone endpoint de existencia |
| ms-usuarios (`UsuarioController`) | ✅ Implementado | HATEOAS en `PerfilResponse`; link `agregar-perfil` en `addLinks` |
| ms-artistas | Pendiente | `ArtistaController` — self, update, delete, all, verificar-catálogo |
| ms-recintos | Pendiente | `RecintoController` — self, update, delete, all, verificar-catálogo |
| ms-compras | Pendiente | `CompraResponse` / carrito — links a confirmar, detalle boletos |
| ms-pagos | Pendiente | `PagoResponse` — link `aprobar` cuando estado sea PENDIENTE |
| ms-precios | Baja prioridad | CRUD simple; poco valor hipermedia |
| ms-notificaciones / ms-reportes | Baja prioridad | Principalmente lectura/consulta |

### Enriquecimiento de respuestas (sin copiar biblioteca)

- **ms-boletos:** incluir en `BoletoResponse` resumen de evento (`nombreEvento`, `fecha`) desde `proy_eventos` y nombre de zona — similar al enriquecimiento Feign de catálogo.
- **ms-usuarios:** incluir `perfil` embebido en `UsuarioResponse` al hacer GET por id (evitar segunda llamada).

### Endpoints sin HATEOAS (por diseño)

- `AuthController` — login/register/logout (transaccionales).
- `GET .../precio/id/{id}` — devuelve escalar, no recurso.
- `GET .../existe/*` — utilitarios booleanos.
- Clientes Feign internos entre microservicios.

---

## Seguridad y acceso

- [ ] **API Gateway** — punto único de entrada (`docs/05-Api-Gateway.md` es guía biblioteca; adaptar rutas RitmoTicket: `/eventos`, `/boletos`, etc.).
- [ ] **Swagger en todos los MS** — hoy documentado con detalle en catálogo; replicar anotaciones en boletos/usuarios/compras.
- [ ] **Validar usuario en compra** — `CompraService` con Feign a `ms-usuarios` antes de guardar.
- [ ] **Links HATEOAS según rol** — ocultar `delete`/`update` en `_links` si el token no tiene permiso (avanzado).

---

## Flujo de negocio / Kafka

- [ ] **Marcar boleto Vendido al confirmar compra** — consumer o llamada desde `CompraService.confirmar`.
- [ ] **Notificación automática** — consumer en `ms-notificaciones` tras `compras.compra.updated` → envío correo/SMS.
- [ ] **Reporte automático de venta** — consumer en `ms-reportes` al cerrar compra.
- [ ] **Eventos `pagos.pago.updated` para Fallido/Reembolsado** — ampliar `PagoService` más allá de `aprobar`.

---

## Datos y consistencia

- [ ] **Script de migración** para BDD ya cargadas con seed antiguo (`id_recinto` 101–103, etc.) sin recrear contenedores.
- [ ] **Boletos para eventos 3 y 4** — Lollapalooza y Mon Laferte en seed de inventario.
- [ ] **Lollapalooza multi-artista** — modelo alternativo si no quieres un solo `id_artista` en festivales.

---

## Calidad y operación

- [ ] Tests de integración HATEOAS — verificar presencia de `_links.self` en respuestas.
- [ ] Tests de contrato Feign entre MS.
- [ ] Despliegue Docker por microservicio (no solo Postgres/Kafka).
- [ ] Corregir hash BCrypt de Carlos en `10-usuarios.sql` si aún falla login en algún entorno.

---

## Documentación

- [ ] Apéndice RitmoTicket en `01-Seguridad-JJWT.md` (matriz de roles y puertos reales).
- [ ] Postman Collection versionada con flujo Carlos (login → eventos → compra → pago).

---

*Última actualización: junio 2026 — revisar al implementar cada ítem.*
