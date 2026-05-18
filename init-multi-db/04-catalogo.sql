-- =========================================
-- 03-catalogo.sql
-- =========================================

\c catalogo

DROP TABLE IF EXISTS proy_recintos;
DROP TABLE IF EXISTS proy_artistas;
DROP TABLE IF EXISTS catalogo_eventos;
DROP TABLE IF EXISTS generos;
DROP TABLE IF EXISTS categorias;

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