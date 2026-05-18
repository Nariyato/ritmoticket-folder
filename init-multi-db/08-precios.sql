-- =========================================
-- 07-precios.sql
-- =========================================

\c precios

DROP TABLE IF EXISTS proy_boletos;
DROP TABLE IF EXISTS proy_eventos;
DROP TABLE IF EXISTS promociones;
DROP TABLE IF EXISTS descuentos;
DROP TABLE IF EXISTS precios;

CREATE TABLE precios (
    id_precio SERIAL PRIMARY KEY,
    tipo_boleto VARCHAR(50),
    valor NUMERIC(10,2),
    moneda VARCHAR(10),
    estado VARCHAR(20)
);

CREATE TABLE descuentos (
    id_descuento SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    porcentaje INT,
    fecha_inicio DATE,
    fecha_fin DATE
);

CREATE TABLE promociones (
    id_promocion SERIAL PRIMARY KEY,
    descripcion VARCHAR(100),
    codigo VARCHAR(50),
    estado VARCHAR(20),
    expiracion DATE
);

CREATE TABLE proy_eventos (
    id_evento INT,
    nombre_evento VARCHAR(100),
    fecha DATE
);

CREATE TABLE proy_boletos (
    id_boleto INT,
    codigo VARCHAR(50),
    estado VARCHAR(20)
);