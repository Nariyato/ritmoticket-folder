# Guía de Desarrollo: Implementación de Seguridad JWT

Esta guía secuencial detalla cómo transformar el código base de la plataforma "Biblioteca-Kafka" para incorporar autenticación y autorización mediante JSON Web Tokens (JWT) y Spring Security.

## Etapa 1: Comprender la Arquitectura de Seguridad

Antes de escribir código, debemos comprender dos conceptos fundamentales:
1. **Autenticación (¿Quién eres?):** El microservicio `ms-usuarios` verificará las credenciales del usuario y, si son correctas, firmará y devolverá un token JWT.
2. **Autorización (¿Qué puedes hacer?):** Los demás microservicios (`ms-catalogo` y `ms-recursos`) examinarán el token provisto para determinar el rol del usuario y si se le permite ejecutar la acción solicitada.

**Matriz de Permisos del Sistema:**
- **Cliente:** Sólo puede leer datos (libros, recursos).
- **Bibliotecario:** Puede leer y modificar datos del catálogo y recursos (pero no usuarios).
- **Administrador:** Control total sobre todos los microservicios.

---

## Etapa 2: Agregar Dependencias Maven

¿Por qué? Necesitamos librerías para manejar criptografía (Spring Security) y generación/validación de tokens (JJWT).

**Paso 2.1: Actualizar el archivo `pom.xml` padre (`c:\32-biblioteca-kafka\pom.xml`)**
Defina las versiones globales. Agregue dentro del bloque `<properties>`:
```xml
<jjwt.version>0.12.6</jjwt.version>
```

Y dentro de `<dependencyManagement><dependencies>`:
```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>${spring-boot.version}</version>
</dependency>
<!-- JJWT API, Impl, Jackson -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>${jjwt.version}</version>
</dependency>
<!-- ... (repetir para jjwt-impl y jjwt-jackson) ... -->
```

**Paso 2.2: Actualizar `common/pom.xml` (`c:\32-biblioteca-kafka\common\pom.xml`)**
Como todo microservicio depende de `common`, añadir aquí las dependencias garantiza que se propaguen.
Agregue en `<dependencies>`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
</dependency>
<!-- ... (incluir impl y jackson scope runtime) ... -->
```

---

## Etapa 3: Crear la Librería de Seguridad en `common`

Construiremos los cimientos en el módulo compartido para garantizar coherencia en toda la plataforma.

**Paso 3.1: Propiedades JWT**
Cree la clase `JwtProperties.java` en `cl.triskeledu.common.security`. Anotarla con `@ConfigurationProperties(prefix = "jwt")` vincula las claves `secret` y `expiration-ms` del `application.yml` a código Java, evitando strings fijos.

**Paso 3.2: Proveedor de Token**
Cree `JwtTokenProvider.java`. Esta clase utilizará la librería JJWT para firmar tokens usando `SecretKey` y `HMAC-SHA256`. Aquí codificará los métodos de generación (`Jwts.builder()...`) y verificación (`Jwts.parser()...`).

**Paso 3.3: Filtro de Peticiones**
Cree `JwtAuthenticationFilter.java` extendiendo `OncePerRequestFilter`. 
¿Por qué? Este filtro examinará la cabecera `Authorization` de **todas** las peticiones HTTP, validará el "Bearer token" a través de nuestro Provider, e inyectará al usuario en el `SecurityContextHolder` para que Spring lo reconozca como autenticado.

**Paso 3.4: Interceptor Feign**
Cree `FeignClientInterceptor.java` implementando `RequestInterceptor`. Extrae el token actual de Spring Security y lo pega como un nuevo encabezado en peticiones salientes a otros microservicios.

**Paso 3.5: Auto-Configuración**
Modifique `CommonAutoConfiguration.java` para añadir `@EnableConfigurationProperties(JwtProperties.class)`.

**Paso 3.6: Manejador de Excepciones**
En `GlobalExceptionHandler.java`, agregue métodos `@ExceptionHandler` para atrapar `AuthenticationException` (devuelve 401) y `AccessDeniedException` (devuelve 403), logrando respuestas de error limpias en formato JSON.

---

## Etapa 4: Configurar `ms-usuarios` como Servidor de Autenticación

**Paso 4.1: Modelo de Base de datos**
En `Usuario.java`, amplíe la longitud del campo contraseña.
```java
@Column(name = "password", nullable = false, length = 255)
```
¿Por qué? Los hashes generados por algoritmos como BCrypt son largos (usualmente de 60 caracteres) y no caben en una columna de `length=50`.

**Paso 4.2: DTOs para Entrada/Salida**
Cree clases en `cl.triskeledu.usuarios.dto`: `LoginRequest` (email, password), `RegisterRequest` (datos personales para un nuevo registro) y `LoginResponse` (contendrá el token String).

**Paso 4.3: Servicio y Controlador de Autenticación**
Cree `AuthService.java` y `AuthController.java`. Implemente la validación de contraseñas haciendo uso de `passwordEncoder.matches(raw, hashed)`. Si tiene éxito, devuelva un `generarToken()`.

**Paso 4.4: Modificar Servicio de Usuarios**
En `UsuarioService.java`, inyecte `PasswordEncoder`. Durante los procesos de registro (`create` y `update`), asegúrese de codificar la contraseña antes de guardar en repositorio:
```java
usuario.setPassword(passwordEncoder.encode(request.getPassword()));
```

**Paso 4.5: Configuración de Spring Security**
Cree `SecurityConfig.java` en `ms-usuarios`. Deshabilite CSRF, establezca la sesión `STATELESS`, aplique las reglas `.requestMatchers("/api/v1/auth/**").permitAll()`, exija roles para otras rutas, e introduzca el filtro con `.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)`.

**Paso 4.6: Modificar YAML**
En `application.yml`, introduzca el bloque de variables para el JWT (estas coincidirán con `@ConfigurationProperties`):
```yaml
jwt:
  secret: "suClaveBase64..."
  expiration-ms: 3600000
```

---

## Etapa 5: Configurar `ms-catalogo` como Servidor de Recursos

**Paso 5.1: Configuración de Seguridad**
Cree `SecurityConfig.java`. La estructura es similar, excepto que no se habilitan rutas `/auth`. Solo permita accesos públicos en `/actuator/**` y asigne reglas de escritura a los roles "Administrador" y "Bibliotecario".

**Paso 5.2: Variables de entorno YAML**
Copie estrictamente el mismo bloque `jwt` configurado en `ms-usuarios` dentro de `application.yml` en `ms-catalogo`. Sin la misma clave secreta, las firmas fallarán al verificar.

---

## Etapa 6: Configurar `ms-recursos` como Servidor de Recursos

**Paso 6.1: Configuración de Seguridad y YAML**
Repita el mismo procedimiento ejecutado en la Etapa 5, adaptando las rutas `/api/v1/recursos/**` en las verificaciones de tipo `requestMatchers`. Asegúrese de que el `jwt.secret` sea idéntico.

---

## Etapa 7: Actualizar la Base de Datos Inicial (Docker)

Dado que las contraseñas antiguas estaban en texto plano y hemos migrado, las validaciones fallarán irremediablemente.

**Paso 7.1: Archivo SQL Inicial**
Edite `c:\32-biblioteca-kafka\init-multi-db\01-usuarios.sql`.
*   Cambie la definición de la tabla: `password VARCHAR(255) NOT NULL,`
*   Actualice todos los `INSERT` de la contraseña `'Biblio@2026'` por su equivalencia segura con BCrypt (ej: `$2b$10$...`).

**Paso 7.2: Reiniciar Entorno**
Para que la base de datos Postgres de Docker tome este cambio inicial de volúmenes, derribe y limpie los volúmenes en consola:
```bash
docker-compose down -v
docker-compose up -d
```

---

## Etapa 8: Verificación y Pruebas

Finalmente, en Postman (tal como se indica en la guía de seguridad `SEGURIDAD-JWT.md`):
1. Confirme que la recuperación de libros (GET `/api/v1/libros`) falla (Error `401`).
2. Autentique exitosamente a un "Cliente" enviando sus credenciales (POST `/api/v1/auth/login`).
3. Adjunte el Bearer Token en los headers y vuelva a intentar el GET de libros (Éxito `200`).
4. Intente borrar un libro como cliente y corrobore la restricción por rol (Error `403`).
5. Repita el paso de borrado con las credenciales de un Administrador (Éxito `200` o `204`).
