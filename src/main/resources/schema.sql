

CREATE DATABASE IF NOT EXISTS medical_turnos_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE medical_turnos_db;


CREATE TABLE IF NOT EXISTS usuarios (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    rol        VARCHAR(20)  NOT NULL DEFAULT 'RECEPCIONISTA',
    activo     BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS turnos (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_nombre   VARCHAR(100) NOT NULL,
    paciente_dni      VARCHAR(20)  NOT NULL,
    paciente_email    VARCHAR(100),
    paciente_telefono VARCHAR(20),
    medico_id         BIGINT       NOT NULL,
    medico_nombre     VARCHAR(100) NOT NULL,
    especialidad      VARCHAR(100) NOT NULL,
    fecha             DATE         NOT NULL,
    hora              TIME         NOT NULL,
    estado            VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE',
    observaciones     VARCHAR(500),
    consultorio       VARCHAR(50),
    creado_por_id     BIGINT,
    created_at        DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_turno_usuario FOREIGN KEY (creado_por_id)
        REFERENCES usuarios(id) ON DELETE SET NULL,
    INDEX idx_fecha           (fecha),
    INDEX idx_medico_fecha    (medico_id, fecha),
    INDEX idx_paciente_dni    (paciente_dni),
    INDEX idx_estado          (estado),
    INDEX idx_creado_por      (creado_por_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT IGNORE INTO usuarios (username, email, password, rol) VALUES
('admin',      'admin@medical.com',     '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN'),
('recepcion1', 'recepcion@medical.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'RECEPCIONISTA'),
('drperez',    'drperez@medical.com',   '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'MEDICO');
