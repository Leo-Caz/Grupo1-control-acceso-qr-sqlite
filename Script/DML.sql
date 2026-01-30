-- database: ../DataBase/Usuario.sqlite

INSERT INTO CatalogoTipo
    (Nombre, Descripcion) VALUES 
    ('Tipo Persona','Tipos estudiante, rector, ...'), 
    ('Sexo', 'Masculino, Femenino'),
    ('Estado Civil', 'Soltero, Casado, ...');

INSERT INTO Catalogo
( IdCatalogoTipo        ,Nombre         ,Descripcion) VALUES
(   1                   ,'Estudiante'   ,'rol en la universidad'), -- ID 1
(   1                   ,'Docente'      ,'rol en la universidad'), -- ID 2
(   1                   ,'Ingeniero'    ,'rol en la universidad'), -- ID 3
(   1                   ,'Decano'       ,'rol en la universidad'), -- ID 4
(   1                   ,'Coordinador'  ,'rol en la universidad'), -- ID 5
(   1                   ,'Personal'     ,'rol en la universidad'), -- ID 6

(   2                   ,'Masculino'    ,'tipos de sexualidad'),    -- ID 7
(   2                   ,'Femenino'     ,'tipos de sexualidad'),    -- ID 8
(   2                   ,'Hibrido'      ,'tipos de sexualidad'),    -- ID 9
(   2                   ,'Asexual'      ,'tipos de sexualidad'),    -- ID 10

(   3                   ,'Soltero'      ,'tipos de estado civil'),  -- ID 11
(   3                   ,'Casado'       ,'tipos de estado civil'),  -- ID 12
(   3                   ,'Divorciado'   ,'tipos de estado civil'),  -- ID 13
(   3                   ,'Viudo'        ,'tipos de estado civil');          -- ID 18

-- INSERT definitivo sin la columna IdCatalogoRaza
INSERT INTO Usuario
(IdCatalogoTipoUsuario, IdCatalogoSexo, IdCatalogoEstadoCivil, 
 Cedula, PrimerNombre, SegundoNombre, PrimerApellido, SegundoApellido, Foto)
VALUES
(1, 7, 11, '1234567890', 'Juan', 'Carlos', 'Perez', 'Lopez', 'juan.jpg'),
(2, 8, 13, '9876543210', 'Maria','Elena',  'Gonzalez','Vera','maria.jpg'),
(1, 7, 11, '1322132312', 'Leandro', NULL, 'Castro', NULL, 'leandro.jpg'),
(1, 7, 11, '1724738800', 'Alexander', NULL, 'Cando', NULL, 'alexander.jpg'),
(1, 7, 11, '1753295920', 'Nicolas', 'Alejandro', 'Bohorquez', 'Grijalva', 'nicolas.jpg'),
(1, 8, 11, '1727341255', 'Daniela', NULL, 'Caiza', NULL, 'daniela.jpg'),
(1, 8, 11, '1150545414', 'Zoe', NULL, 'Cartuche', NULL, 'zoe.jpg'),
(3, 7, 11, '0923456789', 'Patricio', NULL, 'Paccha', NULL, 'patricio.jpg');


INSERT INTO Carnet (IdUsuario, CodigoQR) VALUES
(1, '550e8400-e29b-41d4-a716-446655440000'), -- Juan Perez
(2, '6ba7b810-9dad-11d1-80b4-00c04fd430c8'), -- Maria Gonzalez
(3, 'f47ac10b-58cc-4372-a567-0e02b2c3d479'), -- Leandro Castro
(4, 'ad300451-246e-473c-95a2-97217578351b'), -- Alexander Cando
(5, '5d8c3605-f9a1-42ec-8228-444c13038a8f'), -- Nicolas Bohorquez
(6, '0a614838-5184-4820-9189-9b935564858b'), -- Daniela Caiza
(7, '2f4a4605-873b-4891-995c-7d9d5543666d'), -- Zoe Cartuche
(8, 'b07f87a8-140b-4228-8d9e-1051564789d2'); -- Patricio Paccha