-- =========================================
-- 05-notificaciones.sql
-- =========================================

\c notificaciones

-- 1. ELIMINACIÓN (Orden jerárquico inverso)
DROP TABLE IF EXISTS proy_compras;
DROP TABLE IF EXISTS proy_usuarios;
DROP TABLE IF EXISTS sms;
DROP TABLE IF EXISTS correos;
DROP TABLE IF EXISTS notificaciones;

-- 2. CREACIÓN DE TABLAS MAESTRAS
CREATE TABLE notificaciones (
    id_notificacion SERIAL PRIMARY KEY,
    mensaje VARCHAR(200),
    tipo VARCHAR(50), -- Confirmación, Recordatorio, Alerta de Pago
    fecha_envio DATE,
    estado VARCHAR(20) -- Enviado, Pendiente, Fallido
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

-- 3. CREACIÓN DE TABLAS DE PROYECCIÓN (Sincronización con otros microservicios)
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

-- 4. INSERCIÓN DE DATOS (Poblamiento)

-- Poblamiento de Proyecciones (Sincronizado con microservicios de Usuarios y Compras)
INSERT INTO proy_usuarios (id_usuario, nombre, correo) VALUES
(7, 'Carlos Contreras', 'carlos@cliente.cl'),
(8, 'Camila Cervantes', 'camila@cliente.cl'),
(9, 'Cristian Castro', 'cristian@cliente.cl');

INSERT INTO proy_compras (id_compra, total, estado) VALUES
(1, 90000.00, 'Completada'),
(2, 150000.00, 'Completada'),
(3, 45000.00, 'Pendiente');

-- Notificaciones Generales
INSERT INTO notificaciones (mensaje, tipo, fecha_envio, estado) VALUES
('Su compra para Los Bunkers ha sido confirmada con éxito.', 'Confirmación', '2025-05-15', 'Enviado'),
('Recordatorio: Su concierto de Dua Lipa es en 48 horas.', 'Recordatorio', '2026-03-08', 'Pendiente'),
('Atención: El pago de su entrada para Lollapalooza sigue pendiente.', 'Alerta de Pago', '2025-05-18', 'Enviado');

-- Historial de Correos Electrónicos
INSERT INTO correos (destinatario, asunto, fecha, estado) VALUES
('carlos@cliente.cl', 'Confirmación de Compra #1 - RitmoTicket', '2025-05-15', 'Entregado'),
('camila@cliente.cl', 'Tu E-Ticket para Dua Lipa ya está disponible', '2025-05-16', 'Entregado'),
('cristian@cliente.cl', 'Problema con el pago de su reserva', '2025-05-18', 'Enviado');

-- Historial de SMS (Mensajería rápida)
INSERT INTO sms (telefono, mensaje, fecha, estado) VALUES
('+56912345678', 'RT-Ticket: Carlos, tu código para Los Bunkers es TKT-LB-001', '2025-05-15', 'Enviado'),
('+56987654321', 'RT-Ticket: Camila, disfruta el show de Dua Lipa!', '2026-03-10', 'Pendiente'),
('+56955554433', 'RT-Ticket: Cristian, tu reserva expira en 2 horas.', '2025-05-18', 'Enviado');