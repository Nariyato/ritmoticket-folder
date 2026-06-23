@echo off
cls
echo ====================================================
echo      REINICIO TOTAL: INFRAESTRUCTURA Y DATOS
echo ====================================================

echo 1. DETENIENDO CONTENEDORES Y LIMPIANDO REDES...
:: down baja los contenedores del compose actual
docker compose down

echo 2. ELIMINANDO VOLUMENES (DATOS DE KAFKA Y POSTGRES)...
:: Esto asegura que los topics y las tablas desaparezcan de verdad
docker volume prune -f

echo 3. INICIANDO INFRAESTRUCTURA (DB, KAFKA, UI)...
docker compose up -d

echo 4. ESPERANDO A QUE LOS SERVICIOS ESTEN LISTOS...
:: Kafka y Postgres necesitan unos segundos para inicializarse antes de recibir comandos
timeout /t 15 /nobreak

echo 5. COMPILANDO BASES DE DATOS ADICIONALES...
:: Entramos a tu carpeta de utilidades de DB
cd init-multi-db
call docker_compile_dbs.bat

echo.
echo ====================================================
echo              ¡PROCESO FINALIZADO!
echo ====================================================
echo - Kafka UI: http://localhost:8080
echo - Postgres: localhost:5433
echo.
echo IMPORTANTE: Antes de lanzar tu App, verifica en el 
echo navegador que el cluster aparezca "Online".
echo ====================================================
pause
