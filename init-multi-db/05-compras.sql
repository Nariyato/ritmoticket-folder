-- =========================================
-- 05-compras.sql
-- =========================================

\c compras

DROP TABLE IF EXISTS proy_boletos;
DROP TABLE IF EXISTS proy_pagos;
DROP TABLE IF EXISTS carritos;
DROP TABLE IF EXISTS detalle_compras;
DROP TABLE IF EXISTS compras;

CREATE TABLE compras (
    id_compra SERIAL PRIMARY KEY,
    id_usuario INT,
    fecha DATE,
    total NUMERIC(10,2),
    estado VARCHAR(20)
);

CREATE TABLE detalle_compras (
    id_detalle SERIAL PRIMARY KEY,
    id_compra INT REFERENCES compras(id_compra),
    id_boleto INT,
    cantidad INT,
    subtotal NUMERIC(10,2)
);

CREATE TABLE carritos (
    id_carrito SERIAL PRIMARY KEY,
    id_usuario INT,
    fecha_creacion DATE,
    estado VARCHAR(20),
    total NUMERIC(10,2)
);

CREATE TABLE proy_pagos (
    id_pago INT,
    monto NUMERIC(10,2),
    estado VARCHAR(20)
);

CREATE TABLE proy_boletos (
    id_boleto INT,
    codigo VARCHAR(50),
    estado VARCHAR(20)
);