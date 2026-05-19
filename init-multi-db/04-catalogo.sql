-- =========================================
-- 04-catalogo.sql
-- =========================================

\c catalogo;

-- 1. ELIMINACIÓN (Orden jerárquico inverso) 
DROP TABLE IF EXISTS proy_recintos;
DROP TABLE IF EXISTS proy_artistas;
DROP TABLE IF EXISTS catalogo_eventos;
DROP TABLE IF EXISTS generos;
DROP TABLE IF EXISTS categorias;

-- 2. CREACIÓN DE TABLAS MAESTRAS 
CREATE TABLE categorias (
    id_categoria SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    descripcion VARCHAR(100),
    estado VARCHAR(20),
    fecha_creacion DATE
);

CREATE TABLE generos (
    id_genero SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    descripcion VARCHAR(100),
    popularidad INT,
    estado VARCHAR(20)
);

CREATE TABLE catalogo_eventos (
    id_catalogo SERIAL PRIMARY KEY,
    nombre_evento VARCHAR(100),
    categoria VARCHAR(50),
    fecha DATE,
    estado VARCHAR(20)
);

-- 3. CREACIÓN DE TABLAS DE PROYECCIÓN 
CREATE TABLE proy_artistas (
    id_artista INT,
    nombre_artistico VARCHAR(100),
    genero VARCHAR(50)
);

CREATE TABLE proy_recintos (
    id_recinto INT,
    nombre VARCHAR(100),
    ciudad VARCHAR(50)
);

-- 4. INSERCIÓN DE DATOS (Poblamiento)

-- Categorías de Eventos
INSERT INTO categorias (nombre, descripcion, estado, fecha_creacion) VALUES
('Masivos', 'Conciertos en estadios y recintos de gran capacidad', 'Activo', '2025-01-10'),
('Festivales', 'Eventos de larga duración con múltiples artistas', 'Activo', '2025-01-15'),
('Íntimos', 'Sesiones acústicas o presentaciones en teatros pequeños', 'Activo', '2025-02-01'),
('Streaming', 'Acceso digital para visualización online en vivo', 'Inactivo', '2025-03-20');

-- Géneros Musicales
INSERT INTO generos (nombre, descripcion, popularidad, estado) VALUES
('Rock Latino', 'Exponentes del rock en español y fusiones locales', 85, 'Activo'),
('Pop Internacional', 'Artistas de alcance global y hits radiales', 95, 'Activo'),
('Urbano', 'Reggaetón, Trap y ritmos de calle contemporáneos', 98, 'Activo'),
('Indie', 'Producciones independientes y alternativas', 60, 'Activo');

-- Catálogo de Eventos (La oferta disponible para el usuario)
INSERT INTO catalogo_eventos (nombre_evento, categoria, fecha, estado) VALUES
('Gira Ven Aquí - Los Bunkers', 'Masivos', '2025-12-15', 'Disponible'),
('Radical Optimism Tour - Dua Lipa', 'Masivos', '2026-03-10', 'Agotado'),
('Lollapalooza Chile 2026', 'Festivales', '2026-03-21', 'Preventa'),
('Autopoiética Tour - Mon Laferte', 'Íntimos', '2025-11-20', 'Disponible');

-- Poblamiento de Proyecciones (Referencias cruzadas con otros microservicios)
INSERT INTO proy_artistas (id_artista, nombre_artistico, genero) VALUES
(1, 'Los Bunkers', 'Rock Latino'),
(2, 'Dua Lipa', 'Pop Internacional'),
(4, 'Mon Laferte', 'Indie');

INSERT INTO proy_recintos (id_recinto, nombre, ciudad) VALUES
(101, 'Estadio Nacional', 'Santiago'),
(102, 'Movistar Arena', 'Santiago'),
(103, 'Teatro Biobío', 'Concepcion');