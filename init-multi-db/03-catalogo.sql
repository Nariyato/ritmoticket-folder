-- =========================================
-- 03-catalogo.sql
-- =========================================

\c catalogo;

-- 1. ELIMINACIÓN (Orden jerárquico inverso) 
DROP TABLE IF EXISTS eventos; --cambiado de catalogo_eventos a eventos
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

CREATE TABLE eventos (              --cambiado de catalogo_eventos a eventos
    id_evento SERIAL PRIMARY KEY,
    nombre_evento VARCHAR(100),
    categoria VARCHAR(50),
    fecha DATE,
    estado VARCHAR(20),
    id_artista INT,
    id_recinto INT
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
-- id_artista → artistas.id_artista (ms-artistas) | id_recinto → recintos.id_recinto (ms-recintos)
INSERT INTO eventos (nombre_evento, categoria, fecha, estado, id_artista, id_recinto) VALUES
('Gira Ven Aquí - Los Bunkers', 'Masivos', '2025-12-15', 'Disponible', 1, 1),   -- Estadio Nacional
('Radical Optimism Tour - Dua Lipa', 'Masivos', '2026-03-10', 'Agotado', 2, 2), -- Movistar Arena
('Lollapalooza Chile 2026', 'Festivales', '2026-03-21', 'Preventa', 5, 4),       -- Espacio Riesco
('Autopoiética Tour - Mon Laferte', 'Íntimos', '2025-11-20', 'Disponible', 4, 3); -- Teatro Biobío