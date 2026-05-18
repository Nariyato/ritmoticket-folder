-- =========================================
-- 06-pagos.sql
-- =========================================

\c pagos

DROP TABLE IF EXISTS proy_usuarios;
DROP TABLE IF EXISTS proy_compras;
DROP TABLE IF EXISTS reembolsos;
DROP TABLE IF EXISTS transacciones;
DROP TABLE IF EXISTS pagos;

CREATE TABLE pagos (
    id_pago SERIAL PRIMARY KEY,
    monto NUMERIC(10,2),
    metodo VARCHAR(50),
    fecha_pago DATE,
    estado VARCHAR(20)
);

CREATE TABLE transacciones (
    id_transaccion SERIAL PRIMARY KEY,
    id_pago INT REFERENCES pagos(id_pago),
    codigo VARCHAR(50),
    banco VARCHAR(50),
    estado VARCHAR(20)
);

CREATE TABLE reembolsos (
    id_reembolso SERIAL PRIMARY KEY,
    id_pago INT REFERENCES pagos(id_pago),
    monto NUMERIC(10,2),
    fecha DATE,
    motivo VARCHAR(100)
);

CREATE TABLE proy_compras (
    id_compra INT,
    total NUMERIC(10,2),
    estado VARCHAR(20)
);

CREATE TABLE proy_usuarios (
    id_usuario INT,
    nombre VARCHAR(100),
    correo VARCHAR(100)
);