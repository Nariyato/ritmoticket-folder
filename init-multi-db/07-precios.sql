-- =========================================
-- 07-precios.sql
-- =========================================

-- Conectarse a la base de datos específica para este microservicio
\c precios

-- 1. ELIMINACIÓN (Orden jerárquico inverso para evitar conflictos de FK) 
DROP TABLE IF EXISTS proy_boletos;
DROP TABLE IF EXISTS proy_eventos;
DROP TABLE IF EXISTS promociones;
DROP TABLE IF EXISTS descuentos;
DROP TABLE IF EXISTS precios;

-- 2. CREACIÓN DE TABLAS MAESTRAS 
CREATE TABLE precios (
    id_precio SERIAL PRIMARY KEY,
    tipo_boleto VARCHAR(50), -- VIP, Cancha, Platea, etc.
    valor NUMERIC(10,2),
    moneda VARCHAR(10),
    estado VARCHAR(20) -- Activo, Obsoleto
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
    codigo VARCHAR(50), -- Código de cupón
    estado VARCHAR(20), -- Activa, Expirada
    expiracion DATE
);

-- 3. CREACIÓN DE TABLAS DE PROYECCIÓN 
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

-- 4. INSERCIÓN DE DATOS (Poblamiento)

-- Definición de Precios Estándar (Consistente con microservicios anteriores)
INSERT INTO precios (tipo_boleto, valor, moneda, estado) VALUES
('VIP Experience', 150000.00, 'CLP', 'Activo'),
('Platea Alta', 85000.00, 'CLP', 'Activo'),
('Cancha General', 45000.00, 'CLP', 'Activo'),
('Galería', 25000.00, 'CLP', 'Activo');

-- Configuración de Descuentos
INSERT INTO descuentos (nombre, porcentaje, fecha_inicio, fecha_fin) VALUES
('Early Bird (Venta Anticipada)', 20, '2025-01-01', '2025-03-01'),
('Descuento Estudiante', 15, '2025-01-01', '2025-12-31'),
('Preventa Fan Club', 10, '2025-04-10', '2025-04-15');

-- Promociones y Cupones
INSERT INTO promociones (descripcion, codigo, estado, expiracion) VALUES
('2x1 CyberDay Ritmo', 'CYBER2X1', 'Activa', '2025-06-01'),
('Descuento Primera Compra', 'BIENVENIDORT', 'Activa', '2025-12-31'),
('Promo Lanzo Álbum Los Bunkers', 'VIDADEPERROS', 'Expirada', '2025-05-01');

-- Poblamiento de Proyecciones (Sincronizado con microservicios de Boletos y Catálogo)
INSERT INTO proy_eventos (id_evento, nombre_evento, fecha) VALUES
(1, 'Gira Ven Aquí - Los Bunkers', '2025-12-15'),
(2, 'Radical Optimism Tour - Dua Lipa', '2026-03-10');

INSERT INTO proy_boletos (id_boleto, codigo, estado) VALUES
(1, 'TKT-LB-001', 'Vendido'),
(3, 'TKT-DL-501', 'Reservado'),
(4, 'TKT-DL-502', 'Vendido');