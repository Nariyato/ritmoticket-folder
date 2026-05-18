-- =========================================
-- 10-reportes.sql
-- =========================================

\c reportes

DROP TABLE IF EXISTS proy_compras;
DROP TABLE IF EXISTS proy_pagos;
DROP TABLE IF EXISTS auditorias;
DROP TABLE IF EXISTS estadisticas;
DROP TABLE IF EXISTS reportes;

CREATE TABLE reportes (
    id_reporte SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    fecha_generacion DATE,
    tipo VARCHAR(50),
    estado VARCHAR(20)
);

CREATE TABLE estadisticas (
    id_estadistica SERIAL PRIMARY KEY,
    descripcion VARCHAR(100),
    valor INT,
    fecha DATE,
    categoria VARCHAR(50)
);

CREATE TABLE auditorias (
    id_auditoria SERIAL PRIMARY KEY,
    accion VARCHAR(100),
    usuario VARCHAR(50),
    fecha DATE,
    estado VARCHAR(20)
);

CREATE TABLE proy_pagos (
    id_pago INT,
    monto NUMERIC(10,2),
    estado VARCHAR(20)
);

CREATE TABLE proy_compras (
    id_compra INT,
    total NUMERIC(10,2),
    estado VARCHAR(20)
);