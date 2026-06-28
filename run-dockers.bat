@echo off
setlocal
cd /d "%~dp0"
cls
echo ====================================================
echo    RITMOTICKET TRISKELEDU - DESPLIEGUE EN DOCKER
echo ====================================================
echo.

rem Paso 1: Detener y eliminar contenedores existentes
echo [1/5] DETENIENDO Y ELIMINANDO CONTENEDORES ANTERIORES...
docker compose down -v --remove-orphans 2>nul
echo       Contenedores y volumenes anteriores eliminados.
echo.

rem Paso 2: Compilar todos los JARs con Maven
echo [2/5] COMPILANDO PROYECTO JAVA CON MAVEN...
echo       (common, eureka, api-gateway, ms-artistas, ms-boletos, ms-catalogo, ms-compras, ms-notificaciones, ms-pagos, ms-precios, ms-recintos, ms-reportes, ms-usuarios)
echo.
call mvn clean package -DskipTests -q
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ****************************************************
    echo ERROR: La compilacion Maven fallo.
    echo Revisa los errores arriba y corrige antes de reintentar.
    echo ****************************************************
    pause
    exit /b 1
)
echo       Compilacion exitosa. JARs generados.
echo.

rem Paso 3: Levantar Base de Datos y Mensajeria
echo [3/5] INICIANDO POSTGRES Y KAFKA...
docker compose up -d postgres kafka
echo.
echo       Esperando a que PostgreSQL este completamente activo (healthy)...
powershell -Command "while ((docker inspect --format '{{.State.Health.Status}}' postgres-rdbms 2>$null) -ne 'healthy') { Start-Sleep -Seconds 2 }"
echo       [OK] PostgreSQL esta listo.
echo.

rem Paso 4: Esperar a que PostgreSQL termine la inicializacion automatica
echo [4/5] ESPERANDO INICIALIZACION DE BASES DE DATOS...
echo       Los scripts SQL se ejecutan solos al crear el volumen (sin relanzarlos).
echo.
powershell -NoProfile -Command ^
  "$max = 90; " ^
  "for ($i = 0; $i -lt $max; $i++) { " ^
  "  $status = docker inspect --format '{{.State.Status}}' postgres-rdbms 2>$null; " ^
  "  if ($status -ne 'running') { Start-Sleep -Seconds 2; continue }; " ^
  "  $check = docker exec postgres-rdbms psql -U postgres -d artistas -tAc 'SELECT 1 FROM artistas LIMIT 1;' 2>$null; " ^
  "  if ($LASTEXITCODE -eq 0 -and $check.Trim() -eq '1') { exit 0 }; " ^
  "  Start-Sleep -Seconds 2 " ^
  "}; " ^
  "Write-Host 'ERROR: PostgreSQL no termino de cargar los datos de prueba a tiempo.'; " ^
  "exit 1"
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: La inicializacion de PostgreSQL no completo. Ver: docker logs postgres-rdbms
    pause
    exit /b 1
)
echo       [OK] Bases de datos creadas y cargadas con datos de prueba.
echo.

rem Paso 5: Levantar Eureka, Gateway y Microservicios
echo [5/5] LEVANTANDO EUREKA, API GATEWAY Y MICROSERVICIOS...
echo.

rem 1. Iniciar Eureka
echo       Iniciando Eureka Server...
docker compose up -d --build eureka-server
echo       Esperando a que Eureka Server este saludable...
powershell -Command "while ((docker inspect --format '{{.State.Health.Status}}' eureka-server 2>$null) -ne 'healthy') { Start-Sleep -Seconds 2 }"
echo       [OK] Eureka Server esta activo y saludable.
echo.

rem 2. Iniciar API Gateway
echo       Iniciando API Gateway...
docker compose up -d --build api-gateway
echo       Esperando a que API Gateway este saludable...
powershell -Command "while ((docker inspect --format '{{.State.Health.Status}}' api-gateway 2>$null) -ne 'healthy') { Start-Sleep -Seconds 2 }"
echo       [OK] API Gateway esta activo y saludable.
echo.

rem 3. Iniciar Microservicios y Kafka UI
echo       Reconstruyendo imagenes Docker con los JARs recien compilados...
echo       Iniciando microservicios (ms-artistas, ms-boletos, ms-catalogo, ms-compras, ms-notificaciones, ms-pagos, ms-precios, ms-recintos, ms-reportes, ms-usuarios)...
docker compose up -d --build ms-artistas ms-boletos ms-catalogo ms-compras ms-notificaciones ms-pagos ms-precios ms-recintos ms-reportes ms-usuarios kafka-ui
echo.
echo       Esperando 25 segundos a que los microservicios se registren en Eureka...
powershell -Command "Start-Sleep -Seconds 25"
echo       [OK] Microservicios listos.
echo.

echo ====================================================
echo           SISTEMA COMPLETAMENTE OPERATIVO
echo ====================================================
echo.
echo   Servicios disponibles:
echo   -----------------------------------------------
echo   API Gateway:    http://localhost:9000
echo   Eureka:         http://localhost:8761
echo   Kafka UI:       http://localhost:8080
echo   PostgreSQL:     localhost:5433
echo   -----------------------------------------------
echo.
echo   Para detener todo: docker compose down -v
echo ====================================================
endlocal
pause
