-- ============================================================
-- Base de datos de Actividades (práctica Día 3)
-- Ejecutar como ROOT:   mysql -u root -p < sql/01_actividades.sql
-- ============================================================
DROP DATABASE IF EXISTS actividades_db;
CREATE DATABASE actividades_db CHARACTER SET utf8mb4;
USE actividades_db;

-- ---------- TABLA (con restricciones: la BD también valida) ----------
CREATE TABLE actividad (
    codigo       VARCHAR(10)   NOT NULL,
    nombre       VARCHAR(60)   NOT NULL,
    tipo         ENUM('PRESENCIAL','VIRTUAL','HIBRIDA') NOT NULL,
    tarifa_base  DECIMAL(10,2) NOT NULL,
    cupo_total   INT           NOT NULL,
    inscritos    INT           NOT NULL DEFAULT 0,
    PRIMARY KEY (codigo),
    CONSTRAINT ck_tarifa    CHECK (tarifa_base > 0),
    CONSTRAINT ck_cupo      CHECK (cupo_total > 0),
    CONSTRAINT ck_inscritos CHECK (inscritos >= 0 AND inscritos <= cupo_total)
);

-- ---------- USUARIOS (mínimo privilegio) ----------
-- act_definer: dueño de la lógica. Puede tocar la tabla.
CREATE USER IF NOT EXISTS 'act_definer'@'localhost' IDENTIFIED BY 'Definer#2026';
GRANT SELECT, INSERT, UPDATE, DELETE ON actividades_db.actividad TO 'act_definer'@'localhost';
GRANT EXECUTE ON actividades_db.* TO 'act_definer'@'localhost';

-- act_app: el que usa Java. NO puede tocar la tabla, solo ejecutar procedimientos.
CREATE USER IF NOT EXISTS 'act_app'@'localhost' IDENTIFIED BY 'App#2026';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'act_app'@'localhost';

-- ---------- PROCEDIMIENTOS ----------
DELIMITER $$

CREATE DEFINER='act_definer'@'localhost' PROCEDURE sp_listar_actividades()
SQL SECURITY DEFINER
BEGIN
    SELECT codigo, nombre, tipo, tarifa_base, cupo_total, inscritos
    FROM actividad ORDER BY codigo;
END$$

CREATE DEFINER='act_definer'@'localhost' PROCEDURE sp_insertar_actividad(
    IN p_codigo VARCHAR(10), IN p_nombre VARCHAR(60), IN p_tipo VARCHAR(12),
    IN p_tarifa DECIMAL(10,2), IN p_cupo INT)
SQL SECURITY DEFINER
BEGIN
    IF EXISTS (SELECT 1 FROM actividad WHERE codigo = p_codigo) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya existe una actividad con ese código';
    END IF;
    INSERT INTO actividad (codigo, nombre, tipo, tarifa_base, cupo_total, inscritos)
    VALUES (p_codigo, p_nombre, p_tipo, p_tarifa, p_cupo, 0);
END$$

CREATE DEFINER='act_definer'@'localhost' PROCEDURE sp_actualizar_inscritos(
    IN p_codigo VARCHAR(10), IN p_inscritos INT)
SQL SECURITY DEFINER
BEGIN
    IF NOT EXISTS (SELECT 1 FROM actividad WHERE codigo = p_codigo) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No existe la actividad';
    END IF;
    UPDATE actividad SET inscritos = p_inscritos WHERE codigo = p_codigo;
END$$

CREATE DEFINER='act_definer'@'localhost' PROCEDURE sp_eliminar_actividad(IN p_codigo VARCHAR(10))
SQL SECURITY DEFINER
BEGIN
    DELETE FROM actividad WHERE codigo = p_codigo;
    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No existe la actividad';
    END IF;
END$$

DELIMITER ;

-- ---------- PERMISOS DE LA APP: solo EXECUTE, procedimiento por procedimiento ----------
GRANT EXECUTE ON PROCEDURE actividades_db.sp_listar_actividades   TO 'act_app'@'localhost';
GRANT EXECUTE ON PROCEDURE actividades_db.sp_insertar_actividad   TO 'act_app'@'localhost';
GRANT EXECUTE ON PROCEDURE actividades_db.sp_actualizar_inscritos TO 'act_app'@'localhost';
GRANT EXECUTE ON PROCEDURE actividades_db.sp_eliminar_actividad   TO 'act_app'@'localhost';
FLUSH PRIVILEGES;

-- ---------- DATOS DE PRUEBA ----------
CALL sp_insertar_actividad('A01', 'Fotografia', 'PRESENCIAL', 5000, 2);
CALL sp_insertar_actividad('A02', 'Excel basico', 'VIRTUAL', 4000, 3);
CALL sp_insertar_actividad('A03', 'SCRUM', 'HIBRIDA', 6000, 5);
