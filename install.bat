@echo off
echo.
echo === REINSTALACION DE DEPENDENCIAS MAVEN ===
echo.

REM Paso 1: Eliminar carpeta local de dependencias
echo Eliminando carpeta .m2 ...
rmdir /s /q %USERPROFILE%\.m2

REM Paso 2: Eliminar carpetas target de los proyectos
echo Eliminando carpetas target ...
rmdir /s /q C:\rimtoticket-folder\eureka\target
rmdir /s /q C:\rimtoticket-folder\ms-artistas\target


rmdir /s /q C:\rimtoticket-folder\ms-compras\target
rmdir /s /q C:\rimtoticket-folder\ms-notificaciones\target
rmdir /s /q C:\rimtoticket-folder\ms-pagos\target

rmdir /s /q C:\rimtoticket-folder\ms-recintos\target
rmdir /s /q C:\rimtoticket-folder\ms-reportes\target
rmdir /s /q C:\rimtoticket-folder\ms-usuarios\target


REM Paso 3: Instalar todas las dependencias forzadamente
echo Descargando dependencias nuevamente con Maven ...
mvn clean install -U -DskipTests

echo.
echo === PROCESO COMPLETADO ===
pause
