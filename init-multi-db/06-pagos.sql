-- =========================================
-- 06-pagos.sql
-- =========================================

-- Conectarse a la base de datos específica para este microservicio
\c pagos

-- 1. ELIMINACIÓN (Orden jerárquico inverso para evitar conflictos de FK)
DROP TABLE IF EXISTS proy_compras;
DROP TABLE IF EXISTS reembolsos;
DROP TABLE IF EXISTS transacciones;
DROP TABLE IF EXISTS pagos;

-- 2. CREACIÓN DE TABLAS MAESTRAS [
CREATE TABLE pagos (
    id_pago SERIAL PRIMARY KEY,
    id_compra INT,
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

-- 4. INSERCIÓN DE DATOS (Poblamiento)

-- Poblamiento de Proyecciones (Sincronizado con Compras previas) 
INSERT INTO proy_compras (id_compra, total, estado) VALUES
(1, 90000.00, 'COMPLETADA'),
(2, 150000.00, 'COMPLETADA'),
(3, 45000.00, 'PENDIENTE');

-- Registro de Pagos (id_compra referencia lógica a ms-compras, compras 1-3)
INSERT INTO pagos (id_compra, monto, metodo, fecha_pago, estado) VALUES
(1, 90000.00, 'WEBPAY', '2025-05-15', 'APROBADO'),        -- Pago compra 1 (Carlos)
(2, 150000.00, 'PAYPAL', '2025-05-16', 'APROBADO'),        -- Pago compra 2 (Camila)
(3, 45000.00, 'TRANSFERENCIA', '2025-05-18', 'PENDIENTE'), -- Pago compra 3 (Cristian)
(NULL, 25000.00, 'WEBPAY', '2025-05-10', 'REEMBOLSADO');  -- Pago histórico sin compra vinculada

-- Registro de Transacciones Bancarias
INSERT INTO transacciones (id_pago, codigo, banco, estado) VALUES
(1, 'AUTH-WP-12345', 'Banco Santander', 'Exitosa'),
(2, 'PAYPAL-ID-999', 'PayPal Service', 'Exitosa'),
(3, 'TRF-BK-777', 'Banco Estado', 'En Proceso'),
(4, 'AUTH-WP-11111', 'Banco de Chile', 'Anulada');

-- Registro de Reembolsos
INSERT INTO reembolsos (id_pago, monto, fecha, motivo) VALUES
(4, 25000.00, '2025-05-12', 'Evento cancelado por el artista');