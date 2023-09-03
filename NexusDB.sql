-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema japyld
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema nexus
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema nexus
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `nexus` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `nexus` ;

-- -----------------------------------------------------
-- Table `nexus`.`cargos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`cargos` (
  `idCargos` INT NOT NULL AUTO_INCREMENT,
  `nombreCargo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCargos`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`empresas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`empresas` (
  `idEmpresas` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `ruc` INT NOT NULL,
  `telefono` INT NOT NULL,
  `direccion` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(45) NOT NULL,
  `fechaAfiliacion` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`idEmpresas`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`imagenes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`imagenes` (
  `idImagenes` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(45) NOT NULL,
  `imagen` LONGBLOB NULL DEFAULT NULL,
  PRIMARY KEY (`idImagenes`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`usuarios` (
  `idUsuarios` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(45) NOT NULL,
  `contrasenia` VARCHAR(45) NOT NULL,
  `habilitado` TINYINT(1) NOT NULL,
  `fechaRegistro` DATE NULL DEFAULT NULL,
  `tecnicoConCuadrilla` TINYINT(1) NULL DEFAULT NULL,
  `idCargos` INT NOT NULL,
  `idEmpresas` INT NOT NULL,
  `idImagenPerfil` INT NOT NULL,
  PRIMARY KEY (`idUsuarios`),
  INDEX `fk_Usuarios_Cargos_idx` (`idCargos` ASC) VISIBLE,
  INDEX `fk_Usuarios_Empresas1_idx` (`idEmpresas` ASC) VISIBLE,
  INDEX `fk_Usuarios_Imagenes1_idx` (`idImagenPerfil` ASC) VISIBLE,
  CONSTRAINT `fk_Usuarios_Cargos`
    FOREIGN KEY (`idCargos`)
    REFERENCES `nexus`.`cargos` (`idCargos`),
  CONSTRAINT `fk_Usuarios_Empresas1`
    FOREIGN KEY (`idEmpresas`)
    REFERENCES `nexus`.`empresas` (`idEmpresas`),
  CONSTRAINT `fk_Usuarios_Imagenes1`
    FOREIGN KEY (`idImagenPerfil`)
    REFERENCES `nexus`.`imagenes` (`idImagenes`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`cuadrillas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`cuadrillas` (
  `idCuadrillas` INT NOT NULL AUTO_INCREMENT,
  `idSupervisor` INT NOT NULL,
  `idTecnico` INT NOT NULL,
  `ocupado` TINYINT(1) NOT NULL,
  PRIMARY KEY (`idCuadrillas`),
  INDEX `fk_Cuadrillas_Usuarios1_idx` (`idSupervisor` ASC) VISIBLE,
  INDEX `fk_Cuadrillas_Usuarios2_idx` (`idTecnico` ASC) VISIBLE,
  CONSTRAINT `fk_Cuadrillas_Usuarios1`
    FOREIGN KEY (`idSupervisor`)
    REFERENCES `nexus`.`usuarios` (`idUsuarios`),
  CONSTRAINT `fk_Cuadrillas_Usuarios2`
    FOREIGN KEY (`idTecnico`)
    REFERENCES `nexus`.`usuarios` (`idUsuarios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`sitios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`sitios` (
  `idSitios` INT NOT NULL AUTO_INCREMENT,
  `tipo` TINYINT(1) NOT NULL,
  `provincia` VARCHAR(45) NOT NULL,
  `distrito` VARCHAR(45) NOT NULL,
  `ubigeo` INT NOT NULL,
  `longitud` DECIMAL(12,0) NOT NULL,
  `departamento` VARCHAR(45) NOT NULL,
  `latitud` DECIMAL(12,0) NOT NULL,
  `habilitado` TINYINT(1) NOT NULL,
  `tipoZona` TINYINT(1) NOT NULL,
  `archivo` LONGBLOB NULL DEFAULT NULL,
  PRIMARY KEY (`idSitios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`tickets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`tickets` (
  `idTickets` INT NOT NULL AUTO_INCREMENT,
  `tipoTicket` TINYINT(1) NOT NULL,
  `latitud` DECIMAL(12,0) NULL DEFAULT NULL,
  `longitud` DECIMAL(12,0) NULL DEFAULT NULL,
  `descripcion` VARCHAR(45) NOT NULL,
  `estado` INT NOT NULL,
  `idImagenTicket` INT NOT NULL,
  `idSupervisorEncargado` INT NULL DEFAULT NULL,
  `idUsuarioCreador` INT NOT NULL,
  `idEmpresaAsignada` INT NOT NULL,
  `idCuadrilla` INT NULL DEFAULT NULL,
  `idSitios` INT NOT NULL,
  PRIMARY KEY (`idTickets`),
  INDEX `fk_Tickets_Imagenes1_idx` (`idImagenTicket` ASC) VISIBLE,
  INDEX `fk_Tickets_Usuarios1_idx` (`idSupervisorEncargado` ASC) VISIBLE,
  INDEX `fk_Tickets_Usuarios2_idx` (`idUsuarioCreador` ASC) VISIBLE,
  INDEX `fk_Tickets_Empresas1_idx` (`idEmpresaAsignada` ASC) VISIBLE,
  INDEX `fk_Tickets_Cuadrillas1_idx` (`idCuadrilla` ASC) VISIBLE,
  INDEX `fk_Tickets_Sitios1_idx` (`idSitios` ASC) VISIBLE,
  CONSTRAINT `fk_Tickets_Cuadrillas1`
    FOREIGN KEY (`idCuadrilla`)
    REFERENCES `nexus`.`cuadrillas` (`idCuadrillas`),
  CONSTRAINT `fk_Tickets_Empresas1`
    FOREIGN KEY (`idEmpresaAsignada`)
    REFERENCES `nexus`.`empresas` (`idEmpresas`),
  CONSTRAINT `fk_Tickets_Imagenes1`
    FOREIGN KEY (`idImagenTicket`)
    REFERENCES `nexus`.`imagenes` (`idImagenes`),
  CONSTRAINT `fk_Tickets_Sitios1`
    FOREIGN KEY (`idSitios`)
    REFERENCES `nexus`.`sitios` (`idSitios`),
  CONSTRAINT `fk_Tickets_Usuarios1`
    FOREIGN KEY (`idSupervisorEncargado`)
    REFERENCES `nexus`.`usuarios` (`idUsuarios`),
  CONSTRAINT `fk_Tickets_Usuarios2`
    FOREIGN KEY (`idUsuarioCreador`)
    REFERENCES `nexus`.`usuarios` (`idUsuarios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`comentarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`comentarios` (
  `idComentarios` INT NOT NULL AUTO_INCREMENT,
  `comentario` TEXT NULL DEFAULT NULL,
  `fechaPublicacion` DATETIME NOT NULL,
  `idTickets` INT NOT NULL,
  `idComentarista` INT NOT NULL,
  PRIMARY KEY (`idComentarios`),
  INDEX `fk_Comentarios_Tickets1_idx` (`idTickets` ASC) VISIBLE,
  INDEX `fk_Comentarios_Usuarios1_idx` (`idComentarista` ASC) VISIBLE,
  CONSTRAINT `fk_Comentarios_Tickets1`
    FOREIGN KEY (`idTickets`)
    REFERENCES `nexus`.`tickets` (`idTickets`),
  CONSTRAINT `fk_Comentarios_Usuarios1`
    FOREIGN KEY (`idComentarista`)
    REFERENCES `nexus`.`usuarios` (`idUsuarios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`dinamicaequipoatributo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`dinamicaequipoatributo` (
  `idDinamicaEquipoAtributo` INT NOT NULL AUTO_INCREMENT,
  `nuevoCampo` VARCHAR(45) NOT NULL,
  `tipoDato` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDinamicaEquipoAtributo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`tipoequipo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`tipoequipo` (
  `idTipoEquipo` INT NOT NULL AUTO_INCREMENT,
  `nombreTipo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idTipoEquipo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`equipos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`equipos` (
  `idEquipos` INT NOT NULL AUTO_INCREMENT,
  `marca` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(45) NOT NULL,
  `modelo` VARCHAR(45) NOT NULL,
  `paginaModelo` VARCHAR(45) NULL DEFAULT NULL,
  `idSitios` INT NOT NULL,
  `idImagenes` INT NOT NULL,
  `idTipoEquipo` INT NOT NULL,
  PRIMARY KEY (`idEquipos`),
  INDEX `fk_Equipos_Sitios1_idx` (`idSitios` ASC) VISIBLE,
  INDEX `fk_Equipos_Imagenes1_idx` (`idImagenes` ASC) VISIBLE,
  INDEX `fk_Equipos_TipoEquipo1_idx` (`idTipoEquipo` ASC) VISIBLE,
  CONSTRAINT `fk_Equipos_Imagenes1`
    FOREIGN KEY (`idImagenes`)
    REFERENCES `nexus`.`imagenes` (`idImagenes`),
  CONSTRAINT `fk_Equipos_Sitios1`
    FOREIGN KEY (`idSitios`)
    REFERENCES `nexus`.`sitios` (`idSitios`),
  CONSTRAINT `fk_Equipos_TipoEquipo1`
    FOREIGN KEY (`idTipoEquipo`)
    REFERENCES `nexus`.`tipoequipo` (`idTipoEquipo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`dinamicaequipovalor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`dinamicaequipovalor` (
  `idAtributoEquipo` INT NOT NULL,
  `idEquipos` INT NOT NULL,
  `valorNuevoCampo` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idAtributoEquipo`, `idEquipos`),
  INDEX `fk_DinamicaEquipoAtributo_has_Equipos_Equipos1_idx` (`idEquipos` ASC) VISIBLE,
  INDEX `fk_DinamicaEquipoAtributo_has_Equipos_DinamicaEquipoAtribut_idx` (`idAtributoEquipo` ASC) VISIBLE,
  CONSTRAINT `fk_DinamicaEquipoAtributo_has_Equipos_DinamicaEquipoAtributo1`
    FOREIGN KEY (`idAtributoEquipo`)
    REFERENCES `nexus`.`dinamicaequipoatributo` (`idDinamicaEquipoAtributo`),
  CONSTRAINT `fk_DinamicaEquipoAtributo_has_Equipos_Equipos1`
    FOREIGN KEY (`idEquipos`)
    REFERENCES `nexus`.`equipos` (`idEquipos`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`dinamicasitioatributo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`dinamicasitioatributo` (
  `idDinamicaSitioAtributo` INT NOT NULL AUTO_INCREMENT,
  `nuevoCampo` VARCHAR(45) NOT NULL,
  `tipoAtributo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDinamicaSitioAtributo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`dinamicasitiovalor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`dinamicasitiovalor` (
  `idSitios` INT NOT NULL,
  `idAtributoSitio` INT NOT NULL,
  `valorNuevoCampo` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idSitios`, `idAtributoSitio`),
  INDEX `fk_Sitios_has_DinamicaSitioAtributo_DinamicaSitioAtributo1_idx` (`idAtributoSitio` ASC) VISIBLE,
  INDEX `fk_Sitios_has_DinamicaSitioAtributo_Sitios1_idx` (`idSitios` ASC) VISIBLE,
  CONSTRAINT `fk_Sitios_has_DinamicaSitioAtributo_DinamicaSitioAtributo1`
    FOREIGN KEY (`idAtributoSitio`)
    REFERENCES `nexus`.`dinamicasitioatributo` (`idDinamicaSitioAtributo`),
  CONSTRAINT `fk_Sitios_has_DinamicaSitioAtributo_Sitios1`
    FOREIGN KEY (`idSitios`)
    REFERENCES `nexus`.`sitios` (`idSitios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nexus`.`formularios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nexus`.`formularios` (
  `idFormularios` INT NOT NULL AUTO_INCREMENT,
  `fechaLlenado` DATETIME NOT NULL,
  `descripcion` VARCHAR(45) NOT NULL,
  `confirmacion` TINYINT(1) NOT NULL,
  `idTecnico` INT NOT NULL,
  `idImagenesForm` INT NOT NULL,
  `idTickets` INT NOT NULL,
  PRIMARY KEY (`idFormularios`),
  INDEX `fk_Formularios_Usuarios1_idx` (`idTecnico` ASC) VISIBLE,
  INDEX `fk_Formularios_Imagenes1_idx` (`idImagenesForm` ASC) VISIBLE,
  INDEX `fk_Formularios_Tickets1_idx` (`idTickets` ASC) VISIBLE,
  CONSTRAINT `fk_Formularios_Imagenes1`
    FOREIGN KEY (`idImagenesForm`)
    REFERENCES `nexus`.`imagenes` (`idImagenes`),
  CONSTRAINT `fk_Formularios_Tickets1`
    FOREIGN KEY (`idTickets`)
    REFERENCES `nexus`.`tickets` (`idTickets`),
  CONSTRAINT `fk_Formularios_Usuarios1`
    FOREIGN KEY (`idTecnico`)
    REFERENCES `nexus`.`usuarios` (`idUsuarios`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
