@echo off
setlocal

:MENU
cls
echo.
echo ============================================
echo   Ritmoticket - MENU PRINCIPAL
echo ============================================
echo.
echo   [1] Iniciar todos los servicios (dev)
echo   [2] Iniciar todos los servicios (test)
echo   [3] Compilar microservicios
echo   [4] Reinstalar dependencias Maven
echo.
echo   --- Servicios individuales ---
echo   [5] Iniciar Eureka
echo   [6] Iniciar ms-artistas

echo   [7] Iniciar ms-boletos

echo   [8] Iniciar ms-catalogo

echo   [9] Iniciar ms-compras

echo   [10] Iniciar ms-notificaciones

echo   [11] Iniciar ms-pagos

echo   [12] Iniciar ms-precios

echo   [13] Iniciar ms-recintos

echo   [14] Iniciar ms-reportes

echo   [15] Iniciar ms-usuarios
echo.
echo   [0] Salir
echo.
echo ============================================
set /p opcion="  Selecciona una opcion: "

if "%opcion%"=="1" goto RUN_ALL
if "%opcion%"=="2" goto RUN_TEST
if "%opcion%"=="3" goto COMPILE
if "%opcion%"=="4" goto INSTALL
if "%opcion%"=="5" goto RUN_EUREKA
if "%opcion%"=="6" goto RUN_ARTISTAS

if "%opcion%"=="7" goto RUN_BOLETOS

if "%opcion%"=="8" goto RUN_CATALOGO

if "%opcion%"=="9" goto RUN_COMPRAS

if "%opcion%"=="10" goto RUN_NOTIFICACIONES

if "%opcion%"=="11" goto RUN_PAGOS

if "%opcion%"=="12" goto RUN_PRECIOS

if "%opcion%"=="13" goto RUN_RECINTOS

if "%opcion%"=="14" goto RUN_REPORTES

if "%opcion%"=="15" goto RUN_USUARIOS
if "%opcion%"=="0" goto SALIR

echo.
echo   Opcion invalida. Intenta de nuevo.
timeout /t 2 /nobreak > nul
goto MENU

REM ============================================

:RUN_ALL
cls
echo.
echo ===== Iniciando Eureka Server =====
start "EUREKA" mvn -f eureka spring-boot:run
timeout /t 5 /nobreak > nul
echo ===== Iniciando Microservicios =====
start "MS-ARTISTAS" mvn -f ms-artistas spring-boot:run

start "MS-BOLETOS" mvn -f ms-boletos spring-boot:run

start "MS-CATALOGO" mvn -f ms-catalogo spring-boot:run

start "MS-COMPRAS" mvn -f ms-compras spring-boot:run

start "MS-NOTIFICACIONES" mvn -f ms-notificaciones spring-boot:run

start "MS-PAGOS" mvn -f ms-pagos spring-boot:run

start "MS-PRECIOS" mvn -f ms-precios spring-boot:run

start "MS-RECINTOS" mvn -f ms-recintos spring-boot:run

start "MS-REPORTES" mvn -f ms-reportes spring-boot:run

start "MS-USUARIOS" mvn -f ms-usuarios spring-boot:run
rem  Se inicia el API Gateway despues de los microservicios para que encuentre servicios en Eureka
timeout /t 5 /nobreak > nul
echo ===== Iniciando API Gateway =====
start "API-GATEWAY" mvn -f api-gateway spring-boot:run

echo Todos los servicios han sido lanzados.
pause
goto MENU

:RUN_TEST
cls
echo.
echo ===== Iniciando Eureka Server (test) =====
start "EUREKA" java -jar eureka\target\cl-triskeledu-eureka-1.0-SNAPSHOT.jar --spring.profiles.active=test
timeout /t 5 /nobreak > nul
echo ===== Iniciando Microservicios (test) =====
start "MS-ARTISTAS" java -jar ms-artistas\\target\\cl-triskeledu-artistas-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

start "MS-BOLETOS" java -jar ms-boletos\\target\\cl-triskeledu-boletos-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

start "MS-CATALOGO" java -jar ms-catalogo\\target\\cl-triskeledu-catalogo-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

start "MS-COMPRAS" java -jar ms-compras\\target\\cl-triskeledu-compras-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

start "MS-NOTIFICACIONES" java -jar ms-notificaciones\\target\\cl-triskeledu-notificaciones-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

start "MS-PAGOS" java -jar ms-pagos\\target\\cl-triskeledu-pagos-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

start "MS-PRECIOS" java -jar ms-precios\\target\\cl-triskeledu-precios-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

start "MS-RECINTOS" java -jar ms-recintos\\target\\cl-triskeledu-recintos-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

start "MS-REPORTES" java -jar ms-reportes\\target\\cl-triskeledu-reportes-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

start "MS-USUARIOS" java -jar ms-usuarios\\target\\cl-triskeledu-usuarios-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
rem  Se inicia el API Gateway en modo test
timeout /t 5 /nobreak > nul
echo ===== Iniciando API Gateway (test) =====
start "API-GATEWAY" java -jar api-gateway\\target\\cl-triskeledu-gateway-0.0.1-SNAPSHOT.jar --spring.profiles.active=test

echo Todos los servicios han sido lanzados en modo test.
pause
goto MENU

:COMPILE
cls
echo.
echo ===== Compilando microservicios =====
cd /d C:\ritmoticket-folder\ms-artistas

call mvn clean install -U

cd /d C:\ritmoticket-folder\ms-boletos

call mvn clean install -U

cd /d C:\ritmoticket-folder\ms-catalogo

call mvn clean install -U

cd /d C:\ritmoticket-folder\ms-compras

call mvn clean install -U

cd /d C:\ritmoticket-folder\ms-notificaciones

call mvn clean install -U

cd /d C:\ritmoticket-folder\ms-pagos

call mvn clean install -U

cd /d C:\ritmoticket-folder\ms-precios

call mvn clean install -U

cd /d C:\ritmoticket-folder\ms-recintos

call mvn clean install -U

cd /d C:\ritmoticket-folder\ms-reportes

call mvn clean install -U

cd /d C:\ritmoticket-folder\ms-usuarios

call mvn clean install -U

echo Compilacion completada.
pause
goto MENU

:INSTALL
cls
echo.
echo === REINSTALACION DE DEPENDENCIAS MAVEN ===
echo.
echo Eliminando carpeta .m2 ...
rmdir /s /q %USERPROFILE%\.m2
echo Eliminando carpetas target ...
rmdir /s /q C:\ritmoticket-folder\eureka\target

rmdir /s /q C:\ritmoticket-folder\ms-artistas\target

rmdir /s /q C:\ritmoticket-folder\ms-boletos\target

rmdir /s /q C:\ritmoticket-folder\ms-catalogo\target

rmdir /s /q C:\ritmoticket-folder\ms-compras\target

rmdir /s /q C:\ritmoticket-folder\ms-notificaciones\target

rmdir /s /q C:\ritmoticket-folder\ms-pagos\target

rmdir /s /q C:\ritmoticket-folder\ms-precios\target

rmdir /s /q C:\ritmoticket-folder\ms-recintos\target

rmdir /s /q C:\ritmoticket-folder\ms-reportes\target

rmdir /s /q C:\ritmoticket-folder\ms-usuarios\target

echo Descargando dependencias nuevamente con Maven ...
mvn clean install -U -DskipTests
echo.
echo === PROCESO COMPLETADO ===
pause
goto MENU

:RUN_EUREKA
cls
echo.
echo ===== Iniciando Eureka =====
start "EUREKA" mvn -f eureka spring-boot:run
echo Eureka iniciado.
pause
goto MENU

:RUN_ARTISTAS

cls

echo.

echo ===== Iniciando ms-artistas =====

start "MS-ARTISTAS" mvn -f ms-artistas spring-boot:run

echo ms-artistas iniciado.

pause

goto MENU



:RUN_BOLETOS

cls

echo.

echo ===== Iniciando ms-boletos =====

start "MS-BOLETOS" mvn -f ms-boletos spring-boot:run

echo ms-boletos iniciado.

pause

goto MENU



:RUN_CATALOGO

cls

echo.

echo ===== Iniciando ms-catalogo =====

start "MS-CATALOGO" mvn -f ms-catalogo spring-boot:run

echo ms-catalogo iniciado.

pause

goto MENU



:RUN_COMPRAS

cls

echo.

echo ===== Iniciando ms-compras =====

start "MS-COMPRAS" mvn -f ms-compras spring-boot:run

echo ms-compras iniciado.

pause

goto MENU



:RUN_NOTIFICACIONES

cls

echo.

echo ===== Iniciando ms-notificaciones =====

start "MS-NOTIFICACIONES" mvn -f ms-notificaciones spring-boot:run

echo ms-notificaciones iniciado.

pause

goto MENU



:RUN_PAGOS

cls

echo.

echo ===== Iniciando ms-pagos =====

start "MS-PAGOS" mvn -f ms-pagos spring-boot:run

echo ms-pagos iniciado.

pause

goto MENU



:RUN_PRECIOS

cls

echo.

echo ===== Iniciando ms-precios =====

start "MS-PRECIOS" mvn -f ms-precios spring-boot:run

echo ms-precios iniciado.

pause

goto MENU



:RUN_RECINTOS

cls

echo.

echo ===== Iniciando ms-recintos =====

start "MS-RECINTOS" mvn -f ms-recintos spring-boot:run

echo ms-recintos iniciado.

pause

goto MENU



:RUN_REPORTES

cls

echo.

echo ===== Iniciando ms-reportes =====

start "MS-REPORTES" mvn -f ms-reportes spring-boot:run

echo ms-reportes iniciado.

pause

goto MENU



:RUN_USUARIOS

cls

echo.

echo ===== Iniciando ms-usuarios =====

start "MS-USUARIOS" mvn -f ms-usuarios spring-boot:run

echo ms-usuarios iniciado.

pause

goto MENU

rem Seccion para iniciar el API Gateway de forma individual
:RUN_GATEWAY
cls
echo.
echo ===== Iniciando API Gateway =====
start "API-GATEWAY" mvn -f api-gateway spring-boot:run
echo API Gateway iniciado en puerto 9000.
pause
goto MENU

:SALIR
cls
echo.
echo   Hasta luego :D.
echo.
endlocal
exit /b
