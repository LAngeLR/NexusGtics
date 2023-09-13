-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: nexus
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `archivos`
--

DROP TABLE IF EXISTS `archivos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archivos` (
  `idArchivos` int NOT NULL AUTO_INCREMENT,
  `tipo` int NOT NULL,
  `archivo` longblob,
  PRIMARY KEY (`idArchivos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archivos`
--

LOCK TABLES `archivos` WRITE;
/*!40000 ALTER TABLE `archivos` DISABLE KEYS */;
/*!40000 ALTER TABLE `archivos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cargos`
--

DROP TABLE IF EXISTS `cargos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cargos` (
  `idCargos` int NOT NULL AUTO_INCREMENT,
  `nombreCargo` varchar(45) NOT NULL,
  PRIMARY KEY (`idCargos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cargos`
--

LOCK TABLES `cargos` WRITE;
/*!40000 ALTER TABLE `cargos` DISABLE KEYS */;
/*!40000 ALTER TABLE `cargos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comentarios`
--

DROP TABLE IF EXISTS `comentarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comentarios` (
  `idComentarios` int NOT NULL AUTO_INCREMENT,
  `comentario` varchar(250) DEFAULT NULL,
  `fechaPublicacion` datetime NOT NULL,
  `idTickets` int NOT NULL,
  `idComentarista` int NOT NULL,
  PRIMARY KEY (`idComentarios`),
  KEY `fk_Comentarios_Tickets1_idx` (`idTickets`),
  KEY `fk_Comentarios_Usuarios1_idx` (`idComentarista`),
  CONSTRAINT `fk_Comentarios_Tickets1` FOREIGN KEY (`idTickets`) REFERENCES `tickets` (`idTickets`),
  CONSTRAINT `fk_Comentarios_Usuarios1` FOREIGN KEY (`idComentarista`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentarios`
--

LOCK TABLES `comentarios` WRITE;
/*!40000 ALTER TABLE `comentarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `comentarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuadrillas`
--

DROP TABLE IF EXISTS `cuadrillas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuadrillas` (
  `idCuadrillas` int NOT NULL AUTO_INCREMENT,
  `idSupervisor` int NOT NULL,
  `idTecnico` int NOT NULL,
  `ocupado` tinyint(1) NOT NULL,
  `fechaCreacion` datetime NOT NULL,
  PRIMARY KEY (`idCuadrillas`),
  KEY `fk_Cuadrillas_Usuarios1_idx` (`idSupervisor`),
  KEY `fk_Cuadrillas_Usuarios2_idx` (`idTecnico`),
  CONSTRAINT `fk_Cuadrillas_Usuarios1` FOREIGN KEY (`idSupervisor`) REFERENCES `usuarios` (`idUsuarios`),
  CONSTRAINT `fk_Cuadrillas_Usuarios2` FOREIGN KEY (`idTecnico`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuadrillas`
--

LOCK TABLES `cuadrillas` WRITE;
/*!40000 ALTER TABLE `cuadrillas` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuadrillas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dinamicaequipoatributo`
--

DROP TABLE IF EXISTS `dinamicaequipoatributo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dinamicaequipoatributo` (
  `idDinamicaEquipoAtributo` int NOT NULL AUTO_INCREMENT,
  `nuevoCampo` varchar(45) NOT NULL,
  `tipoDato` int NOT NULL,
  PRIMARY KEY (`idDinamicaEquipoAtributo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dinamicaequipoatributo`
--

LOCK TABLES `dinamicaequipoatributo` WRITE;
/*!40000 ALTER TABLE `dinamicaequipoatributo` DISABLE KEYS */;
/*!40000 ALTER TABLE `dinamicaequipoatributo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dinamicaequipovalor`
--

DROP TABLE IF EXISTS `dinamicaequipovalor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dinamicaequipovalor` (
  `idAtributoEquipo` int NOT NULL,
  `idEquipos` int NOT NULL,
  `valorNuevoCampo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idAtributoEquipo`,`idEquipos`),
  KEY `fk_DinamicaEquipoAtributo_has_Equipos_Equipos1_idx` (`idEquipos`),
  KEY `fk_DinamicaEquipoAtributo_has_Equipos_DinamicaEquipoAtribut_idx` (`idAtributoEquipo`),
  CONSTRAINT `fk_DinamicaEquipoAtributo_has_Equipos_DinamicaEquipoAtributo1` FOREIGN KEY (`idAtributoEquipo`) REFERENCES `dinamicaequipoatributo` (`idDinamicaEquipoAtributo`),
  CONSTRAINT `fk_DinamicaEquipoAtributo_has_Equipos_Equipos1` FOREIGN KEY (`idEquipos`) REFERENCES `equipos` (`idEquipos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dinamicaequipovalor`
--

LOCK TABLES `dinamicaequipovalor` WRITE;
/*!40000 ALTER TABLE `dinamicaequipovalor` DISABLE KEYS */;
/*!40000 ALTER TABLE `dinamicaequipovalor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dinamicasitioatributo`
--

DROP TABLE IF EXISTS `dinamicasitioatributo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dinamicasitioatributo` (
  `idDinamicaSitioAtributo` int NOT NULL AUTO_INCREMENT,
  `nuevoCampo` varchar(45) NOT NULL,
  `tipoAtributo` int NOT NULL,
  PRIMARY KEY (`idDinamicaSitioAtributo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dinamicasitioatributo`
--

LOCK TABLES `dinamicasitioatributo` WRITE;
/*!40000 ALTER TABLE `dinamicasitioatributo` DISABLE KEYS */;
/*!40000 ALTER TABLE `dinamicasitioatributo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dinamicasitiovalor`
--

DROP TABLE IF EXISTS `dinamicasitiovalor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dinamicasitiovalor` (
  `idSitios` int NOT NULL,
  `idAtributoSitio` int NOT NULL,
  `valorNuevoCampo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idSitios`,`idAtributoSitio`),
  KEY `fk_Sitios_has_DinamicaSitioAtributo_DinamicaSitioAtributo1_idx` (`idAtributoSitio`),
  KEY `fk_Sitios_has_DinamicaSitioAtributo_Sitios1_idx` (`idSitios`),
  CONSTRAINT `fk_Sitios_has_DinamicaSitioAtributo_DinamicaSitioAtributo1` FOREIGN KEY (`idAtributoSitio`) REFERENCES `dinamicasitioatributo` (`idDinamicaSitioAtributo`),
  CONSTRAINT `fk_Sitios_has_DinamicaSitioAtributo_Sitios1` FOREIGN KEY (`idSitios`) REFERENCES `sitios` (`idSitios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dinamicasitiovalor`
--

LOCK TABLES `dinamicasitiovalor` WRITE;
/*!40000 ALTER TABLE `dinamicasitiovalor` DISABLE KEYS */;
/*!40000 ALTER TABLE `dinamicasitiovalor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empresas`
--

DROP TABLE IF EXISTS `empresas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empresas` (
  `idEmpresas` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `ruc` int NOT NULL,
  `telefono` int NOT NULL,
  `direccion` varchar(120) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `fechaAfiliacion` date DEFAULT NULL,
  `representante` varchar(75) NOT NULL,
  PRIMARY KEY (`idEmpresas`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresas`
--

LOCK TABLES `empresas` WRITE;
/*!40000 ALTER TABLE `empresas` DISABLE KEYS */;
/*!40000 ALTER TABLE `empresas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipos`
--

DROP TABLE IF EXISTS `equipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipos` (
  `idEquipos` int NOT NULL AUTO_INCREMENT,
  `marca` varchar(45) NOT NULL,
  `descripcion` varchar(45) NOT NULL,
  `modelo` varchar(45) NOT NULL,
  `paginaModelo` varchar(45) DEFAULT NULL,
  `idSitios` int NOT NULL,
  `idImagenes` int NOT NULL,
  `idTipoEquipo` int NOT NULL,
  PRIMARY KEY (`idEquipos`),
  KEY `fk_Equipos_Sitios1_idx` (`idSitios`),
  KEY `fk_Equipos_Imagenes1_idx` (`idImagenes`),
  KEY `fk_Equipos_TipoEquipo1_idx` (`idTipoEquipo`),
  CONSTRAINT `fk_Equipos_Imagenes1` FOREIGN KEY (`idImagenes`) REFERENCES `archivos` (`idArchivos`),
  CONSTRAINT `fk_Equipos_Sitios1` FOREIGN KEY (`idSitios`) REFERENCES `sitios` (`idSitios`),
  CONSTRAINT `fk_Equipos_TipoEquipo1` FOREIGN KEY (`idTipoEquipo`) REFERENCES `tipoequipo` (`idTipoEquipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipos`
--

LOCK TABLES `equipos` WRITE;
/*!40000 ALTER TABLE `equipos` DISABLE KEYS */;
/*!40000 ALTER TABLE `equipos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formularios`
--

DROP TABLE IF EXISTS `formularios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `formularios` (
  `idFormularios` int NOT NULL AUTO_INCREMENT,
  `fechaLlenado` datetime NOT NULL,
  `descripcion` varchar(45) NOT NULL,
  `confirmacion` tinyint(1) NOT NULL,
  `idTecnico` int NOT NULL,
  `idImagenesForm` int NOT NULL,
  `idTickets` int NOT NULL,
  `idTipoTicket` int NOT NULL,
  PRIMARY KEY (`idFormularios`),
  KEY `fk_Formularios_Usuarios1_idx` (`idTecnico`),
  KEY `fk_Formularios_Imagenes1_idx` (`idImagenesForm`),
  KEY `fk_Formularios_Tickets1_idx` (`idTickets`),
  KEY `fk_formularios_tipoticket1_idx` (`idTipoTicket`),
  CONSTRAINT `fk_Formularios_Imagenes1` FOREIGN KEY (`idImagenesForm`) REFERENCES `archivos` (`idArchivos`),
  CONSTRAINT `fk_Formularios_Tickets1` FOREIGN KEY (`idTickets`) REFERENCES `tickets` (`idTickets`),
  CONSTRAINT `fk_formularios_tipoticket1` FOREIGN KEY (`idTipoTicket`) REFERENCES `tipoticket` (`idTipoTicket`),
  CONSTRAINT `fk_Formularios_Usuarios1` FOREIGN KEY (`idTecnico`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formularios`
--

LOCK TABLES `formularios` WRITE;
/*!40000 ALTER TABLE `formularios` DISABLE KEYS */;
/*!40000 ALTER TABLE `formularios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historialtickets`
--

DROP TABLE IF EXISTS `historialtickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historialtickets` (
  `idhistorialTickets` int NOT NULL,
  `estado` int NOT NULL,
  `fechaCambioEstado` datetime NOT NULL,
  `idTickets` int NOT NULL,
  `iidUsuarios` int NOT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`idhistorialTickets`),
  KEY `fk_historialTickets_tickets_idx` (`idTickets`),
  KEY `fk_historialtickets_usuarios1_idx` (`iidUsuarios`),
  CONSTRAINT `fk_historialTickets_tickets` FOREIGN KEY (`idTickets`) REFERENCES `tickets` (`idTickets`),
  CONSTRAINT `fk_historialtickets_usuarios1` FOREIGN KEY (`iidUsuarios`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historialtickets`
--

LOCK TABLES `historialtickets` WRITE;
/*!40000 ALTER TABLE `historialtickets` DISABLE KEYS */;
/*!40000 ALTER TABLE `historialtickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sitios`
--

DROP TABLE IF EXISTS `sitios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sitios` (
  `idSitios` int NOT NULL AUTO_INCREMENT,
  `tipo` tinyint(1) NOT NULL,
  `provincia` varchar(45) NOT NULL,
  `distrito` varchar(45) NOT NULL,
  `ubigeo` int NOT NULL,
  `departamento` varchar(45) NOT NULL,
  `longitud` decimal(10,7) NOT NULL,
  `latitud` decimal(10,7) NOT NULL,
  `habilitado` tinyint(1) NOT NULL,
  `tipoZona` tinyint(1) NOT NULL,
  `idArchivos` int NOT NULL,
  PRIMARY KEY (`idSitios`),
  KEY `fk_sitios_archivos1_idx` (`idArchivos`),
  CONSTRAINT `fk_sitios_archivos1` FOREIGN KEY (`idArchivos`) REFERENCES `archivos` (`idArchivos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sitios`
--

LOCK TABLES `sitios` WRITE;
/*!40000 ALTER TABLE `sitios` DISABLE KEYS */;
/*!40000 ALTER TABLE `sitios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tickets` (
  `idTickets` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) NOT NULL,
  `estado` int NOT NULL,
  `fechaCreacion` datetime NOT NULL,
  `fechaCierre` datetime DEFAULT NULL,
  `usuarioSolicitante` varchar(70) DEFAULT NULL,
  `idImagenTicket` int NOT NULL,
  `idSupervisorEncargado` int DEFAULT NULL,
  `idUsuarioCreador` int NOT NULL,
  `idEmpresaAsignada` int NOT NULL,
  `idCuadrilla` int DEFAULT NULL,
  `idSitios` int NOT NULL,
  `idTipoTicket` int NOT NULL,
  PRIMARY KEY (`idTickets`),
  KEY `fk_Tickets_Imagenes1_idx` (`idImagenTicket`),
  KEY `fk_Tickets_Usuarios1_idx` (`idSupervisorEncargado`),
  KEY `fk_Tickets_Usuarios2_idx` (`idUsuarioCreador`),
  KEY `fk_Tickets_Empresas1_idx` (`idEmpresaAsignada`),
  KEY `fk_Tickets_Cuadrillas1_idx` (`idCuadrilla`),
  KEY `fk_Tickets_Sitios1_idx` (`idSitios`),
  KEY `fk_tickets_tipoticket1_idx` (`idTipoTicket`),
  CONSTRAINT `fk_Tickets_Cuadrillas1` FOREIGN KEY (`idCuadrilla`) REFERENCES `cuadrillas` (`idCuadrillas`),
  CONSTRAINT `fk_Tickets_Empresas1` FOREIGN KEY (`idEmpresaAsignada`) REFERENCES `empresas` (`idEmpresas`),
  CONSTRAINT `fk_Tickets_Imagenes1` FOREIGN KEY (`idImagenTicket`) REFERENCES `archivos` (`idArchivos`),
  CONSTRAINT `fk_Tickets_Sitios1` FOREIGN KEY (`idSitios`) REFERENCES `sitios` (`idSitios`),
  CONSTRAINT `fk_tickets_tipoticket1` FOREIGN KEY (`idTipoTicket`) REFERENCES `tipoticket` (`idTipoTicket`),
  CONSTRAINT `fk_Tickets_Usuarios1` FOREIGN KEY (`idSupervisorEncargado`) REFERENCES `usuarios` (`idUsuarios`),
  CONSTRAINT `fk_Tickets_Usuarios2` FOREIGN KEY (`idUsuarioCreador`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipoequipo`
--

DROP TABLE IF EXISTS `tipoequipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipoequipo` (
  `idTipoEquipo` int NOT NULL AUTO_INCREMENT,
  `nombreTipo` varchar(45) NOT NULL,
  PRIMARY KEY (`idTipoEquipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoequipo`
--

LOCK TABLES `tipoequipo` WRITE;
/*!40000 ALTER TABLE `tipoequipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipoequipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipoticket`
--

DROP TABLE IF EXISTS `tipoticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipoticket` (
  `idTipoTicket` int NOT NULL,
  `nombreTipoTicket` varchar(45) NOT NULL,
  PRIMARY KEY (`idTipoTicket`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoticket`
--

LOCK TABLES `tipoticket` WRITE;
/*!40000 ALTER TABLE `tipoticket` DISABLE KEYS */;
/*!40000 ALTER TABLE `tipoticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `idUsuarios` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `contrasenia` varchar(45) NOT NULL,
  `habilitado` tinyint(1) NOT NULL,
  `fechaRegistro` date DEFAULT NULL,
  `tecnicoConCuadrilla` tinyint(1) DEFAULT NULL,
  `idCargos` int NOT NULL,
  `idEmpresas` int NOT NULL,
  `idImagenPerfil` int NOT NULL,
  PRIMARY KEY (`idUsuarios`),
  KEY `fk_Usuarios_Cargos_idx` (`idCargos`),
  KEY `fk_Usuarios_Empresas1_idx` (`idEmpresas`),
  KEY `fk_Usuarios_Imagenes1_idx` (`idImagenPerfil`),
  CONSTRAINT `fk_Usuarios_Cargos` FOREIGN KEY (`idCargos`) REFERENCES `cargos` (`idCargos`),
  CONSTRAINT `fk_Usuarios_Empresas1` FOREIGN KEY (`idEmpresas`) REFERENCES `empresas` (`idEmpresas`),
  CONSTRAINT `fk_Usuarios_Imagenes1` FOREIGN KEY (`idImagenPerfil`) REFERENCES `archivos` (`idArchivos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-13 10:17:12
