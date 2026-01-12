-- database: ../DataBase/Usuario.sqlite
PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS Registro;
DROP TABLE IF EXISTS Carnet;
DROP TABLE IF EXISTS Persona;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS CatalogoTipo;

CREATE TABLE CatalogoTipo(
    IdCatalogoTipo  INTEGER PRIMARY KEY AUTOINCREMENT,
    Nombre          VARCHAR(20) NOT NULL UNIQUE,
    Descripcion     VARCHAR(90),

    Estado VARCHAR(1) NOT NULL DEFAULT ('A'),
    FechaCreacion DATETIME DEFAULT(datetime('now','localtime')),
    FechaModificacion DATETIME
);

CREATE TABLE Catalogo(
    IdCatalogo      INTEGER PRIMARY KEY AUTOINCREMENT,
    IdCatalogoTipo  INTEGER NOT NULL REFERENCES CatalogoTipo(IdCatalogoTipo),
    Nombre          VARCHAR(50) NOT NULL,
    Descripcion     VARCHAR(90),

    Estado VARCHAR(1) NOT NULL DEFAULT ('A'),
    FechaCreacion DATETIME DEFAULT(datetime('now','localtime')),
    FechaModificacion DATETIME
);

CREATE TABLE Usuario (
    IdUsuario               INTEGER PRIMARY KEY AUTOINCREMENT,
    IdCatalogoTipoUsuario   INTEGER NOT NULL REFERENCES Catalogo(IdCatalogo),
    IdCatalogoSexo          INTEGER NOT NULL REFERENCES Catalogo(IdCatalogo),
    IdCatalogoEstadoCivil   INTEGER NOT NULL REFERENCES Catalogo(IdCatalogo),
    
    PrimerNombre            VARCHAR(15) NOT NULL,
    SegundoNombre           VARCHAR(15),
    PrimerApellido          VARCHAR(12) NOT NULL,
    SegundoApellido         VARCHAR(12) NOT NULL,
    Cedula                  VARCHAR(10) NOT NULL UNIQUE,
    IdCatalogoTipo          INTEGER NOT NULL REFERENCES CatalogoTipo(IdCatalogoTipo),
    Foto                    TEXT NOT NULL DEFAULT 'sin_foto.jpg',

    Estado VARCHAR(1) NOT NULL DEFAULT ('A'),
    FechaCreacion DATETIME DEFAULT(datetime('now','localtime')),
    FechaModificacion DATETIME
);


CREATE TABLE Carnet (
    IdCarnet    INTEGER PRIMARY KEY AUTOINCREMENT,
    IdUsuario   INTEGER REFERENCES Usuario(IdUsuario),
    CodigoQR    VARCHAR(75) NOT NULL UNIQUE,

    Estado VARCHAR(1) NOT NULL DEFAULT ('A'),
    FechaCreacion DATETIME DEFAULT(datetime('now','localtime')),
    FechaModificacion DATETIME
);

CREATE TABLE Registro (
    IdRegistro      INTEGER PRIMARY KEY AUTOINCREMENT,
    IdUsuario       INTEGER NOT NULL REFERENCES Usuario(IdUsuario),
    IdCarnet        INTEGER NOT NULL REFERENCES Carnet(IdCarnet),
    FechaEntrada    DATETIME DEFAULT(datetime('now','localtime')),

    Estado VARCHAR(1) NOT NULL DEFAULT ('A')
);