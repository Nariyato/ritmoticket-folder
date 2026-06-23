# Implementación de Seguridad con JWT

Este documento detalla la implementación de seguridad basada en JSON Web Tokens (JWT) en el proyecto Biblioteca-Kafka.

## 1. Introducción a JWT

Un JSON Web Token (JWT) es un estándar abierto (RFC 7519) que define una forma compacta y autónoma de transmitir información de forma segura entre las partes como un objeto JSON. Esta información puede ser verificada y en la que se puede confiar porque está firmada digitalmente.

Un JWT consta de tres partes separadas por puntos (`.`):

1. **Header (Cabecera):** Típicamente consta de dos partes: el tipo de token (JWT) y el algoritmo de firma utilizado, como HMAC SHA256 o RSA. Por ejemplo:

```json
{
   "alg": "HS256",
   "typ": "JWT"
}
```

2. **Payload (Carga útil):** Contiene los *claims* (declaraciones). Los claims son declaraciones sobre una entidad (típicamente, el usuario) y datos adicionales. Hay claims registrados (estándar), públicos y privados. En nuestro caso, incluimos el email, rol y nombre del usuario. Por ejemplo:
```json
{
  "email": "cgomezvega@triskeledu.cl",
  "rol": "Administrador",
  "nombre": "Cristian Gomez"
}
```

3. **Signature (Firma):** Para crear la parte de la firma, se debe tomar la cabecera codificada, la carga útil codificada, un secreto y el algoritmo especificado en la cabecera, para luego firmarlo. Esto verifica que el remitente del JWT es quien dice ser y asegura que el mensaje no ha sido alterado. Por ejemplo:

```
S3g2YTlCclN0cjhXNXZlekFidTFjRHRmR2g0aktsTXA1TjZPM1F5UjBTOD0
```

Como texto plano, un token real se ve así de largo y continuo (sin espacios ni saltos de línea):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImNnb21lenZlZ2FAdHJpc2tlbGVkdS5jbCIsInJvbCI6IkFVTUFfUk9MRSIsIm5vbWJyZSI6IkNyaXN0aWFuIEdvbWV6In0.S3g2YTlCclN0cjhXNXZlekFidTFjRHRmR2g0aktsTXA1TjZPM1F5UjBTOD0
```

## 2. Arquitectura de Seguridad del Proyecto

El proyecto implementa un modelo de seguridad distribuido adaptado a microservicios:

* **Microservicio Usuarios (`ms-usuarios`):** Actúa como **Servidor de Autenticación (Auth Server)**. Es el único componente que tiene acceso directo a las credenciales de los usuarios en la base de datos (hasheadas con BCrypt). Es responsable de verificar las credenciales y emitir tokens JWT válidos.
* **Otros Microservicios (`ms-catalogo`, `ms-recursos`):** Actúan como **Servidores de Recursos (Resource Servers)**. Confían en los tokens emitidos por el servidor de autenticación. No validan contraseñas; en su lugar, verifican criptográficamente la firma del JWT usando una clave secreta compartida y autorizan las peticiones basándose en los claims (roles) contenidos en el token.
* **Módulo Common (`common`):** Contiene la lógica de seguridad compartida (filtros, proveedores de tokens, interceptores) para garantizar consistencia y evitar duplicación de código en todos los microservicios.

## 3. Fragmentos de Código Clave

A continuación, se explican los componentes centrales de la implementación:

### A. Proveedor de Tokens (`JwtTokenProvider.java`)

Genera y valida los tokens JWT. Reside en el módulo `common`.

```java
public String generarToken(String email, String rol, String nombreCompleto) {
    Date ahora = new Date();
    Date expiracion = new Date(ahora.getTime() + jwtProperties.getExpirationMs());

    return Jwts.builder()
            .subject(email)                        // Subject = email del usuario
            .claim("rol", rol)                     // Claim personalizado: rol
            .claim("nombre", nombreCompleto)       // Claim personalizado: nombre
            .issuedAt(ahora)                       // Fecha de emisión
            .expiration(expiracion)                // Fecha de expiración
            .signWith(getSigningKey())             // Firma con HMAC-SHA256
            .compact();
}
```

*Explicación:* Crea el token estableciendo el *subject* (identificador principal), *claims* personalizados (rol, nombre), tiempo de validez y firma criptográfica.

### B. Filtro de Autenticación (`JwtAuthenticationFilter.java`)

Intercepta todas las peticiones HTTP para extraer y validar el JWT. Reside en `common`.

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
    String token = extraerToken(request); // Extrae de "Authorization: Bearer <token>"
    if (StringUtils.hasText(token) && jwtTokenProvider.validarToken(token)) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        String rol = jwtTokenProvider.getRolFromToken(token);
    
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
        UsernamePasswordAuthenticationToken authentication 
                = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
}
```

*Explicación:* Busca el token en la cabecera `Authorization`. Si existe y es válido, inyecta la identidad del usuario y su rol en el contexto de Spring Security. Si el token es inválido, simplemente deja que Spring Security rechace el acceso.

### C. Configuración de Seguridad (`SecurityConfig.java` - Ejemplo de ms-catalogo)

Define qué endpoints requieren qué permisos. Cada microservicio tiene la suya.

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Stateless, sin cookies, no hay riesgo CSRF
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/libros/**").hasAnyRole("Administrador", "Bibliotecario", "Cliente")
            .requestMatchers(HttpMethod.POST, "/api/v1/libros/**").hasAnyRole("Administrador", "Bibliotecario")
            // ... otras reglas ...
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

*Explicación:* Configura la aplicación como "stateless" (sin mantener sesión en memoria), define reglas de autorización basadas en roles (RBAC) y posiciona el filtro JWT antes de los procesos de autenticación predeterminados.

### D. Flujo de Autenticación (`AuthService.java` en ms-usuarios)

Gestión de inicio de sesión con encriptación BCrypt y manejo de bloqueos.

```java
public LoginResponse login(LoginRequest request) {
    Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
  
    // Validar contraseña con BCrypt
    if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
        registrarIntentoFallido(usuario, credencial); // Incrementa intentos y puede bloquear
        throw new RuntimeException("Credenciales inválidas");
    }
  
    registrarAccesoExitoso(usuario, credencial);
    String token = jwtTokenProvider.generarToken(usuario.getEmail(), usuario.getRol(), ...);
  
    return LoginResponse.builder().token(token).build();
}
```

*Explicación:* Busca al usuario, compara la contraseña en texto plano con el hash almacenado de manera segura usando `PasswordEncoder.matches`, gestiona intentos fallidos para prevenir ataques de fuerza bruta y devuelve el token tras el éxito.

### E. Endpoints Públicos (`AuthController.java` en ms-usuarios)

Expone las interfaces de inicio de sesión y registro público.

```java
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
}

@PostMapping("/register") // Asigna automáticamente rol Cliente y encripta password
public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
}
```

### F. Interceptor Feign (`FeignClientInterceptor.java` en common)

Propaga la identidad en llamadas entre microservicios.

```java
@Override
public void apply(RequestTemplate template) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getCredentials() instanceof String token) {
        template.header("Authorization", "Bearer " + token);
    }
}
```

*Explicación:* Cuando un microservicio (e.g. `ms-catalogo`) llama a otro (`ms-recursos`) a través de un cliente Feign, este interceptor adjunta automáticamente el token de la petición original, para que el servicio destino reconozca y autorice al usuario.

## 4. Configuración (application.yml)

Para que el ecosistema confíe en los mismos tokens, todos los microservicios deben compartir la misma configuración base:

```yaml
jwt:
  secret: "dG9rZW5TZWNyZXRLZXlCaWJsaW90ZWNhMjAyNlRyaXNrZWxlZHVTZWN1cml0eQ=="
  expiration-ms: 3600000   # 1 hora en milisegundos
```

* `secret`: Es una clave generada en Base64. Debe ser larga (mínimo 256 bits para HMAC SHA-256) y guardarse en un lugar seguro (por ejemplo, inyectada mediante variables de entorno). **El valor de este proyecto es solo para desarrollo**.
* `expiration-ms`: Define el tiempo de vida útil de un token desde su creación (aquí configurado a 1 hora).

## 5. Matriz de Autorización por Endpoint

| Microservicio   | Endpoint                         | Método HTTP      | Roles Permitidos                      |
| :-------------- | :------------------------------- | :---------------- | :------------------------------------ |
| `ms-usuarios` | `/api/v1/auth/**`              | ALL               | Público (Todos)                      |
| `ms-usuarios` | `/api/v1/usuarios/**`          | GET               | Administrador, Bibliotecario          |
| `ms-usuarios` | `/api/v1/usuarios/**`          | POST, PUT, DELETE | Administrador                         |
| `ms-catalogo` | `/api/v1/libros/**`            | GET               | Administrador, Bibliotecario, Cliente |
| `ms-catalogo` | `/api/v1/libros/**`            | POST, PUT, DELETE | Administrador, Bibliotecario          |
| `ms-recursos` | `/api/v1/recursos/**`          | GET               | Administrador, Bibliotecario, Cliente |
| `ms-recursos` | `/api/v1/recursos/**`          | POST, PUT, DELETE | Administrador, Bibliotecario          |
| `ms-recursos` | `/api/v1/libros-proyeccion/**` | GET               | Administrador, Bibliotecario, Cliente |
| Todos           | `/actuator/**`                 | ALL               | Público (Monitoreo)                  |

## 6. Cómo probar con Postman

Siga esta guía paso a paso para testear la seguridad de la API:

### Usuarios de prueba:

* **Administrador**: `ana@administrador.cl` / `Biblio@2026`
* **Bibliotecario**: `beatriz@bibliotecario.cl` / `Biblio@2026`
* **Cliente**: `carlos@cliente.cl` / `Biblio@2026`

### Paso 1: Intentar acceder sin credenciales (401)

1. En Postman, cree una petición **GET** hacia `http://localhost:9001/api/v1/usuarios`.
2. Asegúrese de que en la pestaña "Authorization", el tipo es "No Auth".
3. Envíe la petición. Recibirá un error `401 Unauthorized`.

### Paso 2: Iniciar Sesión (Login) y obtener el Token

1. Cree una petición **POST** a `http://localhost:9001/api/v1/auth/login`.
2. En la pestaña "Body", seleccione "raw" y "JSON".
3. Ingrese las credenciales (por ejemplo, del Cliente):
   ```json
   {
       "email": "carlos@cliente.cl",
       "password": "Biblio@2026"
   }
   ```
4. Envíe la petición. La respuesta (200 OK) contendrá el `token`.
5. **Copie el valor del token** sin las comillas.

### Paso 3: Configurar Autenticación para Peticiones

1. Cree una petición **GET** a `http://localhost:9002/api/v1/libros` (ms-catalogo).
2. Vaya a la pestaña **Authorization**.
3. En la lista desplegable **Type**, seleccione **Bearer Token**.
4. En el campo **Token**, pegue el valor que copió en el paso anterior. *(Nota: En entornos reales de Postman, puede configurar el token en una variable de entorno como `{{jwt_token}}` para automatizar esto usando scripts)*.
5. Envíe la petición. Debería obtener un `200 OK` y la lista de libros, porque el Cliente tiene acceso de lectura.

### Paso 4: Validar Autorización (403 Forbidden)

1. Con el mismo token del usuario "Cliente", intente realizar una operación no permitida para él, por ejemplo, **DELETE** a `http://localhost:9002/api/v1/libros/1`.
2. Vaya a Authorization -> Bearer Token y use el token del Cliente.
3. Envíe la petición. Debería recibir un error `403 Forbidden`, porque solo los Administradores y Bibliotecarios pueden borrar libros.
4. Si repite este paso utilizando el token de la cuenta de "Ana" (Administrador), la operación de eliminación sería exitosa (o devolvería un 404 si el ID no existe, pero nunca un 403).

## 7. Buenas Prácticas de Seguridad Implementadas (y Recomendadas)

1. **Encriptación Fuerte de Contraseñas:** Se usa **BCrypt** (con salt). Nunca almacenar en texto plano, md5 o sha1.
2. **No Exponer el Hash:** Las contraseñas encriptadas jamás deben ser expuestas en las respuestas de la API (`UsuarioResponse` no la incluye).
3. **Protección Contra Fuerza Bruta:** Se implementó un límite de intentos fallidos (5) que bloquea temporalmente o permanentemente la cuenta en la tabla `credenciales_usuarios`.
4. **JWT Cortos:** Los tokens tienen un tiempo de expiración corto (ej. 1 hora) limitando la ventana de daño si son comprometidos.
5. **Secreto JWT Seguro y Externo:** El secreto (`jwt.secret`) debe ser largo, complejo y nunca exponerse en repositorios públicos. Se debe inyectar vía variables de entorno en producción.
6. **APIs Stateless:** Evita completamente el uso de sesiones de memoria, eliminando vulnerabilidades asociadas y permitiendo una fácil escalabilidad horizontal.
7. **Roles y Privilegios Mínimos (RBAC):** Se aplica el principio de menor privilegio al nivel más granular necesario usando `@EnableWebSecurity` y configuraciones exactas en cada microservicio.
8. **Inmunidad a CSRF:** Dado que es una API Stateless basada en headers en lugar de cookies de sesión automáticas, está inherentemente protegida contra Cross-Site Request Forgery (ataque que suplanta la identidad).
9. **Intercepción Feign Segura:** Las comunicaciones intra-cluster (microservicio a microservicio) propagan contextos de identidad autenticados, evitando abrir endpoints que puedan ser explotados internamente.
10. **Manejo Uniforme de Excepciones:** Se gestionan errores 401 y 403 en `GlobalExceptionHandler` evitando fugas de información interna como stacktraces.
11. **Validación de Entradas:** Todas las clases DTO (`LoginRequest`, `RegisterRequest`) validan firmemente la estructura de los datos que ingresan (largo, formato de email) para prevenir inyecciones.
12. **Uso de HTTPS en Producción (TLS/SSL):** Todo el tráfico debe ser cifrado para evitar interceptación de tokens en texto claro. (Recomendación a implementar en Gateway/Ingress).
13. **Evitar Almacenar Tokens en `localStorage`:** Los clientes web (frontend) no deben usar localstorage por riesgo de ataques XSS; es mejor usar cookies "HttpOnly".
14. **Auditoría de Logs:** Los inicios de sesión incorrectos, así como las cuentas inactivas y bloqueadas, emiten advertencias en los registros de la aplicación (`log.warn`) listos para ser monitoreados con herramientas de observabilidad.
15. **Rotación de Claves:** Se deben proporcionar mecanismos para renovar los tokens secret base (`jwt.secret`) de manera regular y planificada.
