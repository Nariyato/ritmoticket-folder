-- =========================================
-- 04-compras.sql
-- =========================================

-- Conectarse a la base de datos específica
\c compras; 

-- 1. ELIMINACIÓN (Orden jerárquico inverso para evitar conflictos de FK)
DROP TABLE IF EXISTS proy_boletos; 
DROP TABLE IF EXISTS proy_pagos; 
DROP TABLE IF EXISTS carritos; 
DROP TABLE IF EXISTS detalle_compras; 
DROP TABLE IF EXISTS compras; 

-- 2. CREACIÓN DE TABLAS MAESTRAS DEL MICROSERVICIO
CREATE TABLE compras (
    id_compra SERIAL PRIMARY KEY,
    id_usuario INT, -- Referencia al microservicio usuarios
    fecha DATE,
    total NUMERIC(10,2),
    estado VARCHAR(20) -- Completada, Pendiente, Cancelada
); 

CREATE TABLE detalle_compras (
    id_detalle SERIAL PRIMARY KEY,
    id_compra INT REFERENCES compras(id_compra),
    id_boleto INT, -- Referencia al microservicio boletos
    cantidad INT,
    subtotal NUMERIC(10,2)
); 

CREATE TABLE carritos (
    id_carrito SERIAL PRIMARY KEY,
    id_usuario INT,
    fecha_creacion DATE,
    estado VARCHAR(20), -- Activo, Convertido, Expirado
    total NUMERIC(10,2)
); 

-- 3. CREACIÓN DE TABLAS DE PROYECCIÓN (Sincronización mínima local)
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

-- 4. INSERCIÓN DE DATOS (Poblamiento)

-- Compras realizadas (Usuarios 7, 8 y 9 de los ejemplos anteriores)
INSERT INTO compras (id_usuario, fecha, total, estado) VALUES
(7, '2025-05-15', 90000.00, 'Completada'), -- ID 1: Carlos compró 2 canchas
(8, '2025-05-16', 150000.00, 'Completada'), -- ID 2: Camila compró 1 VIP
(9, '2025-05-18', 45000.00, 'Pendiente');   -- ID 3: Cristian tiene un pago en proceso

-- Detalle de las compras
INSERT INTO detalle_compras (id_compra, id_boleto, cantidad, subtotal) VALUES
(1, 1, 1, 45000.00), -- Ticket Los Bunkers
(1, 2, 1, 45000.00), -- Ticket Los Bunkers
(2, 4, 1, 150000.00), -- Ticket Dua Lipa VIP
(3, 3, 1, 45000.00); -- Ticket Dua Lipa General

-- Estado de los carritos actuales
INSERT INTO carritos (id_usuario, fecha_creacion, estado, total) VALUES
(7, '2025-05-14', 'Convertido', 90000.00),
(8, '2025-05-16', 'Convertido', 150000.00),
(9, '2025-05-18', 'Activo', 45000.00),
(7, '2025-05-20', 'Activo', 0.00); -- Nuevo carrito vacío para Carlos

-- Poblamiento de Proyecciones (Referencias cruzadas)
INSERT INTO proy_pagos (id_pago, monto, estado) VALUES
(501, 90000.00, 'Aprobado'),
(502, 150000.00, 'Aprobado'),
(503, 45000.00, 'Procesando');

INSERT INTO proy_boletos (id_boleto, codigo, estado) VALUES
(1, 'TKT-LB-001', 'Vendido'),
(2, 'TKT-LB-002', 'Vendido'),
(3, 'TKT-DL-501', 'Reservado'),
(4, 'TKT-DL-502', 'Vendido');