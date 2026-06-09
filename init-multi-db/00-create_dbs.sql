/* ============================================================
   ARCHIVO: 00-create_dbs.sql
   Propósito: Crear bases de datos independientes por microservicio.
   Ejecutar conectado a la base postgres: psql -U postgres -d postgres -f 00-create_dbs.sql
   ============================================================ */

-- ES FUNDAMENTAL EJECUTAR ESTE SCRIPT QUE PERMITE ELIMINAR LAS BASES DE DATOS
-- SI ES QUE EXISTEN, PARA LUEGO CREARLAS LIMPIAS SIN TABLAS Y DESDE CERO

SELECT 'CREATE DATABASE artistas'       WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'artistas') \gexec
SELECT 'CREATE DATABASE boletos'        WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'boletos') \gexec
SELECT 'CREATE DATABASE catalogo'       WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'catalogo') \gexec
SELECT 'CREATE DATABASE compras'        WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'compras') \gexec
SELECT 'CREATE DATABASE notificaciones' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'notificaciones') \gexec
SELECT 'CREATE DATABASE pagos'          WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'pagos') \gexec
SELECT 'CREATE DATABASE precios'        WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'precios') \gexec
SELECT 'CREATE DATABASE recintos'       WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'recintos') \gexec
SELECT 'CREATE DATABASE reportes'       WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'reportes') \gexec
SELECT 'CREATE DATABASE usuarios'       WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'usuarios') \gexec
