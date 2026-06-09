-- =========================================
-- 10-usuarios.sql
-- =========================================

-- Conectarse a la base de datos específica para este microservicio
\c usuarios

-- 1. ELIMINACIÓN (Orden jerárquico inverso para evitar conflictos de FK)
DROP TABLE IF EXISTS proy_boletos;
DROP TABLE IF EXISTS proy_compras;
DROP TABLE IF EXISTS direcciones;
DROP TABLE IF EXISTS perfiles;
DROP TABLE IF EXISTS usuarios;

-- 2. CREACIÓN DE TABLAS MAESTRAS
CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    fecha_registro DATE
);

CREATE TABLE perfiles (
    id_perfil SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES usuarios(id_usuario),
    nickname VARCHAR(50),
    tipo_usuario VARCHAR(30), -- Cliente, Administrador, Promotor
    estado VARCHAR(20) -- Activo, Suspendido, Inactivo
);

CREATE TABLE direcciones (
    id_direccion SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES usuarios(id_usuario),
    ciudad VARCHAR(50),
    calle VARCHAR(100),
    numero INT
);

-- 3. CREACIÓN DE TABLAS DE PROYECCIÓN (Sincronización mínima local)
CREATE TABLE proy_compras (
    id_compra INT,
    total NUMERIC(10,2),
    estado VARCHAR(20)
);

CREATE TABLE proy_boletos (
    id_boleto INT,
    id_evento INT, 
    id_zona INT, 
    codigo VARCHAR(50),
    estado VARCHAR(20)
);

-- 4. ÍNDICES ADICIONALES
CREATE INDEX idx_usuario_correo ON usuarios(correo);

-- 5. INSERCIÓN DE DATOS (Poblamiento)

-- Usuarios Base (Siguiendo la lógica de los ejemplos previos)
-- Se usan los IDs 7, 8 y 9 para mantener consistencia con los otros microservicios
INSERT INTO usuarios (id_usuario, nombre, correo, telefono, fecha_registro) VALUES
(7, 'Carlos Contreras', 'carlos@cliente.cl', '+56911112222', '2025-01-15'),
(8, 'Camila Cervantes', 'camila@cliente.cl', '+56933334444', '2025-02-10'),
(9, 'Cristian Castro', 'cristian@cliente.cl', '+56955556666', '2025-03-05');

-- Forzar el reinicio de la secuencia del serial después de insertar IDs manuales
SELECT setval('usuarios_id_usuario_seq', (SELECT MAX(id_usuario) FROM usuarios));

-- Perfiles de Usuario
INSERT INTO perfiles (id_usuario, nickname, tipo_usuario, estado) VALUES
(7, 'CarlosC', 'Cliente VIP', 'Activo'),
(8, 'CamiMusic', 'Cliente', 'Activo'),
(9, 'CrisTickets', 'Cliente', 'Activo');

-- Direcciones de Despacho (Para envío de boletos físicos si aplica)
INSERT INTO direcciones (id_usuario, ciudad, calle, numero) VALUES
(7, 'Santiago', 'Av. Libertador', 1000),
(8, 'Concepción', 'Calle O''Higgins', 450),
(9, 'Valparaíso', 'Av. Pedro Montt', 12);

-- Poblamiento de Proyecciones (Sincronizado con Compras y Boletos anteriores)
INSERT INTO proy_compras (id_compra, total, estado) VALUES
(1, 90000.00, 'Completada'),
(2, 150000.00, 'Completada'),
(3, 45000.00, 'Pendiente');

INSERT INTO proy_boletos (id_boleto, id_evento, id_zona, codigo, estado) VALUES
(1, 1, 1, 'TKT-LB-001', 'Vendido'),
(3, 1, 3, 'TKT-DL-501', 'Reservado'),
(4, 1, 4, 'TKT-DL-502', 'Vendido');