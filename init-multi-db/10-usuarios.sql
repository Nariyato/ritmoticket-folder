-- =========================================
-- 10-usuarios.sql
-- =========================================

-- Conectarse a la base de datos específica para este microservicio
\c usuarios

-- 1. ELIMINACIÓN (Orden jerárquico inverso para evitar conflictos de FK)
DROP TABLE IF EXISTS proy_boletos;
DROP TABLE IF EXISTS credenciales_usuarios;
DROP TABLE IF EXISTS perfiles;
DROP TABLE IF EXISTS usuarios;

-- 2. CREACIÓN DE TABLAS MAESTRAS
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    apellido VARCHAR(150) NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL CHECK (rol IN ('Cliente', 'Administrador', 'Organizador')),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE perfiles (
    id SERIAL PRIMARY KEY,
    usuario_correo VARCHAR(150) UNIQUE NOT NULL REFERENCES usuarios(correo) ON DELETE CASCADE,
    telefono VARCHAR(30),
    direccion VARCHAR(180), 
    fecha_registro DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE credenciales_usuarios (
    id SERIAL PRIMARY KEY,
    usuario_correo VARCHAR(150) UNIQUE NOT NULL REFERENCES usuarios(correo) ON DELETE CASCADE,
    ultimo_acceso TIMESTAMP,
    bloqueado BOOLEAN NOT NULL DEFAULT FALSE,
    intentos_fallidos INT NOT NULL DEFAULT 0 CHECK (intentos_fallidos >= 0)
);

-- 3. CREACIÓN DE TABLAS DE PROYECCIÓN (Sincronización mínima local)
CREATE TABLE proy_boletos (
    id_boleto INT,
    id_evento INT, 
    id_zona INT, 
    codigo VARCHAR(50),
    estado VARCHAR(20)
);

-- 4. ÍNDICES ADICIONALES
CREATE INDEX idx_usuarios_rol ON usuarios(rol);
CREATE INDEX idx_usuarios_activo ON usuarios(activo);
CREATE INDEX idx_perfil_correo ON perfiles(usuario_correo);

-- 5. INSERCIÓN DE DATOS (Poblamiento)

-- Usuarios Base (Siguiendo la lógica de los ejemplos previos)
-- Se usan los IDs 7, 8 y 9 para mantener consistencia con los otros microservicios
-- La contraseña por defecto fue configurada como 'Ritmo@2026' para todos los usuarios, y está almacenada como 
-- hash (huella digital) utilizando el algoritmo BCrypt (generado con BCryptPasswordEncoder de Spring Security).
INSERT INTO usuarios (nombre, apellido, correo, password, rol) VALUES
('Ana',      'Aguilar',   'ana@administrador.cl',     '$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW',  'Administrador'),
('Andrés',   'Acosta',    'andres@administrador.cl',  '$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW',  'Administrador'),
('Adrián',   'Álvarez',   'adrian@administrador.cl',  '$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW',  'Administrador'),
('Beatriz',  'Bermúdez',  'beatriz@organizador.cl',      '$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW',  'Organizador'),
('Benito',   'Barrios',   'benito@organizador.cl',       '$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW',  'Organizador'),
('Belén',    'Bravo',     'belen@organizador.cl',        '$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW',  'Organizador'),
('Carlos',   'Contreras', 'carlos@cliente.cl',        '$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW', 'Cliente'),
('Camila',   'Cervantes', 'camila@cliente.cl',        '$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW',  'Cliente'),
('Cristian', 'Castro', 'cristian@cliente.cl',         '$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW',  'Cliente');
        -- "$2a$10$n5Ve7TJaQcPLrABIOQPMyefjFGeCBh3AX3yal1fTdE0QknQQmcUyW" es un hash de ejemplo para la contraseña 'Ritmo@2026' utilizando "https://bcrypt-generator.com" (Rounds/cost: 10).


-- Perfiles de Usuario
INSERT INTO perfiles (usuario_correo, telefono, direccion) VALUES
('ana@administrador.cl', '+56911111111', 'Calle Principal 123'),
('beatriz@organizador.cl', '+56922222222', 'Santiago Centro 456'),
('carlos@cliente.cl', '+56933333333', 'Paine 791'),
('camila@cliente.cl', NULL, 'Av. Secundaria 456'),
('cristian@cliente.cl', '+56944444444', 'Calle Tercera 789');

INSERT INTO credenciales_usuarios (usuario_correo, ultimo_acceso, bloqueado, intentos_fallidos) VALUES
('ana@administrador.cl',     NOW(), FALSE, 0),
('beatriz@organizador.cl',   NOW(), FALSE, 0),
('carlos@cliente.cl',        NOW(), FALSE, 1),
('camila@cliente.cl',        NULL,  FALSE, 0),
('cristian@cliente.cl',      NULL,  TRUE,  3);

-- Poblamiento de Proyecciones (Sincronizado con Boletos anteriores)
INSERT INTO proy_boletos (id_boleto, id_evento, id_zona, codigo, estado) VALUES
(1, 1, 2, 'TKT-LB-001', 'Vendido'),
(2, 1, 2, 'TKT-LB-002', 'Vendido'),
(3, 2, 2, 'TKT-DL-501', 'Reservado'),
(4, 2, 1, 'TKT-DL-502', 'Vendido');