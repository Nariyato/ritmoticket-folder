-- =========================================
-- 01-artistas.sql
-- =========================================

-- Conectarse a la base de datos específica
\c artistas;

-- 1. ELIMINACIÓN (Orden jerárquico inverso para evitar errores de FK)
DROP TABLE IF EXISTS eventos_artista; --lista informativa para el perfil del artista, no es el creador oficial del evento
DROP TABLE IF EXISTS albums;
DROP TABLE IF EXISTS artistas;

-- 2. CREACIÓN DE TABLAS MAESTRAS
CREATE TABLE artistas (
    id_artista SERIAL PRIMARY KEY,
    nombre_artistico VARCHAR(100),
    pais VARCHAR(50),
    genero VARCHAR(50),
    estado VARCHAR(20) -- Activo, En Gira, Retirado
);

CREATE TABLE albums (
    id_album SERIAL PRIMARY KEY,
    id_artista INT REFERENCES artistas(id_artista),
    titulo VARCHAR(100),
    fecha_lanzamiento DATE,
    tipo VARCHAR(50) -- Estudio, En Vivo, Recopilatorio
);

CREATE TABLE eventos_artista (
    id_evento SERIAL PRIMARY KEY,
    id_artista INT REFERENCES artistas(id_artista),
    nombre_evento VARCHAR(100),
    fecha DATE,
    ciudad VARCHAR(50)
);

-- 4. INSERCIÓN DE DATOS (Poblamiento)

-- Artistas
INSERT INTO artistas (nombre_artistico, pais, genero, estado) VALUES
('Los Bunkers', 'Chile', 'Rock Latino', 'En Gira'),    -- ID 1
('Dua Lipa', 'Reino Unido', 'Pop', 'Activo'),          -- ID 2
('Bad Bunny', 'Puerto Rico', 'Urbano', 'En Pausa'),    -- ID 3
('Mon Laferte', 'Chile', 'Indie/Pop', 'En Gira'),      -- ID 4
('Metallica', 'EE.UU.', 'Heavy Metal', 'Activo');      -- ID 5

-- Albums
INSERT INTO albums (id_artista, titulo, fecha_lanzamiento, tipo) VALUES
(1, 'Vida de Perros', '2025-09-08', 'Estudio'),
(2, 'Noviembre', '2023-11-01', 'Estudio'),
(3, 'Future Nostalgia', '2020-03-27', 'Estudio'),
(4, 'Un Verano Sin Ti', '2022-05-06', 'Estudio'),
(5, 'Autopoiética', '2023-11-10', 'Estudio');

-- Eventos de Artista (Próximos conciertos registrados en el perfil del artista)
INSERT INTO eventos_artista (id_artista, nombre_evento, fecha, ciudad) VALUES
(1, 'Gira Ven Aquí', '2025-12-15', 'Santiago'),
(2, 'Radical Optimism Tour', '2026-03-10', 'Santiago'),
(3, 'Autopoiética Tour', '2025-11-20', 'Concepcion'),
(4, 'M72 World Tour', '2026-01-25', 'Santiago');