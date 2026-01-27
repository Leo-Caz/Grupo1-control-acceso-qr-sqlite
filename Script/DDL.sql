-- database: ../DataBase/Usuario.sqlite
PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS Registro;
DROP TABLE IF EXISTS Carnet;
DROP TABLE IF EXISTS Persona;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Catalogo;
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
    IdCatalogoTipoUsuario   INTEGER NOT NULL,
    IdCatalogoSexo          INTEGER NOT NULL,
    IdCatalogoEstadoCivil   INTEGER NOT NULL,
    Cedula                  TEXT NOT NULL UNIQUE,
    PrimerNombre            TEXT NOT NULL,
    SegundoNombre           TEXT,
    PrimerApellido          TEXT NOT NULL,
    SegundoApellido         TEXT,
    Foto                    TEXT,
    Estado                  TEXT NOT NULL DEFAULT 'A',
    FechaCreacion           DATETIME DEFAULT CURRENT_TIMESTAMP,
    FechaModificacion       DATETIME,
    
    FOREIGN KEY (IdCatalogoTipoUsuario) REFERENCES Catalogo(IdCatalogo),
    FOREIGN KEY (IdCatalogoSexo)        REFERENCES Catalogo(IdCatalogo),
    FOREIGN KEY (IdCatalogoEstadoCivil) REFERENCES Catalogo(IdCatalogo)
);


CREATE TABLE Carnet (
    IdCarnet          INTEGER PRIMARY KEY AUTOINCREMENT,
    IdUsuario         INTEGER NOT NULL REFERENCES Usuario(IdUsuario),
    CodigoQR          VARCHAR(75) NOT NULL UNIQUE,

    Estado            VARCHAR(1) NOT NULL DEFAULT ('A'),
    FechaCreacion     DATETIME DEFAULT(datetime('now','localtime')),
    FechaModificacion DATETIME
);

CREATE TABLE Registro (
    IdRegistro      INTEGER PRIMARY KEY AUTOINCREMENT,
    IdUsuario       INTEGER NOT NULL REFERENCES Usuario(IdUsuario),
    IdCarnet        INTEGER NOT NULL REFERENCES Carnet(IdCarnet),
    FechaEntrada    DATETIME DEFAULT(datetime('now','localtime')),

    Estado VARCHAR(1) NOT NULL DEFAULT ('A')
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_carnet_usuario
ON Carnet(IdUsuario);