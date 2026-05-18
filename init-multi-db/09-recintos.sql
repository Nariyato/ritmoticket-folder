-- =========================================
-- 08-recintos.sql
-- =========================================

\c recintos

DROP TABLE IF EXISTS proy_catalogo;
DROP TABLE IF EXISTS proy_artistas;
DROP TABLE IF EXISTS sectores;
DROP TABLE IF EXISTS escenarios;
DROP TABLE IF EXISTS recintos;

CREATE TABLE recintos (
    id_recinto SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    ciudad VARCHAR(50),
    capacidad INT,
    estado VARCHAR(20)
);

CREATE TABLE escenarios (
    id_escenario SERIAL PRIMARY KEY,
    id_recinto INT REFERENCES recintos(id_recinto),
    nombre VARCHAR(100),
    capacidad INT,
    tipo VARCHAR(50)
);

CREATE TABLE sectores (
    id_sector SERIAL PRIMARY KEY,
    id_escenario INT REFERENCES escenarios(id_escenario),
    nombre VARCHAR(50),
    capacidad INT,
    estado VARCHAR(20)
);

CREATE TABLE proy_artistas (
    id_artista INT,
    nombre_artistico VARCHAR(100),
    genero VARCHAR(50)
);

CREATE TABLE proy_catalogo (
    id_catalogo INT,
    nombre_evento VARCHAR(100),
    categoria VARCHAR(50)
);