-- BD
CREATE DATABASE DB_VETERINARIA
/*!40100 DEFAULT CHARACTER 
 * SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ 
/*!80016 DEFAULT ENCRYPTION='N' */;

-- Tabla para el personal de la clinica
CREATE TABLE tbl_usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    rol VARCHAR(20) NOT NULL,
    estado TINYINT(1) DEFAULT 1 -- 1 para Activo, 0 para Inactivo
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla para los dueños de las mascotas
CREATE TABLE tbl_clientes (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni CHAR(8) NOT NULL UNIQUE,
    telefono VARCHAR(9),
    email VARCHAR(100),
    direccion VARCHAR(255),
    fecha_registro DATE DEFAULT (CURDATE())
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla para los pacientes (mascotas)
CREATE TABLE tbl_pacientes (
    id_paciente INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    id_cliente INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(50),
    fecha_nacimiento DATE,
    sexo ENUM('M', 'H') NOT NULL, -- 'M' para Macho, 'H' para Hembra
    FOREIGN KEY (id_cliente) REFERENCES tbl_clientes(id_cliente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla para los veterinarios
CREATE TABLE tbl_veterinarios (
    id_veterinario INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100),
    colegiatura VARCHAR(20) UNIQUE,
    estado TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla para los servicios que ofrece la clínica
CREATE TABLE tbl_servicios (
    id_servicio INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre_servicio VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de citas
CREATE TABLE tbl_citas (
    id_cita INT PRIMARY KEY AUTO_INCREMENT,
    id_paciente INT NOT NULL,
    id_veterinario INT NOT NULL,
    id_servicio INT NOT NULL,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    estado VARCHAR(20) NOT NULL,
    observaciones TEXT,
    diagnostico TEXT,
    UNIQUE KEY idx_unica_cita (id_veterinario, fecha_cita, hora_cita), -- Restriccion para evitar doble reserva
    FOREIGN KEY (id_paciente) REFERENCES tbl_pacientes(id_paciente),
    FOREIGN KEY (id_veterinario) REFERENCES tbl_veterinarios(id_veterinario),
    FOREIGN KEY (id_servicio) REFERENCES tbl_servicios(id_servicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- Procedimiendos Almacenados Para Busquedas
CREATE PROCEDURE usp_Buscar_Usuario(IN texto VARCHAR(100))
BEGIN
    SELECT 
        * 
    FROM 
        tbl_usuarios
    WHERE 
        codigo LIKE CONCAT('%', texto, '%')
        OR nombre LIKE CONCAT('%', texto, '%')
        OR apellido LIKE CONCAT('%', texto, '%')
        OR email LIKE CONCAT('%', texto, '%');
END

CREATE PROCEDURE usp_Buscar_Cliente(IN texto VARCHAR(100))
BEGIN
    SELECT 
        * 
    FROM 
        tbl_clientes
    WHERE 
        codigo LIKE CONCAT('%', texto, '%')
        OR nombres LIKE CONCAT('%', texto, '%')
        OR apellidos LIKE CONCAT('%', texto, '%')
        OR dni LIKE CONCAT('%', texto, '%')
        OR email LIKE CONCAT('%', texto, '%');
END

CREATE PROCEDURE usp_Buscar_Paciente(IN texto VARCHAR(100))
BEGIN
    SELECT 
        p.*,
        CONCAT(c.nombres, ' ', c.apellidos) AS nombreCliente
    FROM 
        tbl_pacientes p
    LEFT JOIN 
        tbl_clientes c ON p.id_cliente = c.id_cliente
    WHERE 
        p.codigo LIKE CONCAT('%', texto, '%')
        OR p.nombre LIKE CONCAT('%', texto, '%')
        OR c.nombres LIKE CONCAT('%', texto, '%')
        OR c.apellidos LIKE CONCAT('%', texto, '%');
END

CREATE PROCEDURE usp_Buscar_Veterinario(IN texto VARCHAR(100))
BEGIN
    SELECT 
        *
    FROM 
        tbl_veterinarios
    WHERE 
        codigo LIKE CONCAT('%', texto, '%')
        OR nombres LIKE CONCAT('%', texto, '%')
        OR apellidos LIKE CONCAT('%', texto, '%')
        OR colegiatura LIKE CONCAT('%', texto, '%');
END

CREATE PROCEDURE usp_Buscar_Servicio(IN texto VARCHAR(100))
BEGIN
    SELECT 
        *
    FROM 
        tbl_servicios
    WHERE 
        codigo LIKE CONCAT('%', texto, '%')
        OR nombre_servicio LIKE CONCAT('%', texto, '%');
END

CREATE PROCEDURE usp_Buscar_Cita(IN texto VARCHAR(100))
BEGIN
    SELECT 
        ci.*,
        p.nombre AS nombrePaciente,
        CONCAT(v.nombres, ' ', v.apellidos) AS nombreVeterinario,
        s.nombre_servicio
    FROM 
        tbl_citas ci
    LEFT JOIN 
        tbl_pacientes p ON ci.id_paciente = p.id_paciente
    LEFT JOIN 
        tbl_veterinarios v ON ci.id_veterinario = v.id_veterinario
    LEFT JOIN 
        tbl_servicios s ON ci.id_servicio = s.id_servicio
    WHERE 
        ci.codigo LIKE CONCAT('%', texto, '%')
        OR p.nombre LIKE CONCAT('%', texto, '%')
        OR v.nombres LIKE CONCAT('%', texto, '%')
        OR v.apellidos LIKE CONCAT('%', texto, '%')
        OR s.nombre_servicio LIKE CONCAT('%', texto, '%');
END

-- Insecciones de datos

-- Tabla Usuarios
INSERT INTO tbl_usuarios (codigo, nombre, apellido, password, email, rol, estado) VALUES
('US-0001', 'Admin', 'Principal', 'US-0001', 'admin@clinicavet.com', 'Administrador', 1),
('US-0002', 'Elena', 'Rodriguez', 'US-0002', 'erodriguez.vet@clinicavet.com', 'Veterinario', 1),
('US-0003', 'Miguel', 'Sanchez', 'US-0003', 'msanchez.vet@clinicavet.com', 'Veterinario', 1),
('US-0004', 'Laura', 'Fernandez', 'US-0004', 'lfernandez.vet@clinicavet.com', 'Veterinario', 1),
('US-0005', 'Ricardo', 'Flores', 'US-0005', 'rflores.vet@clinicavet.com', 'Veterinario', 1),
('US-0006', 'Mateo', 'Rojas', 'US-0006', 'mrojas.vet@clinicavet.com', 'Veterinario', 1),
('US-0007', 'Alejandra', 'Nuñez', 'US-0007', 'anunez.vet@clinicavet.com', 'Veterinario', 1),
('US-0008', 'Javier', 'Silva', 'US-0008', 'jsilva.vet@clinicavet.com', 'Veterinario', 1),
('US-0009', 'Carolina', 'Guzman', 'US-0009', 'cguzman.vet@clinicavet.com', 'Veterinario', 1),
('US-0010', 'Sergio', 'Paredes', 'US-0010', 'sparedes.vet@clinicavet.com', 'Veterinario', 1),
('US-0011', 'Ana', 'Gomez', 'US-0011', 'ana.gomez@email.com', 'Usuario', 1),
('US-0012', 'Luis', 'Martinez', 'US-0012', 'luis.martinez@email.com', 'Usuario', 1),
('US-0013', 'Sofia', 'Torres', 'US-0013', 'sofia.t@email.com', 'Usuario', 1),
('US-0014', 'Carlos', 'Diaz', 'US-0014', 'carlos.d@email.com', 'Usuario', 1),
('US-0015', 'Maria', 'Huaman', 'US-0015', 'maria.h@email.com', 'Usuario', 1),
('US-0016', 'Javier', 'Quispe', 'US-0016', 'javier.q@email.com', 'Usuario', 1),
('US-0017', 'Patricia', 'Salazar', 'US-0017', 'patricia.s@email.com', 'Usuario', 1),
('US-0018', 'Roberto', 'Caceres', 'US-0018', 'roberto.c@email.com', 'Usuario', 1),
('US-0019', 'Beatriz', 'Ponce', 'US-0019', 'beatriz.p@email.com', 'Usuario', 1),
('US-0020', 'Diego', 'Benitez', 'US-0020', 'diego.b@email.com', 'Usuario', 1);

-- Tabla Clientes
INSERT INTO tbl_clientes (codigo, nombres, apellidos, dni, telefono, email, direccion, fecha_registro) VALUES
('CL-0001', 'Ana', 'Gomez', '12345678', '987654321', 'ana.gomez@email.com', 'Av. Los Girasoles 123, Lima', '2023-01-15'),
('CL-0002', 'Luis', 'Martinez', '87654321', '912345678', 'luis.martinez@email.com', 'Calle Las Begonias 456, Arequipa', '2023-03-22'),
('CL-0003', 'Sofia', 'Torres', '11223344', '955511223', 'sofia.t@email.com', 'Jr. Las Palmeras 789, Trujillo', '2023-05-10'),
('CL-0004', 'Carlos', 'Diaz', '44556677', '998877665', 'carlos.d@email.com', 'Av. El Sol 101, Cusco', '2023-06-01'),
('CL-0005', 'Maria', 'Huaman', '77665544', '944332211', 'maria.h@email.com', 'Calle Grau 222, Piura', '2023-07-18'),
('CL-0006', 'Javier', 'Quispe', '66554433', '933221100', 'javier.q@email.com', 'Av. La Marina 333, Callao', '2023-08-05'),
('CL-0007', 'Patricia', 'Salazar', '55443322', '922110099', 'patricia.s@email.com', 'Jr. Puno 444, Ica', '2023-09-12'),
('CL-0008', 'Roberto', 'Caceres', '44332211', '911009988', 'roberto.c@email.com', 'Calle Real 555, Huancayo', '2023-10-20'),
('CL-0009', 'Beatriz', 'Ponce', '33221100', '900998877', 'beatriz.p@email.com', 'Av. Bolognesi 666, Tacna', '2023-11-25'),
('CL-0010', 'Diego', 'Benitez', '22110099', '988776655', 'diego.b@email.com', 'Jr. Ayacucho 777, Chimbote', '2023-12-30');

-- Tabla Veterinarios
INSERT INTO tbl_veterinarios (codigo, nombres, apellidos, especialidad, colegiatura, estado) VALUES
('VE-0001', 'Elena', 'Rodriguez', 'Cirugía General', 'CMVP-1234', 1),
('VE-0002', 'Miguel', 'Sanchez', 'Medicina Interna Felina', 'CMVP-5678', 1),
('VE-0003', 'Laura', 'Fernandez', 'Dermatología Veterinaria', 'CMVP-9101', 1),
('VE-0004', 'Ricardo', 'Flores', 'Cardiología Veterinaria', 'CMVP-2345', 1),
('VE-0005', 'Mateo', 'Rojas', 'Animales Exóticos', 'CMVP-6789', 1),
('VE-0006', 'Daniel', 'Morales', 'Oncología Veterinaria', 'CMVP-1012', 0),
('VE-0007', 'Alejandra', 'Nuñez', 'Medicina General', 'CMVP-3456', 1),
('VE-0008', 'Javier', 'Silva', 'Odontología Veterinaria', 'CMVP-7890', 1),
('VE-0009', 'Carolina', 'Guzman', 'Fisioterapia y Rehabilitación', 'CMVP-1121', 1),
('VE-0010', 'Sergio', 'Paredes', 'Neurología', 'CMVP-4567', 1),
('VE-0011', 'Monica', 'Rios', 'Medicina de Emergencias', 'CMVP-8901', 1),
('VE-0012', 'Esteban', 'Castillo', 'Cirugía Ortopédica', 'CMVP-1213', 1),
('VE-0013', 'Luciana', 'Mora', 'Medicina Preventiva', 'CMVP-5679', 1),
('VE-0014', 'Hugo', 'Leon', 'Oftalmología Veterinaria', 'CMVP-9012', 1),
('VE-0015', 'Silvia', 'Ramirez', 'Medicina Interna Canina', 'CMVP-2346', 1),
('VE-0016', 'Andres', 'Cordova', 'Medicina General', 'CMVP-6790', 0),
('VE-0017', 'Ximena', 'Vega', 'Dermatología', 'CMVP-1013', 1),
('VE-0018', 'Nicolas', 'Solis', 'Cirugía de Tejidos Blandos', 'CMVP-3457', 1),
('VE-0019', 'Renata', 'Peña', 'Medicina Felina', 'CMVP-7891', 1),
('VE-0020', 'Gonzalo', 'Aguilar', 'Medicina General', 'CMVP-1122', 1);

-- Tabla Servicios
INSERT INTO tbl_servicios (codigo, nombre_servicio, descripcion, precio) VALUES
('S-0001', 'Consulta General', 'Revisión completa del estado de salud de la mascota.', 70.00),
('S-0002', 'Vacunación Anual Canina', 'Incluye vacuna quíntuple y antirrábica.', 120.50),
('S-0003', 'Desparasitación Interna', 'Tratamiento para eliminar parásitos intestinales.', 45.00),
('S-0004', 'Corte de Pelo y Baño', 'Servicio de grooming completo para perros y gatos.', 85.00),
('S-0005', 'Limpieza Dental (Profilaxis)', 'Eliminación de sarro y pulido dental bajo sedación.', 250.00),
('S-0006', 'Esterilización Canina Hembra', 'Cirugía para esterilizar perras.', 450.00),
('S-0007', 'Castración Canina Macho', 'Cirugía para castrar perros.', 300.00),
('S-0008', 'Esterilización Felina Hembra', 'Cirugía para esterilizar gatas.', 350.00),
('S-0009', 'Castración Felina Macho', 'Cirugía para castrar gatos.', 200.00),
('S-0010', 'Análisis de Sangre Completo', 'Hemograma y perfil bioquímico.', 180.00),
('S-0011', 'Radiografía (Rayos X)', 'Toma de placas radiográficas (por zona).', 150.00),
('S-0012', 'Ecografía Abdominal', 'Examen de órganos internos por ultrasonido.', 200.00),
('S-0013', 'Consulta de Especialidad', 'Consulta con un veterinario especialista (cardiología, etc.).', 150.00),
('S-0014', 'Hospitalización (por día)', 'Cuidado y monitoreo de pacientes internados.', 120.00),
('S-0015', 'Aplicación de Microchip', 'Implantación y registro de microchip de identificación.', 100.00),
('S-0016', 'Baño Medicado', 'Baño con shampoo especial para tratar problemas de piel.', 95.00),
('S-0017', 'Vacunación Felina Triple', 'Vacuna para Rinotraqueitis, Calicivirus y Panleucopenia.', 90.00),
('S-0018', 'Tratamiento Antipulgas', 'Aplicación de pipeta o pastilla antipulgas y garrapatas.', 60.00),
('S-0019', 'Certificado de Salud para Viaje', 'Emisión de certificado veterinario para viajes.', 130.00),
('S-0020', 'Consulta de Emergencia', 'Atención fuera del horario de citas regulares.', 180.00);

-- Tabla Pacientes
INSERT INTO tbl_pacientes (codigo, id_cliente, nombre, especie, raza, fecha_nacimiento, sexo) VALUES
('PA-0001', 1, 'Max', 'Perro', 'Labrador Retriever', '2020-05-10', 'M'),
('PA-0002', 1, 'Luna', 'Gato', 'Siames', '2021-11-20', 'H'),
('PA-0003', 2, 'Rocky', 'Perro', 'Bulldog Francés', '2019-03-15', 'M'),
('PA-0004', 3, 'Nala', 'Gato', 'Mestizo', '2022-08-01', 'H'),
('PA-0005', 4, 'Coco', 'Perro', 'Beagle', '2021-01-25', 'M'),
('PA-0006', 5, 'Kira', 'Perro', 'Pastor Alemán', '2018-07-12', 'H'),
('PA-0007', 6, 'Thor', 'Perro', 'Golden Retriever', '2022-09-30', 'M'),
('PA-0008', 7, 'Lola', 'Gato', 'Angora', '2020-02-14', 'H'),
('PA-0009', 8, 'Toby', 'Perro', 'Poodle', '2023-04-05', 'M'),
('PA-0010', 9, 'Misha', 'Gato', 'Persa', '2017-12-01', 'H'),
('PA-0011', 10, 'Simba', 'Perro', 'Chihuahua', '2022-06-18', 'M'),
('PA-0012', 1, 'Frida', 'Gato', 'Ragdoll', '2021-10-22', 'H'),
('PA-0013', 2, 'Bruno', 'Perro', 'Boxer', '2019-08-08', 'M'),
('PA-0014', 3, 'Cleo', 'Gato', 'Siberiano', '2023-01-03', 'H'),
('PA-0015', 4, 'Jack', 'Perro', 'Jack Russell', '2020-09-11', 'M'),
('PA-0016', 5, 'Sasha', 'Perro', 'Husky Siberiano', '2021-05-16', 'H'),
('PA-0017', 6, 'Oliver', 'Gato', 'British Shorthair', '2022-03-28', 'M'),
('PA-0018', 7, 'Milo', 'Perro', 'Shih Tzu', '2023-07-09', 'M'),
('PA-0019', 8, 'Zoe', 'Gato', 'Mestizo', '2019-06-21', 'H'),
('PA-0020', 9, 'Beto', 'Conejo', 'Cabeza de León', '2022-11-13', 'M'),
('PA-0021', 10, 'Paco', 'Loro', 'Amazona', '2015-04-01', 'M');

-- Tabla Citas
INSERT INTO tbl_citas (id_paciente, id_veterinario, id_servicio, codigo, fecha_cita, hora_cita, estado, observaciones, diagnostico) VALUES
(1, 1, 1, 'C-0001', '2024-06-10', '10:00:00', 'Programada', 'N/A', 'Evaluación por cojera intermitente en miembro posterior derecho.'),
(3, 2, 2, 'C-0002', '2024-06-10', '11:30:00', 'Confirmada', 'N/A', 'Aplicación de refuerzo de vacunación anual canina.'),
(2, 3, 4, 'C-0003', '2024-06-11', '09:00:00', 'Programada', 'N/A', 'Servicio de grooming y baño medicado para control de dermatitis.'),
(4, 1, 3, 'C-0004', '2024-06-11', '12:00:00', 'Confirmada', 'N/A', 'Desparasitación interna de rutina.'),
(5, 4, 13, 'C-0005', '2024-06-12', '16:00:00', 'Pendiente', 'N/A', 'Consulta cardiológica por tos seca persistente.'),
(6, 5, 11, 'C-0006', '2024-06-12', '14:00:00', 'Programada', 'N/A', 'Toma de radiografía de cadera para evaluación de displasia.'),
(7, 7, 2, 'C-0007', '2024-06-13', '10:30:00', 'Cancelada', 'N/A', 'Cita para vacunación de cachorro cancelada por el cliente.'),
(8, 2, 8, 'C-0008', '2024-06-15', '11:00:00', 'Programada', 'N/A', 'Evaluación pre-quirúrgica para esterilización felina.'),
(9, 8, 5, 'C-0009', '2024-06-14', '09:30:00', 'Confirmada', 'N/A', 'Procedimiento de profilaxis dental programado.'),
(10, 19, 13, 'C-0010', '2024-06-14', '17:00:00', 'Programada', 'N/A', 'Consulta de segunda opinión por síndrome braquiocefálico.'),
(11, 1, 1, 'C-0011', '2024-06-17', '15:00:00', 'Pendiente', 'N/A', 'Pendiente de confirmación para chequeo general.'),
(12, 12, 1, 'C-0012', '2024-06-17', '12:30:00', 'Confirmada', 'N/A', 'Control por episodio de gastritis aguda.'),
(13, 14, 1, 'C-0013', '2024-06-18', '10:00:00', 'Programada', 'N/A', 'Consulta oftalmológica por epifora crónica.'),
(14, 2, 17, 'C-0014', '2024-06-18', '11:00:00', 'Cancelada', 'N/A', 'Cita para vacunación felina cancelada, se reprogramará.'),
(15, 3, 16, 'C-0015', '2024-06-20', '14:30:00', 'Programada', 'N/A', 'Evaluación dermatológica por prurito intenso y enrojecimiento.'),
(16, 1, 10, 'C-0016', '2024-06-20', '16:30:00', 'Confirmada', 'N/A', 'Toma de muestra para análisis de sangre pre-quirúrgico.'),
(17, 15, 1, 'C-0017', '2024-06-21', '18:00:00', 'Pendiente', 'N/A', 'Pendiente de evaluación por hiporexia (disminución del apetito).'),
(18, 7, 3, 'C-0018', '2024-06-22', '09:30:00', 'Programada', 'N/A', 'Administración de desparasitante para cachorro.'),
(19, 17, 1, 'C-0019', '2024-06-24', '10:00:00', 'Confirmada', 'N/A', 'Curación y control de herida por mordedura.'),
(20, 5, 1, 'C-0020', '2024-06-25', '15:00:00', 'Programada', 'N/A', 'Consulta de rutina para animal exótico (conejo).'),
(21, 5, 1, 'C-0021', '2024-06-26', '12:00:00', 'Cancelada', 'N/A', 'Consulta por picaje en ave (cancelada).'),
(1, 4, 13, 'C-0022', '2024-06-28', '17:30:00', 'Programada', 'N/A', 'Interconsulta con especialista en cardiología por soplo cardíaco.'),
(3, 18, 12, 'C-0023', '2024-06-27', '10:30:00', 'Confirmada', 'N/A', 'Ecografía abdominal de control por antecedente de cuerpo extraño.');
 