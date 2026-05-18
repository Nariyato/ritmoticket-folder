-- =========================================
-- 01-usuarios.sql
-- =========================================

\c usuarios

DROP TABLE IF EXISTS proy_boletos;
DROP TABLE IF EXISTS proy_compras;
DROP TABLE IF EXISTS direcciones;
DROP TABLE IF EXISTS perfiles;
DROP TABLE IF EXISTS usuarios;

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
    tipo_usuario VARCHAR(30),
    estado VARCHAR(20)
);

CREATE TABLE direcciones (
    id_direccion SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES usuarios(id_usuario),
    ciudad VARCHAR(50),
    calle VARCHAR(100),
    numero INT
);

CREATE TABLE proy_compras (
    id_compra INT,
    total NUMERIC(10,2),
    estado VARCHAR(20)
);

CREATE TABLE proy_boletos (
    id_boleto INT,
    codigo VARCHAR(50),
    estado VARCHAR(20)
);

CREATE INDEX idx_usuario_correo
ON usuarios(correo);

INSERT INTO usuarios(nombre,correo,telefono,fecha_registro)
VALUES
('Ana Torres','ana@gmail.com','999111222','2026-01-10'),
('Luis Rojas','luis@gmail.com','988777666','2026-02-15');

INSERT INTO perfiles(id_usuario,nickname,tipo_usuario,estado)
VALUES
(1,'anita','cliente','activo'),
(2,'lucho','admin','activo');

INSERT INTO direcciones(id_usuario,ciudad,calle,numero)
VALUES
(1,'Santiago','Alameda',123),
(2,'Valparaiso','Prat',456);