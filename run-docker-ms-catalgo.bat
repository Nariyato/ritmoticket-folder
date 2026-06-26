@echo off
setlocal
cd /d "%~dp0"
cls
echo ====================================================
echo    NUEVA INSTANCIA DE MS-CATALOGO EN DOCKER
echo ====================================================
echo.

rem Buscar un puerto libre entre 13300 y 13399
set PORT=
for /L %%P in (13300,1,13399) do (
    if not defined PORT (
        powershell -Command "try { $l = [System.Net.Sockets.TcpListener]::new([System.Net.IPAddress]::Any, %%P); $l.Start(); $l.Stop(); Write-Output 'free' } catch { Write-Output 'used' }" | findstr /I "free" >nul 2>&1
        if not errorlevel 1 set PORT=%%P
    )
)

if not defined PORT (
    echo ERROR: No se encontro ningun puerto libre entre 13300 y 13399.
    pause
    exit /b 1
)

echo [1/3] Puerto seleccionado libre: %PORT%
echo.

rem Compilar solo ms-catalogo (y sus dependencias como common)
echo [2/3] Compilando ms-catalogo con Maven...
call mvn clean package -DskipTests -q -pl ms-catalogo -am
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ****************************************************
    echo ERROR: La compilacion Maven de ms-catalogo fallo.
    echo ****************************************************
    pause
    exit /b 1
)
echo       [OK] JAR generado.
echo.

rem Construir la imagen Docker de ms-catalogo via compose
echo [3/3] Construyendo imagen Docker de ms-catalogo...
docker compose build ms-catalogo
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ****************************************************
    echo ERROR: No se pudo construir la imagen Docker.
    echo Asegurate de que Docker esta ejecutandose.
    echo ****************************************************
    pause
    exit /b 1
)
echo       [OK] Imagen construida.
echo.

rem Levantar la nueva instancia con el puerto dinamico
set CONTAINER_NAME=ms-catalogo-%PORT%
echo       Levantando contenedor %CONTAINER_NAME% en puerto %PORT%...
docker run -d ^
    --name %CONTAINER_NAME% ^
    --network ritmoticket-network ^
    -e SPRING_PROFILES_ACTIVE=dev ^
    -e SERVER_PORT=%PORT% ^
    -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-rdbms:5432/catalogo ^
    -e SPRING_DATASOURCE_USERNAME=postgres ^
    -e SPRING_DATASOURCE_PASSWORD=123 ^
    -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka-broker:29092 ^
    -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka ^
    -e EUREKA_INSTANCE_PREFER_IP_ADDRESS=true ^
    -p %PORT%:%PORT% ^
    ritmoticket-ms-catalogo

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ****************************************************
    echo ERROR: No se pudo levantar el contenedor Docker.
    echo Asegurate de que Docker esta ejecutandose y la red existe.
    echo Recuerda: el sistema base debe estar corriendo (run-dockers.bat)
    echo ****************************************************
    pause
    exit /b 1
)

echo.
echo ====================================================
echo   INSTANCIA LEVANTADA CORRECTAMENTE
echo ====================================================
echo.
echo   Contenedor : %CONTAINER_NAME%
echo   Puerto     : http://localhost:%PORT%
echo   Eureka     : se registrara automaticamente
echo.
echo   Para detener esta instancia:
echo   docker stop %CONTAINER_NAME% ^&^& docker rm %CONTAINER_NAME%
echo ====================================================
endlocal
pause
