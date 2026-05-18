-- =========================================
-- 02-artistas.sql
-- =========================================

\c artistas

DROP TABLE IF EXISTS proy_precios;
DROP TABLE IF EXISTS proy_recintos;
DROP TABLE IF EXISTS eventos_artista;
DROP TABLE IF EXISTS albums;
DROP TABLE IF EXISTS artistas;

CREATE TABLE artistas (
    id_artista SERIAL PRIMARY KEY,
    nombre_artistico VARCHAR(100),
    pais VARCHAR(50),
    genero VARCHAR(50),
    estado VARCHAR(20)
);

CREATE TABLE albums (
    id_album SERIAL PRIMARY KEY,
    id_artista INT REFERENCES artistas(id_artista),
    titulo VARCHAR(100),
    fecha_lanzamiento DATE,
    tipo VARCHAR(50)
);

CREATE TABLE eventos_artista (
    id_evento SERIAL PRIMARY KEY,
    id_artista INT REFERENCES artistas(id_artista),
    nombre_evento VARCHAR(100),
    fecha DATE,
    ciudad VARCHAR(50)
);

CREATE TABLE proy_recintos (
    id_recinto INT,
    nombre VARCHAR(100),
    ciudad VARCHAR(50)
);

CREATE TABLE proy_precios (
    id_precio INT,
    valor NUMERIC(10,2),
    moneda VARCHAR(10)
);

INSERT INTO artistas(nombre_artistico,pais,genero,estado)
VALUES
('DJ Sonic','Chile','Electronica','activo'),
('RockFire','Argentina','Rock','activo');