@echo off
setlocal
cd /d "%~dp0"
cls
echo ====================================================
echo    INSTANCIA ADICIONAL DE MS-CATALOGO EN DOCKER
echo ====================================================
echo.
echo   Agrega UNA instancia extra de ms-catalogo al cluster.
echo   Puedes ejecutar este script varias veces; cada vez
echo   levanta otra instancia en un puerto distinto.
echo.
echo   Requisito: stack base arriba (run-dockers.bat).
echo   No uses "docker compose up ms-catalogo" para esto:
echo   ese comando reemplaza la instancia principal (:9003).
echo.

rem Buscar puerto libre: sin contenedor ms-catalogo-PUERTO y puerto TCP disponible
set PORT=
for /L %%P in (13300,1,13399) do (
    if not defined PORT (
        docker inspect ms-catalogo-%%P >nul 2>&1
        if errorlevel 1 (
            powershell -NoProfile -Command "try { $l = [System.Net.Sockets.TcpListener]::new([System.Net.IPAddress]::Any, %%P); $l.Start(); $l.Stop(); exit 0 } catch { exit 1 }"
            if not errorlevel 1 set PORT=%%P
        )
    )
)

if not defined PORT (
    echo ERROR: No hay puerto libre entre 13300 y 13399.
    pause
    exit /b 1
)

set CONTAINER_NAME=ms-catalogo-%PORT%
echo [1/4] Nueva instancia: %CONTAINER_NAME%  ^|  Puerto: %PORT%
echo.

echo [2/4] Compilando ms-catalogo con Maven...
call mvn clean package -DskipTests -q -pl ms-catalogo -am
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: La compilacion Maven de ms-catalogo fallo.
    pause
    exit /b 1
)
echo       [OK] JAR generado.
echo.

echo [3/4] Reconstruyendo imagen Docker (sin cache, JAR recien compilado)...
docker compose build --no-cache ms-catalogo
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ****************************************************
    echo ERROR: No se pudo construir la imagen Docker.
    echo Asegurate de que Docker esta ejecutandose.
    echo ****************************************************
    pause
    exit /b 1
)

jar tf ms-catalogo\target\cl-triskeledu-catalogo-0.0.1-SNAPSHOT.jar | findstr /C:"EventoMapperImpl.class" >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: El JAR no contiene EventoMapperImpl. Revisa la compilacion Maven/MapStruct.
    pause
    exit /b 1
)
echo       [OK] Imagen actualizada y JAR verificado.
echo.

echo [4/4] Levantando instancia en puerto %PORT%...
docker run -d ^
    --name %CONTAINER_NAME% ^
    --network ritmoticket-network ^
    -e SPRING_PROFILES_ACTIVE=dev ^
    -e SERVER_PORT=%PORT% ^
    -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/catalogo ^
    -e SPRING_DATASOURCE_USERNAME=postgres ^
    -e SPRING_DATASOURCE_PASSWORD=123 ^
    -e SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=3 ^
    -e SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=1 ^
    -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:29092 ^
    -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka ^
    -e EUREKA_INSTANCE_HOSTNAME=%CONTAINER_NAME% ^
    -e EUREKA_INSTANCE_PREFER_IP_ADDRESS=false ^
    -p %PORT%:%PORT% ^
    ritmoticket-ms-catalogo

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: No se pudo levantar el contenedor.
    echo.
    echo Si el nombre ya existe ^(instancia detenida^), eliminalo y reintenta:
    echo   docker rm %CONTAINER_NAME%
    echo.
    echo Ver instancias existentes:
    echo   docker ps -a --filter "name=ms-catalogo"
    pause
    exit /b 1
)

echo.
echo ====================================================
echo   INSTANCIA ADICIONAL LEVANTADA
echo ====================================================
echo   Contenedor : %CONTAINER_NAME%
echo   Puerto     : http://localhost:%PORT%
echo   Gateway    : http://localhost:9000/api/v1/eventos
echo   Eureka     : http://localhost:8761
echo.
echo   Ver instancias activas:
echo   docker ps --filter "name=ms-catalogo"
echo.
echo   Detener esta instancia:
echo   docker stop %CONTAINER_NAME% ^&^& docker rm %CONTAINER_NAME%
echo.
echo   Para otra instancia mas, ejecuta este script de nuevo.
echo ====================================================
endlocal
pause
