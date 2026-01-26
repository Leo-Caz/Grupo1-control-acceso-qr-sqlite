-- database: ../DataBase/Usuario.sqlite
INSERT INTO CatalogoTipo
    (Nombre, Descripcion) VALUES ('Tipo Persona','Tipos estudiante, rector, ...'), 
    ('Sexo', 'Masculino, Femenino'),
    ('Estado Civil', 'Soltero, Casado, ...'),
    ('Raza', 'Blanco, Negro, ...');

INSERT INTO Catalogo
( IdCatalogoTipo        ,Nombre         ,Descripcion) VALUES
(   1                   ,'Estudiante'   ,'rol en la universidad'), --1
(   1                   ,'Docente'      ,'rol en la universidad'),--2
(   1                   ,'Ingeniero'    ,'rol en la universidad'),--3
(   1                   ,'Decano'       ,'rol en la universidad'),--4
(   1                   ,'Coordinador'  ,'rol en la universidad'),--5
(   1                   ,'Personal'     ,'rol en la universidad'),--6

(   2                   ,'Masculino'    ,'tipos de sexualidad'), --7
(   2                   ,'Femenino'     ,'tipos de sexualidad'), --8
(   2                   ,'Hibrido'      ,'tipos de sexualidad'), --9
(   2                   ,'Asexual'      ,'tipos de sexualidad'),

(   3                   ,'Soltero'      ,'tipos de estado civil'),
(   3                   ,'Casado'       ,'tipos de estado civil'),
(   3                   ,'Divorciado'   ,'tipos de estado civil'),
(   3                   ,'Viudo'        ,'tipos de estado civil'),

(   4                   ,'Negro'        ,'tipos de raza'),
(   4                   ,'Blanco'       ,'tipos de raza'),
(   4                   ,'Mestizo'      ,'tipos de raza'),
(   4                   ,'Indigena'     ,'tipos de raza');

-- para probar estos datos
INSERT INTO Usuario
(IdCatalogoTipoUsuario, IdCatalogoSexo, IdCatalogoEstadoCivil, IdCatalogoRaza,
 Cedula, PrimerNombre, SegundoNombre, PrimerApellido, SegundoApellido, Foto)
VALUES
(1, 7, 11, 17, '1234567890', 'Juan', 'Carlos', 'Perez', 'Lopez', 'juan.jpg'),
(2, 8, 13, 16, '9876543210', 'Maria','Elena',  'Gonzalez','Vera','maria.jpg');



-- Para probar estos datos
INSERT INTO Carnet (IdUsuario, CodigoQR) VALUES
(1, 'QR-JUAN-001'),
(2, 'QR-MARIA-002');