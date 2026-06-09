-- =========================================
-- 09-reportes.sql
-- =========================================

-- Conectarse a la base de datos específica para este microservicio
\c reportes 

-- 1. ELIMINACIÓN (Orden jerárquico inverso)
DROP TABLE IF EXISTS proy_compras; 
DROP TABLE IF EXISTS proy_pagos; 
DROP TABLE IF EXISTS auditorias; 
DROP TABLE IF EXISTS estadisticas; 
DROP TABLE IF EXISTS reportes; 

-- 2. CREACIÓN DE TABLAS MAESTRAS
CREATE TABLE reportes (
    id_reporte SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    fecha_generacion DATE,
    tipo VARCHAR(50), -- Financiero, Operativo, Inventario
    estado VARCHAR(20) -- Generado, Pendiente, Fallido
); 

CREATE TABLE estadisticas (
    id_estadistica SERIAL PRIMARY KEY,
    descripcion VARCHAR(100),
    valor INT,
    fecha DATE,
    categoria VARCHAR(50) -- Ventas, Usuarios, Eventos
); 

CREATE TABLE auditorias (
    id_auditoria SERIAL PRIMARY KEY,
    accion VARCHAR(100),
    usuario VARCHAR(50),
    fecha DATE,
    estado VARCHAR(20) -- OK, ERROR, ADVERTENCIA
); 

-- 3. CREACIÓN DE TABLAS DE PROYECCIÓN (Sincronización con Compras y Pagos)
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

-- 4. INSERCIÓN DE DATOS (Poblamiento)

-- Documentos de Reportes Generados
INSERT INTO reportes (nombre, fecha_generacion, tipo, estado) VALUES
('Ventas Mensuales - Mayo 2025', '2025-05-31', 'Financiero', 'Generado'),
('Asistencia Estimada: Los Bunkers', '2025-12-16', 'Operativo', 'Pendiente'),
('Resumen de Reembolsos por Cancelación', '2025-05-15', 'Financiero', 'Generado');

-- Métricas y Estadísticas de Negocio
INSERT INTO estadisticas (descripcion, valor, fecha, categoria) VALUES
('Total Tickets Vendidos - Mayo', 3, '2025-05-31', 'Ventas'),
('Usuarios Activos en la Plataforma', 9, '2025-05-19', 'Usuarios'),
('Eventos Programados Q4', 5, '2025-05-01', 'Eventos'),
('Tasa de Conversión de Carritos', 75, '2025-05-20', 'Ventas');

-- Registro de Auditoría (Seguridad y Trazabilidad)
INSERT INTO auditorias (accion, usuario, fecha, estado) VALUES
('LOGIN_SUCCESS', 'carlos@cliente.cl', '2025-05-15', 'OK'),
('PURCHASE_COMPLETED', 'camila@cliente.cl', '2025-05-16', 'OK'),
('PAYMENT_GATEWAY_ERROR', 'cristian@cliente.cl', '2025-05-18', 'ERROR'),
('SENSITIVE_DATA_EXPORT', 'admin@ritmoticket.cl', '2025-05-20', 'ADVERTENCIA');

-- Poblamiento de Proyecciones (Consistente con Compras e IDs previos)
INSERT INTO proy_pagos (id_pago, monto, estado) VALUES
(1, 90000.00, 'Aprobado'),
(2, 150000.00, 'Aprobado'),
(3, 45000.00, 'Pendiente');

INSERT INTO proy_compras (id_compra, total, estado) VALUES
(1, 90000.00, 'Completada'),
(2, 150000.00, 'Completada'),
(3, 45000.00, 'Pendiente');