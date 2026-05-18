-- =========================================
-- 04-boletos.sql
-- =========================================

\c boletos

DROP TABLE IF EXISTS proy_eventos;
DROP TABLE IF EXISTS proy_usuarios;
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS zonas;
DROP TABLE IF EXISTS boletos;

CREATE TABLE boletos (
    id_boleto SERIAL PRIMARY KEY,
    codigo VARCHAR(50),
    tipo VARCHAR(50),
    estado VARCHAR(20),
    fecha_emision DATE
);

CREATE TABLE zonas (
    id_zona SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    capacidad INT,
    precio_base NUMERIC(10,2),
    estado VARCHAR(20)
);

CREATE TABLE reservas (
    id_reserva SERIAL PRIMARY KEY,
    id_boleto INT REFERENCES boletos(id_boleto),
    fecha_reserva DATE,
    estado VARCHAR(20),
    expiracion DATE
);

CREATE TABLE proy_usuarios (
    id_usuario INT,
    nombre VARCHAR(100),
    correo VARCHAR(100)
);

CREATE TABLE proy_eventos (
    id_evento INT,
    nombre_evento VARCHAR(100),
    fecha DATE
);