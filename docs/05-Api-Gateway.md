# API Gateway - Manual de Implementación y Uso

## Proyecto Biblioteca de Triskeledu

---

## Índice

1. [Descripción General](#1-descripción-general)
2. [Arquitectura del Sistema](#2-arquitectura-del-sistema)
3. [Cambios Realizados](#3-cambios-realizados)
4. [Estructura de Rutas y Balanceo de Carga](#4-estructura-de-rutas-y-balanceo-de-carga)
5. [Cómo Ejecutar el Sistema](#5-cómo-ejecutar-el-sistema)
6. [Cómo Ejecutar Múltiples Instancias para Redundancia](#6-cómo-ejecutar-múltiples-instancias-para-redundancia)
7. [Guía de Pruebas con Postman](#7-guía-de-pruebas-con-postman)
8. [Endpoints de Monitoreo (Actuator)](#8-endpoints-de-monitoreo-actuator)
9. [Troubleshooting](#9-troubleshooting)

---

## 1. Descripción General

### ¿Qué es un API Gateway?

Un API Gateway es un componente que actúa como **punto de entrada único** para todos los
microservicios de un sistema. En lugar de que los clientes (navegadores, Postman, apps móviles)
conozcan la dirección y puerto de cada microservicio individual, todas las peticiones se dirigen
a un solo lugar: el API Gateway.

### ¿Qué problema resuelve?

**Antes del API Gateway:**
```
Cliente → http://localhost:9001/api/v1/auth/login       (ms-usuarios)
Cliente → http://localhost:9001/api/v1/usuarios          (ms-usuarios)
Cliente → http://localhost:9002/api/v1/libros             (ms-catalogo)
Cliente → http://localhost:9003/api/v1/recursos           (ms-recursos)
```
El cliente necesita conocer 3 puertos diferentes y manejar las URLs de cada servicio.

**Después del API Gateway:**
```
Cliente → http://localhost:9000/api/v1/auth/login        → Gateway → ms-usuarios
Cliente → http://localhost:9000/api/v1/usuarios           → Gateway → ms-usuarios
Cliente → http://localhost:9000/api/v1/libros              → Gateway → ms-catalogo
Cliente → http://localhost:9000/api/v1/recursos            → Gateway → ms-recursos
```
El cliente solo necesita conocer una dirección: `http://localhost:9000`.

### Ventajas de usar API Gateway

| Ventaja | Descripción |
|---------|-------------|
| **Punto de entrada único** | Los clientes solo conocen una URL/puerto (9000) |
| **Balanceo de carga** | Distribuye automáticamente las peticiones entre múltiples instancias de un mismo servicio |
| **Transparencia** | Los microservicios no requieren cambios; el gateway es invisible para ellos |
| **CORS centralizado** | Se configura CORS una sola vez en el gateway en lugar de en cada microservicio |
| **Escalabilidad** | Se pueden agregar/quitar instancias de servicios sin cambiar la configuración del cliente |
| **Monitoreo** | El gateway proporciona métricas y estado de todas las rutas en un solo lugar |

---

## 2. Arquitectura del Sistema

### Diagrama de Flujo de una Petición

```
                                    ┌─────────────────────────┐
                                    │      Eureka Server      │
                                    │      Puerto: 8761       │
                                    │  (Registro de servicios)│
                                    └────────────┬────────────┘
                                                 │ Registra / Descubre
                                                 │
┌──────────┐    Petición HTTP     ┌──────────────┴──────────────┐
│  Cliente  │ ──────────────────► │        API GATEWAY          │
│ (Postman, │ ◄────────────────── │       Puerto: 9000          │
│ Frontend) │    Respuesta HTTP   │  (Spring Cloud Gateway)     │
└──────────┘                      └──────┬──────┬──────┬────────┘
                                         │      │      │
                          Balancea       │      │      │        Balancea
                       ┌─────────────────┘      │      └──────────────────┐
                       │                        │                         │
                       ▼                        ▼                         ▼
           ┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐
           │   ms-usuarios    │  │   ms-catalogo     │  │   ms-recursos     │
           │   Puerto: 9001   │  │   Puerto: 9002    │  │   Puerto: 9003    │
           │                  │  │                   │  │                   │
           │ /api/v1/auth/**  │  │ /api/v1/libros/** │  │ /api/v1/recursos/**│
           │ /api/v1/usuarios │  │                   │  │ /api/v1/libros-   │
           │                  │  │                   │  │   proyeccion/**   │
           └───────────────────┘  └───────────────────┘  └───────────────────┘
```

### Flujo Detallado de una Petición

1. **El cliente** envía una petición HTTP a `http://localhost:9000/api/v1/libros`
2. **El API Gateway** recibe la petición y evalúa sus rutas configuradas
3. La ruta con predicado `Path=/api/v1/libros/**` coincide → destino: `lb://ms-catalogo`
4. El gateway consulta a **Eureka** para obtener las instancias registradas de `ms-catalogo`
5. **Spring Cloud LoadBalancer** selecciona una instancia usando round-robin
6. El gateway **reenvía la petición** (incluyendo headers como `Authorization`) al microservicio seleccionado
7. El **microservicio** procesa la petición, valida el JWT y devuelve la respuesta
8. El gateway **retorna la respuesta** al cliente de forma transparente

---

## 3. Cambios Realizados

### 3.1 Archivos Nuevos (Módulo `api-gateway/`)

#### `api-gateway/pom.xml`
**Propósito:** Archivo Maven del nuevo módulo API Gateway.

**Decisiones de diseño:**
- Hereda del POM padre (`cl.triskeledu:biblioteca`) para reutilizar las versiones de Spring Boot 3.5.14 y Spring Cloud 2025.0.0.
- Utiliza `spring-cloud-starter-gateway` (reactivo/WebFlux) en lugar de `spring-cloud-starter-gateway-mvc` porque:
  - Es la implementación estándar y más madura de Spring Cloud Gateway.
  - Ofrece mejor rendimiento para un proxy/router gracias a su modelo de I/O no bloqueante.
  - No genera conflictos porque el gateway es un módulo independiente que NO depende del módulo `common` (que usa el stack MVC/servlet).
- Incluye `spring-cloud-starter-netflix-eureka-client` para descubrir servicios y habilitar balanceo de carga.
- Incluye `spring-boot-starter-actuator` para monitoreo y verificación de rutas activas.
- **NO incluye** el módulo `common` porque `common` tiene `spring-boot-starter-web` (MVC), que es incompatible con WebFlux.
- **NO incluye** dependencias de seguridad JWT porque cada microservicio ya maneja su propia autenticación. El gateway solo reenvía el header `Authorization` de forma transparente.

**Dependencias:**
| Dependencia | Propósito |
|-------------|-----------|
| `spring-cloud-starter-gateway` | Motor de enrutamiento reactivo (WebFlux) |
| `spring-cloud-starter-netflix-eureka-client` | Registro en Eureka + descubrimiento de servicios |
| `spring-boot-starter-actuator` | Endpoints de monitoreo y estado |
| `spring-boot-starter-test` | Soporte para testing |

---

#### `api-gateway/src/main/java/cl/triskeledu/gateway/BibliotecaGatewayApplication.java`
**Propósito:** Clase principal de la aplicación Spring Boot del gateway.

**Anotaciones:**
- `@SpringBootApplication` — Habilita la autoconfiguración de Spring Boot.
- `@EnableDiscoveryClient` — Registra el gateway en Eureka Server y habilita el descubrimiento de otros servicios.

---

#### `api-gateway/src/main/resources/application.yml`
**Propósito:** Configuración completa del gateway con rutas, CORS y perfiles.

**Configuración de rutas:** Se definen 5 rutas explícitas que cubren todas las APIs del sistema (detalladas en la sección 4).

**Configuración CORS:** Se configura de forma centralizada para permitir peticiones desde frontends en desarrollo (puertos 3000, 4200, 5173).

**Perfiles:** Se mantiene la misma convención de perfiles `dev`/`prod` que usan los demás microservicios.

---

### 3.2 Archivos Modificados

#### `pom.xml` (raíz del proyecto)
**Cambio:** Se agregó `<module>api-gateway</module>` a la sección `<modules>`.

**Razón:** Para que Maven compile e instale el módulo del gateway junto con los demás módulos cuando se ejecuta `mvn clean install` desde la raíz del proyecto.

**Tags:** Los cambios están marcados con `[GATEWAY-INI]` y `[GATEWAY-FIN]`.

```xml
<modules>
    <module>common</module>
    <module>eureka</module>
    <module>ms-usuarios</module>
    <module>ms-catalogo</module>
    <module>ms-recursos</module>
    <!-- [GATEWAY-INI] -->
    <module>api-gateway</module>
    <!-- [GATEWAY-FIN] -->
</modules>
```

---

#### `launch.bat`
**Cambios:**
1. Se agregó la opción `[9] Iniciar API Gateway` al menú principal.
2. Se incluyó el inicio del gateway en la opción "Iniciar todos los servicios (dev)" y "(test)".
3. Se agregó la sección `:RUN_GATEWAY` para inicio individual del gateway.
4. El gateway se inicia **después** de los microservicios (con un `timeout /t 5`) para asegurar que ya estén registrados en Eureka.

**Tags:** Todos los cambios están marcados con `[GATEWAY-INI]` y `[GATEWAY-FIN]`.

---

### 3.3 Archivos NO Modificados

| Archivo/Módulo | ¿Por qué no se modifica? |
|----------------|--------------------------|
| `common/` | El gateway no depende de `common` (stack diferente: WebFlux vs MVC) |
| `eureka/` | Eureka descubre el gateway automáticamente cuando éste se registra |
| `ms-usuarios/` | El gateway es transparente; el MS sigue validando JWT normalmente |
| `ms-catalogo/` | El gateway solo reenvía peticiones sin modificar paths ni headers |
| `ms-recursos/` | Los endpoints del MS se acceden con los mismos paths a través del gateway |
| `docker-compose.yml` | El gateway se ejecuta fuera de Docker (como los demás MS en desarrollo) |

---

## 4. Estructura de Rutas y Balanceo de Carga

### Tabla de Rutas

| ID de Ruta | Path en el Gateway | Microservicio Destino | URI con Balanceo | Ejemplos de URLs |
|---|---|---|---|---|
| `ms-usuarios-auth` | `/api/v1/auth/**` | ms-usuarios (9001) | `lb://ms-usuarios` | `POST /api/v1/auth/login`, `POST /api/v1/auth/register` |
| `ms-usuarios` | `/api/v1/usuarios/**` | ms-usuarios (9001) | `lb://ms-usuarios` | `GET /api/v1/usuarios`, `GET /api/v1/usuarios/1` |
| `ms-catalogo` | `/api/v1/libros/**` | ms-catalogo (9002) | `lb://ms-catalogo` | `GET /api/v1/libros`, `POST /api/v1/libros` |
| `ms-recursos` | `/api/v1/recursos/**` | ms-recursos (9003) | `lb://ms-recursos` | `GET /api/v1/recursos`, `GET /api/v1/recursos/sku/ABC` |
| `ms-recursos-proyeccion` | `/api/v1/libros-proyeccion/**` | ms-recursos (9003) | `lb://ms-recursos` | `GET /api/v1/libros-proyeccion` |

### ¿Cómo funciona el balanceo de carga?

El protocolo `lb://` (load balanced) en la URI de cada ruta activa **Spring Cloud LoadBalancer**:

1. Cuando llega una petición, el gateway consulta a Eureka por todas las instancias del servicio destino.
2. Si hay múltiples instancias (ej: ms-catalogo en puertos 9002 y 9012), el LoadBalancer las distribuye usando **round-robin** (alternando entre ellas secuencialmente).
3. Si una instancia se cae y Eureka la elimina de su registro, el LoadBalancer deja de enviarle peticiones automáticamente.

```
Petición 1 → ms-catalogo:9002
Petición 2 → ms-catalogo:9012
Petición 3 → ms-catalogo:9002
Petición 4 → ms-catalogo:9012
...
```

---

## 5. Cómo Ejecutar el Sistema

### Requisitos Previos
1. **Java 21** instalado y configurado en `JAVA_HOME`
2. **Maven** instalado y en el `PATH`
3. **Docker** ejecutándose (para PostgreSQL y Kafka vía `docker-compose.yml`)

### Paso 1: Iniciar la infraestructura (Docker)
```bash
cd c:\50-biblioteca-gateway
docker-compose up -d
```
Esto inicia PostgreSQL (puerto 5433) y Kafka (puerto 9092).

### Paso 2: Compilar todo el proyecto (incluye el nuevo módulo gateway)
```bash
cd c:\50-biblioteca-gateway
mvn clean install -DskipTests
```

### Paso 3: Iniciar los servicios en orden

**Opción A: Usando `launch.bat`**
- Ejecutar `launch.bat` y seleccionar la opción `[1]` para iniciar todos los servicios.
- El script inicia automáticamente: Eureka → Microservicios → API Gateway.

**Opción B: Manual (recomendado para entender el orden)**
```bash
# Terminal 1: Eureka Server (primero siempre)
mvn -f eureka spring-boot:run

# Esperar ~10 segundos hasta que Eureka esté listo

# Terminal 2: ms-usuarios
mvn -f ms-usuarios spring-boot:run

# Terminal 3: ms-catalogo
mvn -f ms-catalogo spring-boot:run

# Terminal 4: ms-recursos
mvn -f ms-recursos spring-boot:run

# Esperar ~5 segundos hasta que los MS se registren en Eureka

# Terminal 5: API Gateway (último)
mvn -f api-gateway spring-boot:run
```

### Paso 4: Verificar que todo funciona
1. Abrir Eureka Dashboard: `http://localhost:8761` — Deberías ver registrados: `API-GATEWAY`, `MS-USUARIOS`, `MS-CATALOGO`, `MS-RECURSOS`.
2. Verificar rutas del gateway: `GET http://localhost:9000/actuator/gateway/routes`
3. Verificar health del gateway: `GET http://localhost:9000/actuator/health`

---

## 6. Cómo Ejecutar Múltiples Instancias para Redundancia

Para lograr **redundancia y alta disponibilidad**, puedes ejecutar múltiples instancias de un mismo microservicio en puertos diferentes. El API Gateway balanceará automáticamente entre ellas.

### Ejemplo: 2 instancias de ms-catalogo

```bash
# Instancia 1 (puerto original 9002)
mvn -f ms-catalogo spring-boot:run

# Instancia 2 (puerto alternativo 9012)
mvn -f ms-catalogo spring-boot:run -Dspring-boot.run.arguments="--server.port=9012"
```

**Verificación en Eureka Dashboard:**
Abre `http://localhost:8761` y deberías ver 2 instancias de `MS-CATALOGO` registradas:
```
MS-CATALOGO    ms-catalogo:xxxxx  → 192.168.x.x:9002
MS-CATALOGO    ms-catalogo:yyyyy  → 192.168.x.x:9012
```

**Verificación de balanceo:**
Envía múltiples peticiones a `GET http://localhost:9000/api/v1/libros` y observa en los logs de cada instancia que las peticiones se alternan entre ambas.

### Ejemplo: 3 instancias de ms-usuarios
```bash
mvn -f ms-usuarios spring-boot:run
mvn -f ms-usuarios spring-boot:run -Dspring-boot.run.arguments="--server.port=9011"
mvn -f ms-usuarios spring-boot:run -Dspring-boot.run.arguments="--server.port=9021"
```

---

## 7. Guía de Pruebas con Postman

### Configuración Inicial

1. Crear una nueva **Colección** en Postman llamada "Biblioteca - API Gateway".
2. Crear una variable de colección `gateway_url` con valor `http://localhost:9000`.
3. Crear una variable de colección `token` (se llenará después del login).

### 7.1 Login (Obtener Token JWT)

```
POST {{gateway_url}}/api/v1/auth/login
Content-Type: application/json

Body (raw JSON):
{
    "email": "admin@biblioteca.cl",
    "password": "admin123"
}
```

**Respuesta esperada (200 OK):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

> **Nota:** Copia el valor del token y guárdalo en la variable `token` de Postman.
> En la pestaña "Scripts" (Post-response) puedes agregar:
> ```javascript
> var jsonData = pm.response.json();
> pm.collectionVariables.set("token", jsonData.token);
> ```

### 7.2 Registro de Nuevo Usuario

```
POST {{gateway_url}}/api/v1/auth/register
Content-Type: application/json

Body (raw JSON):
{
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@test.cl",
    "password": "password123"
}
```

### 7.3 Consultar Usuarios (Requiere JWT)

```
GET {{gateway_url}}/api/v1/usuarios
Authorization: Bearer {{token}}
```

### 7.4 Consultar Usuario por ID

```
GET {{gateway_url}}/api/v1/usuarios/1
Authorization: Bearer {{token}}
```

### 7.5 Consultar Libros del Catálogo (Requiere JWT)

```
GET {{gateway_url}}/api/v1/libros
Authorization: Bearer {{token}}
```

### 7.6 Crear un Libro

```
POST {{gateway_url}}/api/v1/libros
Authorization: Bearer {{token}}
Content-Type: application/json

Body (raw JSON):
{
    "titulo": "Don Quijote de la Mancha",
    "isbn": "978-84-376-0494-7",
    "autor": "Miguel de Cervantes",
    "anioPublicacion": 1605
}
```

### 7.7 Consultar Recursos Físicos (Requiere JWT)

```
GET {{gateway_url}}/api/v1/recursos
Authorization: Bearer {{token}}
```

### 7.8 Consultar Proyecciones de Libros

```
GET {{gateway_url}}/api/v1/libros-proyeccion
Authorization: Bearer {{token}}
```

### 7.9 Logout

```
POST {{gateway_url}}/api/v1/auth/logout
Authorization: Bearer {{token}}
```

### 7.10 Verificar que ya no se puede acceder (Token Inválido)

Después del logout, intenta acceder a cualquier endpoint protegido:
```
GET {{gateway_url}}/api/v1/libros
Authorization: Bearer {{token}}
```

**Respuesta esperada:** `401 Unauthorized`

### Resumen de Pruebas

| # | Método | URL | Auth | Esperado |
|---|--------|-----|------|----------|
| 1 | POST | `/api/v1/auth/login` | No | 200 + token |
| 2 | POST | `/api/v1/auth/register` | No | 201 Created |
| 3 | GET | `/api/v1/usuarios` | Bearer | 200 + lista usuarios |
| 4 | GET | `/api/v1/usuarios/1` | Bearer | 200 + usuario |
| 5 | GET | `/api/v1/libros` | Bearer | 200 + lista libros |
| 6 | POST | `/api/v1/libros` | Bearer | 201 + libro creado |
| 7 | GET | `/api/v1/recursos` | Bearer | 200 + lista recursos |
| 8 | GET | `/api/v1/libros-proyeccion` | Bearer | 200 + proyecciones |
| 9 | POST | `/api/v1/auth/logout` | Bearer | 200 OK |
| 10 | GET | `/api/v1/libros` | Bearer (expirado) | 401 Unauthorized |

> **Importante:** Todos los endpoints usan el puerto **9000** (gateway) en lugar de los puertos
> individuales de cada microservicio (9001, 9002, 9003). Los microservicios siguen accesibles
> directamente por sus puertos, pero la forma recomendada es siempre usar el gateway.

---

## 8. Endpoints de Monitoreo (Actuator)

El API Gateway expone endpoints de monitoreo vía Spring Boot Actuator:

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `GET /actuator/health` | GET | Estado de salud del gateway |
| `GET /actuator/info` | GET | Información de la aplicación |
| `GET /actuator/gateway/routes` | GET | Lista todas las rutas configuradas |
| `GET /actuator/gateway/globalfilters` | GET | Lista los filtros globales activos |
| `GET /actuator/gateway/routefilters` | GET | Lista los filtros de ruta disponibles |

### Ejemplo: Ver rutas activas
```
GET http://localhost:9000/actuator/gateway/routes
```

**Respuesta:**
```json
[
    {
        "predicate": "Paths: [/api/v1/auth/**], match trailing slash: true",
        "route_id": "ms-usuarios-auth",
        "filters": ["DedupeResponseHeader..."],
        "uri": "lb://ms-usuarios",
        "order": 0
    },
    ...
]
```

---

## 9. Troubleshooting

### Error: "503 Service Unavailable"
**Causa:** El microservicio destino no está registrado en Eureka.
**Solución:**
1. Verificar que el microservicio está corriendo.
2. Verificar que aparece en Eureka Dashboard (`http://localhost:8761`).
3. Esperar ~30 segundos para que Eureka propague el registro.

### Error: "Connection refused" al acceder al gateway
**Causa:** El gateway no está corriendo.
**Solución:** Iniciar el gateway con `mvn -f api-gateway spring-boot:run`.

### El gateway no aparece en Eureka
**Causa:** Eureka no está corriendo o el gateway no puede conectarse.
**Solución:**
1. Verificar que Eureka está corriendo en el puerto 8761.
2. Verificar la propiedad `eureka.client.service-url.defaultZone` en `application.yml` del gateway.

### Las rutas no funcionan después de agregar un nuevo microservicio
**Solución:** Agregar una nueva entrada en la sección `spring.cloud.gateway.routes` del `application.yml` del gateway. Ejemplo:
```yaml
- id: mi-nuevo-servicio
  uri: lb://nombre-en-eureka
  predicates:
    - Path=/api/v1/mi-nuevo-path/**
```

### El balanceo de carga no funciona
**Verificación:**
1. Confirmar que hay múltiples instancias del servicio registradas en Eureka Dashboard.
2. Verificar que ambas instancias tienen `instance-id` diferentes (deben usar `${random.value}`).
3. Enviar múltiples peticiones y revisar los logs de cada instancia.

### Error: "No servers available for service"
**Causa:** El nombre del servicio en la ruta (`lb://nombre`) no coincide con el `spring.application.name` del microservicio.
**Solución:** Verificar que los nombres coincidan exactamente (case-insensitive):
- Ruta: `lb://ms-catalogo` ← debe coincidir con →
- Microservicio: `spring.application.name: ms-catalogo`

---

## Resumen Final

| Componente | Puerto | Función |
|-----------|--------|---------|
| Eureka Server | 8761 | Registro y descubrimiento de servicios |
| **API Gateway** | **9000** | **Punto de entrada único, enrutamiento y balanceo de carga** |
| ms-usuarios | 9001 | Autenticación JWT y gestión de usuarios |
| ms-catalogo | 9002 | Catálogo de libros |
| ms-recursos | 9003 | Recursos físicos y proyecciones |

**Regla de oro:** Los clientes siempre deben usar `http://localhost:9000` (el gateway) para acceder a cualquier servicio del sistema.
