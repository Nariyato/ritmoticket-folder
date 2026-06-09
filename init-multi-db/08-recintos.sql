-- =========================================
-- 08-recintos.sql
-- =========================================

-- Conectarse a la base de datos específica para este microservicio
\c recintos

-- 1. ELIMINACIÓN (Orden jerárquico inverso para evitar conflictos de FK)
DROP TABLE IF EXISTS proy_eventos;
DROP TABLE IF EXISTS proy_artistas;
DROP TABLE IF EXISTS sectores;
DROP TABLE IF EXISTS escenarios;
DROP TABLE IF EXISTS recintos;

-- 2. CREACIÓN DE TABLAS MAESTRAS
CREATE TABLE recintos (
    id_recinto SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    ciudad VARCHAR(50),
    capacidad INT,
    estado VARCHAR(20) -- Operativo, En Remodelación, Cerrado
);

CREATE TABLE escenarios (
    id_escenario SERIAL PRIMARY KEY,
    id_recinto INT REFERENCES recintos(id_recinto),
    nombre VARCHAR(100), -- Escenario Principal, Escenario Acústico, etc.
    capacidad INT,
    tipo VARCHAR(50) -- Abierto, Techado, Anfiteatro
);

CREATE TABLE sectores (
    id_sector SERIAL PRIMARY KEY,
    id_escenario INT REFERENCES escenarios(id_escenario),
    nombre VARCHAR(50), -- Cancha, VIP, Platea, Galería
    capacidad INT,
    estado VARCHAR(20) -- Disponible, Bloqueado
);

-- 3. CREACIÓN DE TABLAS DE PROYECCIÓN (Sincronización mínima local)
CREATE TABLE proy_artistas (
    id_artista INT,
    nombre_artistico VARCHAR(100),
    genero VARCHAR(50)
);

CREATE TABLE proy_eventos (
    id_evento INT,
    nombre_evento VARCHAR(100),
    categoria VARCHAR(50)
);

-- 4. INSERCIÓN DE DATOS (Poblamiento)

-- Definición de Recintos (Venues)
INSERT INTO recintos (nombre, ciudad, capacidad, estado) VALUES
('Estadio Nacional', 'Santiago', 50000, 'Operativo'), -- ID 1
('Movistar Arena', 'Santiago', 15000, 'Operativo'),   -- ID 2
('Teatro Biobío', 'Concepción', 1200, 'Operativo'),   -- ID 3
('Espacio Riesco', 'Santiago', 10000, 'Operativo');    -- ID 4

-- Definición de Escenarios
INSERT INTO escenarios (id_recinto, nombre, capacidad, tipo) VALUES
(1, 'Cancha Central Principal', 45000, 'Abierto'),     -- Escenario ID 1
(2, 'Escenario Central 360', 15000, 'Techado'),        -- Escenario ID 2
(3, 'Sala Principal de Teatro', 1200, 'Techado'),      -- Escenario ID 3
(4, 'Pabellón de Ferias', 8000, 'Techado');            -- Escenario ID 4

-- Definición de Sectores (Distribución de capacidad por escenario)
INSERT INTO sectores (id_escenario, nombre, capacidad, estado) VALUES
(1, 'Cancha General', 25000, 'Disponible'),
(1, 'Galería Norte', 10000, 'Disponible'),
(1, 'VIP Diamond', 500, 'Disponible'),
(2, 'Platea Alta', 5000, 'Disponible'),
(2, 'Cancha VIP', 2000, 'Disponible'),
(3, 'Butacas Primer Piso', 600, 'Disponible');

-- Poblamiento de Proyecciones (Sincronizado con microservicios de Artistas y Catálogo)
INSERT INTO proy_artistas (id_artista, nombre_artistico, genero) VALUES
(1, 'Los Bunkers', 'Rock Latino'),
(2, 'Dua Lipa', 'Pop Internacional'),
(4, 'Mon Laferte', 'Indie');

INSERT INTO proy_eventos (id_evento, nombre_evento, categoria) VALUES
(1, 'Gira Ven Aquí - Los Bunkers', 'Masivos'),
(2, 'Radical Optimism Tour - Dua Lipa', 'Masivos'),
(4, 'Autopoiética Tour - Mon Laferte', 'Íntimos');