# Reporte: Implementación de HATEOAS

Documento de referencia para **Biblioteca-Kafka** (ejemplo del curso) y **RitmoTicket** (proyecto actual).

## ¿Qué es HATEOAS y para qué sirve aquí?

**HATEOAS** (Hypermedia as the Engine of Application State) es el nivel más alto del modelo REST. La idea es simple: cada respuesta de la API incluye **enlaces (`_links`)** que le dicen al cliente qué puede hacer a continuación, sin que el cliente tenga que conocer las URLs de memoria.

### Ejemplo de lo que cambia

**Sin HATEOAS** (respuesta actual):
```json
{
  "id": 1,
  "titulo": "Clean Code",
  "isbn": "978-0132350884"
}
```

**Con HATEOAS** (respuesta nueva):
```json
{
  "id": 1,
  "titulo": "Clean Code",
  "isbn": "978-0132350884",
  "_links": {
    "self":             { "href": "http://localhost:9002/api/v1/libros/1" },
    "update":           { "href": "http://localhost:9002/api/v1/libros/1" },
    "delete":           { "href": "http://localhost:9002/api/v1/libros/1" },
    "agregar-categoria":{ "href": "http://localhost:9002/api/v1/libros/libro/1/categoria/{categoriaId}" },
    "all":              { "href": "http://localhost:9002/api/v1/libros" }
  }
}
```

---

## ¿Dónde aplicarlo en tu proyecto?

### Aplicar HATEOAS — Prioridad ALTA

| Microservicio   | Controlador               | Por qué                                                                    |
|-----------------|---------------------------|----------------------------------------------------------------------------|
| ms-catalogo     | LibroController           | Recurso central. Tiene relaciones con categorías y recursos físicos.       |
| ms-usuarios     | UsuarioController         | Tiene operaciones extra: activar/desactivar. HATEOAS las hace descubribles.|
| ms-recursos     | RecursoFisicoController   | Tiene filtros por disponibilidad y tipo que vale la pena enlazar.          |

### No aplicar (o diferir) — Prioridad BAJA

| Elemento          | Razón                                                                  |
|-------------------|------------------------------------------------------------------------|
| AuthController    | Login/logout/register son endpoints transaccionales, no recursos REST. |
| Clientes Feign    | Son llamadas internas entre microservicios, no expuestas al cliente.   |
| existByIsbn       | Endpoint utilitario (Boolean), no un recurso con navegación.           |

---

## Qué links agregar en cada controlador

### LibroController (ms-catalogo)

| Endpoint           | Links en la respuesta                                    |
|--------------------|----------------------------------------------------------|
| GET /libros        | Por cada libro: self, update, delete, agregar-categoria  |
| GET /libros/{id}   | self, update, delete, agregar-categoria, all             |
| POST /libros       | self, update, delete, all                                |
| PUT /libros/{id}   | self, delete, all                                        |

### UsuarioController (ms-usuarios)

| Endpoint              | Links en la respuesta                                       |
|-----------------------|-------------------------------------------------------------|
| GET /usuarios         | Por cada usuario: self, update, delete, activar, desactivar |
| GET /usuarios/{id}    | self, update, delete, activar, desactivar, all              |
| POST /usuarios        | self, update, delete, all                                   |
| PUT /{id}/activar     | self, desactivar, delete, all                               |
| PUT /{id}/desactivar  | self, activar, delete, all                                  |

### RecursoFisicoController (ms-recursos)

| Endpoint              | Links en la respuesta                                    |
|-----------------------|----------------------------------------------------------|
| GET /recursos         | Por cada recurso: self, update, delete                   |
| GET /recursos/{id}    | self, update, delete, disponibles, all                   |
| POST /recursos        | self, update, delete, all                                |

---

## Archivos a modificar por microservicio

### ms-catalogo (3 archivos)
1. pom.xml — agregar spring-boot-starter-hateoas
2. LibroResponse.java — extender RepresentationModel
3. LibroController.java — agregar links en cada endpoint

### ms-usuarios (3 archivos)
1. pom.xml — agregar spring-boot-starter-hateoas
2. UsuarioResponse.java — extender RepresentationModel
3. UsuarioController.java — agregar links

### ms-recursos (3 archivos)
1. pom.xml — agregar spring-boot-starter-hateoas
2. RecursoFisicoResponse.java — extender RepresentationModel
3. RecursoFisicoController.java — agregar links

> Nota: el pom.xml padre NO necesita cambios. La dependencia se agrega en cada módulo hijo,
> igual que se hizo con springdoc/Swagger.

---

## RitmoTicket — `EventoController` (ms-catalogo)

Implementado en el catálogo de conciertos (`/api/v1/eventos`). El DTO `EventoResponseDTO` extiende `RepresentationModel` e incluye datos enriquecidos (`artista`, `recinto`) además de `_links`.

### Ejemplo de respuesta

```json
{
  "idEvento": 1,
  "nombreEvento": "Gira Ven Aquí - Los Bunkers",
  "idArtista": 1,
  "idRecinto": 1,
  "artista": { "idArtista": 1, "nombreArtistico": "Los Bunkers", "genero": "Rock Latino" },
  "recinto": { "idRecinto": 1, "nombre": "Estadio Nacional", "ciudad": "Santiago" },
  "_links": {
    "self":              { "href": "http://localhost:9003/api/v1/eventos/id/1" },
    "update":            { "href": "http://localhost:9003/api/v1/eventos/id/1" },
    "delete":            { "href": "http://localhost:9003/api/v1/eventos/id/1" },
    "verificar-artista": { "href": "http://localhost:9003/api/v1/eventos/existe/artista/1" },
    "verificar-recinto": { "href": "http://localhost:9003/api/v1/eventos/existe/recinto/1" },
    "all":               { "href": "http://localhost:9003/api/v1/eventos" }
  }
}
```

### Links por endpoint

| Endpoint | Links en la respuesta |
|----------|------------------------|
| `GET /eventos` | Por cada evento: self, update, delete, verificar-artista, verificar-recinto, all |
| `GET /eventos/id/{id}` | Igual que arriba |
| `POST /eventos` | self, update, delete, verificar-*, all |
| `PUT /eventos/id/{id}` | self, delete, verificar-*, all |

### Sin HATEOAS (endpoints utilitarios)

| Endpoint | Motivo |
|----------|--------|
| `GET /eventos/existe/artista/{id}` | Devuelve `Boolean`, no un recurso navegable |
| `GET /eventos/existe/recinto/{id}` | Idem |
| `GET /eventos/existe/idEvento/{id}` | Idem |

### Archivos modificados (RitmoTicket)

1. `ms-catalogo/pom.xml` — `spring-boot-starter-hateoas`
2. `EventoResponseDTO.java` — extiende `RepresentationModel`
3. `EventoController.java` — método `addLinks()` + `CollectionModel` en listado
4. `EventoService.java` — lógica de negocio y enriquecimiento Feign (sin capa `*Impl`)

---

## RitmoTicket — `BoletoController` (ms-boletos)

Puerto **9002**, ruta base `/api/v1/boletos`.

### Links

| Rel | Método | Ruta |
|-----|--------|------|
| `self` | GET | `/api/v1/boletos/id/{id}` |
| `update` | PUT | `/api/v1/boletos/id/{id}` |
| `delete` | DELETE | `/api/v1/boletos/id/{id}` |
| `consultar-precio` | GET | `/api/v1/boletos/precio/id/{id}` |
| `all` | GET | `/api/v1/boletos` |

### Sin HATEOAS

- `GET /precio/id/{id}` — respuesta numérica (usado por `ms-compras` vía Feign).
- `ProyEventoController` — proyección local de eventos Kafka.

### Archivos

1. `BoletoResponse.java` — extiende `RepresentationModel`
2. `BoletoController.java` — `addLinks()` + `CollectionModel`

---

## RitmoTicket — `UsuarioController` (ms-usuarios)

Puerto **9010**, ruta base `/api/v1/usuarios`. Estructura alineada con el ejemplo de biblioteca.

### Links

| Rel | Método | Ruta |
|-----|--------|------|
| `self` | GET | `/api/v1/usuarios/id/{id}` |
| `update` | PUT | `/api/v1/usuarios/id/{id}` |
| `delete` | DELETE | `/api/v1/usuarios/id/{id}` |
| `activar` | PUT | `/api/v1/usuarios/id/{id}/activar` |
| `desactivar` | PUT | `/api/v1/usuarios/id/{id}/desactivar` |
| `all` | GET | `/api/v1/usuarios` |

### Endpoints añadidos (paridad biblioteca)

- `PUT /id/{id}/activar` y `PUT /id/{id}/desactivar` — exponen la lógica que ya existía en `UsuarioService`.

### Sin HATEOAS

- `AuthController` (`/api/v1/auth/**`)
- `POST /perfiles/idUsuario/{id}` — `PerfilResponse` sin hipermedia (mejora opcional en [MEJORAS-OPCIONALES.md](./MEJORAS-OPCIONALES.md))

### Archivos

1. `UsuarioResponse.java` — extiende `RepresentationModel`
2. `UsuarioController.java` — `addLinks()` + `CollectionModel` + activar/desactivar

---

## Roadmap HATEOAS y otras mejoras

Ver [MEJORAS-OPCIONALES.md](./MEJORAS-OPCIONALES.md) para extensiones futuras (artistas, recintos, compras, enriquecimiento de DTOs, etc.).
