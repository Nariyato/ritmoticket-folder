# Reporte: Implementación de HATEOAS en Biblioteca

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
