-- =========================================
-- 09-notificaciones.sql
-- =========================================

\c notificaciones

DROP TABLE IF EXISTS proy_compras;
DROP TABLE IF EXISTS proy_usuarios;
DROP TABLE IF EXISTS sms;
DROP TABLE IF EXISTS correos;
DROP TABLE IF EXISTS notificaciones;

CREATE TABLE notificaciones (
    id_notificacion SERIAL PRIMARY KEY,
    mensaje VARCHAR(200),
    tipo VARCHAR(50),
    fecha_envio DATE,
    estado VARCHAR(20)
);

CREATE TABLE correos (
    id_correo SERIAL PRIMARY KEY,
    destinatario VARCHAR(100),
    asunto VARCHAR(100),
    fecha DATE,
    estado VARCHAR(20)
);

CREATE TABLE sms (
    id_sms SERIAL PRIMARY KEY,
    telefono VARCHAR(20),
    mensaje VARCHAR(200),
    fecha DATE,
    estado VARCHAR(20)
);

CREATE TABLE proy_usuarios (
    id_usuario INT,
    nombre VARCHAR(100),
    correo VARCHAR(100)
);

CREATE TABLE proy_compras (
    id_compra INT,
    total NUMERIC(10,2),
    estado VARCHAR(20)
);