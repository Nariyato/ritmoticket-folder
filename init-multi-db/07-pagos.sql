-- =========================================
-- 07-pagos.sql
-- =========================================

-- Conectarse a la base de datos específica para este microservicio
\c pagos

-- 1. ELIMINACIÓN (Orden jerárquico inverso para evitar conflictos de FK)
DROP TABLE IF EXISTS proy_usuarios;
DROP TABLE IF EXISTS proy_compras;
DROP TABLE IF EXISTS reembolsos;
DROP TABLE IF EXISTS transacciones;
DROP TABLE IF EXISTS pagos;

-- 2. CREACIÓN DE TABLAS MAESTRAS [
CREATE TABLE pagos (
    id_pago SERIAL PRIMARY KEY,
    monto NUMERIC(10,2),
    metodo VARCHAR(50), -- WebPay, PayPal, Transferencia
    fecha_pago DATE,
    estado VARCHAR(20) -- Aprobado, Pendiente, Fallido, Reembolsado
);

CREATE TABLE transacciones (
    id_transaccion SERIAL PRIMARY KEY,
    id_pago INT REFERENCES pagos(id_pago),
    codigo VARCHAR(50), -- Código de autorización bancaria
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

-- 3. CREACIÓN DE TABLAS DE PROYECCIÓN 
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

-- 4. INSERCIÓN DE DATOS (Poblamiento)

-- Poblamiento de Proyecciones (Sincronizado con Usuarios y Compras previos) 
INSERT INTO proy_usuarios (id_usuario, nombre, correo) VALUES
(7, 'Carlos Contreras', 'carlos@cliente.cl'),
(8, 'Camila Cervantes', 'camila@cliente.cl'),
(9, 'Cristian Castro', 'cristian@cliente.cl');

INSERT INTO proy_compras (id_compra, total, estado) VALUES
(1, 90000.00, 'Completada'),
(2, 150000.00, 'Completada'),
(3, 45000.00, 'Pendiente');

-- Registro de Pagos
INSERT INTO pagos (monto, metodo, fecha_pago, estado) VALUES
(90000.00, 'WebPay', '2025-05-15', 'Aprobado'),   -- ID 1
(150000.00, 'PayPal', '2025-05-16', 'Aprobado'),  -- ID 2
(45000.00, 'Transferencia', '2025-05-18', 'Pendiente'), -- ID 3
(25000.00, 'WebPay', '2025-05-10', 'Reembolsado'); -- ID 4 (Anterior)

-- Registro de Transacciones Bancarias
INSERT INTO transacciones (id_pago, codigo, banco, estado) VALUES
(1, 'AUTH-WP-12345', 'Banco Santander', 'Exitosa'),
(2, 'PAYPAL-ID-999', 'PayPal Service', 'Exitosa'),
(3, 'TRF-BK-777', 'Banco Estado', 'En Proceso'),
(4, 'AUTH-WP-11111', 'Banco de Chile', 'Anulada');

-- Registro de Reembolsos
INSERT INTO reembolsos (id_pago, monto, fecha, motivo) VALUES
(4, 25000.00, '2025-05-12', 'Evento cancelado por el artista');