-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: nexus
-- ------------------------------------------------------
-- Server version	8.0.28

CREATE DATABASE  IF NOT EXISTS `nexus` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `nexus`;

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
  `nombre` varchar(45) DEFAULT NULL,
  `contentType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idArchivos`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archivos`
--

LOCK TABLES `archivos` WRITE;
/*!40000 ALTER TABLE `archivos` DISABLE KEYS */;
INSERT INTO `archivos` VALUES (1,1,NULL,'archivo-1.png','image/png'),(2,1,NULL,'archivo-2.png','image/png'),(3,1,NULL,'archivo-3.png','image/png'),(4,1,NULL,'archivo-4.png','image/png'),(5,1,NULL,'archivo-5.png','image/png'),(6,1,NULL,'archivo-6.png','image/png'),(7,1,NULL,'archivo-7.png','image/png'),(8,1,NULL,'archivo-8.png','image/png'),(9,1,NULL,'archivo-9.png','image/png'),(10,1,NULL,'archivo-10.png','image/png'),(11,1,NULL,'archivo-11.png','image/png'),(12,1,NULL,'archivo-12.png','image/png'),(13,1,NULL,'archivo-13.png','image/png'),(14,1,NULL,'archivo-14.png','image/png'),(15,1,NULL,'archivo-15.png','image/png'),(16,1,NULL,'archivo-16.png','image/png'),(17,1,NULL,'archivo-17.png','image/png'),(18,1,NULL,'archivo-18.png','image/png'),(19,1,NULL,'archivo-19.png','image/png'),(20,1,NULL,'archivo-20.png','image/png'),(21,1,NULL,'archivo-21.png','image/png'),(22,1,NULL,'archivo-22.png','image/png'),(23,1,NULL,'archivo-23.png','image/png'),(24,1,NULL,'archivo-24.png','image/png'),(25,1,NULL,'archivo-25.png','image/png'),(26,1,NULL,'archivo-26.png','image/png'),(27,1,NULL,'archivo-27.png','image/png'),(28,1,NULL,'archivo-28.png','image/png'),(29,1,NULL,'archivo-29.png','image/png'),(30,1,NULL,'archivo-30.png','image/png'),(31,1,NULL,'archivo-31.png','image/png'),(32,1,NULL,'archivo-32.png','image/png'),(33,1,NULL,'archivo-33.png','image/png'),(34,1,NULL,'archivo-34.png','image/png'),(35,1,NULL,'archivo-35.png','image/png'),(36,1,NULL,'archivo-36.png','image/png'),(37,1,NULL,'archivo-37.png','image/png'),(38,1,NULL,'archivo-38.png','image/png'),(39,1,NULL,'archivo-39.png','image/png'),(40,1,NULL,'archivo-40.png','image/png'),(41,1,NULL,'archivo-41.png','image/png'),(42,1,NULL,'archivo-42.png','image/png'),(43,1,NULL,'archivo-43.png','image/png'),(44,1,NULL,'archivo-44.png','image/png'),(45,1,NULL,'archivo-45.png','image/png'),(46,1,NULL,'archivo-46.png','image/png'),(47,1,NULL,'archivo-47.png','image/png'),(48,1,NULL,'archivo-48.png','image/png'),(49,1,NULL,'archivo-49.png','image/png'),(50, 1, NULL, 'archivo-50.png', 'image/png'),(51, 1, NULL, 'archivo-51.png', 'image/png'),(52, 1, NULL, 'archivo-52.png', 'image/png'),(53, 1, NULL, 'archivo-53.png', 'image/png'),(54, 1, NULL, 'archivo-54.png', 'image/png'),(55, 1, NULL, 'archivo-55.png', 'image/png'),(56, 1, NULL, 'archivo-56.png', 'image/png'),(57, 1, NULL, 'archivo-57.png', 'image/png'),(58, 1, NULL, 'archivo-58.png', 'image/png'),(59, 1, NULL, 'archivo-59.png', 'image/png'),(60, 1, NULL, 'archivo-60.png', 'image/png'),(61, 1, NULL, 'archivo-61.png', 'image/png'),(62, 1, NULL, 'archivo-62.png', 'image/png'),(63, 1, NULL, 'archivo-63.png', 'image/png'),(64, 1, NULL, 'archivo-64.png', 'image/png'),(65, 1, NULL, 'archivo-65.png', 'image/png'),(66, 1, NULL, 'archivo-66.png', 'image/png'),(67, 1, NULL, 'archivo-67.png', 'image/png'),(68, 1, NULL, 'archivo-68.png', 'image/png'),(69, 1, NULL, 'archivo-69.png', 'image/png');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cargos`
--

LOCK TABLES `cargos` WRITE;
/*!40000 ALTER TABLE `cargos` DISABLE KEYS */;
INSERT INTO `cargos` VALUES (1,'Super administrador'),(2,'Administrador'),(3,'Analista de OyM'),(4,'Analista de Planificacion o Ingenieria'),(5,'Supervisor de Campo'),(6,'Tecnico');
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentarios`
--

LOCK TABLES `comentarios` WRITE;
/*!40000 ALTER TABLE `comentarios` DISABLE KEYS */;
INSERT INTO `comentarios` VALUES (1,'Hacerlo rÃÂ¡pido','2023-09-10 01:00:00',1,4),(2,'Apurarse','2023-09-10 03:00:00',2,6),(3,'Realizado','2023-09-10 05:00:00',3,8),(4,'Urgente','2023-09-10 05:00:00',4,4),(5,'Apurarse','2023-09-10 05:00:00',5,6),(6,'Hacerlo rÃÂ¡pido','2023-09-10 05:00:00',6,8),(7,'Realizado','2023-09-10 05:00:00',7,4),(8,'Urgente','2023-09-10 05:00:00',8,6),(9,'Hacerlo rÃÂ¡pido','2023-09-10 05:00:00',9,6),(10,'Urgente','2023-09-10 05:00:00',10,4),(11,'Urgente','2023-09-10 05:00:00',11,6),(12,'Realizado','2023-09-10 05:00:00',12,8),(13,'Hacerlo rÃÂ¡pido','2023-09-10 05:00:00',13,8),(14,'Urgente','2023-09-10 05:00:00',14,6),(15,'Apurarse','2023-09-10 05:00:00',15,4),(16,'Hacerlo rÃÂ¡pido','2023-09-10 05:00:00',16,8),(17,'Realizado','2023-09-10 05:00:00',17,4),(18,'Apurarse','2023-09-10 05:00:00',18,6),(19,'Hacerlo rÃÂ¡pido','2023-09-10 05:00:00',19,6);
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
  `ocupado` tinyint(1) NOT NULL,
  `fechaCreacion` datetime NOT NULL,
  PRIMARY KEY (`idCuadrillas`),
  KEY `fk_Cuadrillas_Usuarios1_idx` (`idSupervisor`),
  CONSTRAINT `fk_Cuadrillas_Usuarios1` FOREIGN KEY (`idSupervisor`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuadrillas`
--

LOCK TABLES `cuadrillas` WRITE;
/*!40000 ALTER TABLE `cuadrillas` DISABLE KEYS */;
INSERT INTO `cuadrillas` VALUES (1,5,0,'2022-09-10 00:00:00'),(2,6,0,'2022-09-11 00:00:00');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dinamicaequipoatributo`
--

LOCK TABLES `dinamicaequipoatributo` WRITE;
/*!40000 ALTER TABLE `dinamicaequipoatributo` DISABLE KEYS */;
INSERT INTO `dinamicaequipoatributo` VALUES (1,'voltaje',1);
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
INSERT INTO `dinamicaequipovalor` VALUES (1,1,'220'),(1,2,'110'),(1,3,'120'),(1,4,'140'),(1,5,'150'),(1,6,'160'),(1,7,'155'),(1,8,'180'),(1,9,'199'),(1,10,'200'),(1,11,'201'),(1,12,'222'),(1,13,'111'),(1,14,'109');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dinamicasitioatributo`
--

LOCK TABLES `dinamicasitioatributo` WRITE;
/*!40000 ALTER TABLE `dinamicasitioatributo` DISABLE KEYS */;
INSERT INTO `dinamicasitioatributo` VALUES (1,'clima',1);
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
INSERT INTO `dinamicasitiovalor` VALUES (1,1,'lluvioso'),(2,1,'nublado');
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
  `ruc` bigint NOT NULL,
  `telefono` int NOT NULL,
  `direccion` varchar(120) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `fechaAfiliacion` date DEFAULT NULL,
  `representante` varchar(75) NOT NULL,
  PRIMARY KEY (`idEmpresas`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresas`
--

LOCK TABLES `empresas` WRITE;
/*!40000 ALTER TABLE `empresas` DISABLE KEYS */;
INSERT INTO `empresas` VALUES (1,'Nexus',12345678910,123456789,'Av. Universitaria 1801','nexus@gmail.com','2020-06-15','Luis Ramirez'),(2,'4T',10987654321,987654321,'Av. Arequipa 500','4t@gmail.com','2021-06-15','Paolo Guerrero'),(3,'Huawei',11122233344,111222333,'Av. Roosevelt 500','huawei@gmail.com','2022-06-15','Jaime Perez');
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
  `paginaModelo` varchar(130) DEFAULT NULL,
  `idImagenes` int DEFAULT NULL,
  `idTipoEquipo` int NOT NULL,
  `habilitado` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`idEquipos`),
  KEY `fk_Equipos_Imagenes1_idx` (`idImagenes`),
  KEY `fk_Equipos_TipoEquipo1_idx` (`idTipoEquipo`),
  CONSTRAINT `fk_Equipos_Imagenes1` FOREIGN KEY (`idImagenes`) REFERENCES `archivos` (`idArchivos`),
  CONSTRAINT `fk_Equipos_TipoEquipo1` FOREIGN KEY (`idTipoEquipo`) REFERENCES `tipoequipo` (`idTipoEquipo`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipos`
--

LOCK TABLES `equipos` WRITE;
/*!40000 ALTER TABLE `equipos` DISABLE KEYS */;
INSERT INTO `equipos` VALUES (1,'Cisco','modem cisco','Linksys MR9000','https://www.cisco.com/c/es_es/products/routers/index.html',19,2,1),(2,'Huawei','modem huawei','D-Link DWR-953V2','https://consumer.huawei.com/pe/routers/',20,1,1),(3,'Cisco','switch cisco','Catalyst 2960','https://www.cisco.com/site/us/en/products/networking/switches/index.html',21,2,1),(4,'Huawei','switch huawei','Networks EX4300','https://e.huawei.com/es/products/enterprise-networking/switches',22,2,1),(5,'LG','panel lg','NeON 2','https://www.lg.com/us/business/solar',23,3,1),(6,'SunPower','panel sunpower','X-Series','https://us.sunpower.com/products/solar-panels',24,3,1),(7,'Emerson','rectificador emerson','NetSure 8100','https://es.made-in-china.com/co_uniquemande/product_Emerson-R48-3500e-Rectifier-Module-Communication-Module_ryynrgrig.html',25,4,1),(8,'Delta Electronics','rectificador delta','DPR 3000','https://www.deltapowersolutions.com/es-co/',26,4,1),(9,'Hoffman ','gabinete hoffman','PROLINE','https://hoffman-latam.com/',27,5,1),(10,'APC','gabinete APC','NetShelter SX','https://www.apc.com/pe/es/product-subcategory/88954-racks-y-anaqueles/',28,5,1),(11,'Nokia','bbu nokia','AirScale Baseband Unit','https://es.aliexpress.com/item/1005004527394444.html',29,6,1),(12,'Huawei','bbu huawei','Baseband Unit','https://e.huawei.com/es/products/wireless/gsm-r/radio-access-network/dbs3900',30,6,1),(13,'Ericsson ','rru ericsson','Radio 2205 RRU','https://www.ericsson.com/en/portfolio/networks/ericsson-radio-system',31,7,1),(14,'Huawei','rru huawei','Radio Remote Unit','https://e.huawei.com/es/products/wireless/gsm-r/radio-access-network/dbs3900',32,7,1);
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
  `hrelevantes` varchar(45) NOT NULL,
  `conexion` tinyint NOT NULL,
  `movilidad` tinyint NOT NULL,
  `nomredantario` varchar(45) NOT NULL,
  `dni` int NOT NULL,
  `area` float NOT NULL,
  `observaciones` varchar(45) NOT NULL,
  `construccion` tinyint NOT NULL,
  `instalacion` tinyint NOT NULL,
  `despliegue` tinyint NOT NULL,
  `trabarealizados` varchar(45) NOT NULL,
  PRIMARY KEY (`idFormularios`),
  KEY `fk_Formularios_Usuarios1_idx` (`idTecnico`),
  KEY `fk_Formularios_Imagenes1_idx` (`idImagenesForm`),
  KEY `fk_Formularios_Tickets1_idx` (`idTickets`),
  KEY `fk_formularios_tipoticket1_idx` (`idTipoTicket`),
  CONSTRAINT `fk_Formularios_Imagenes1` FOREIGN KEY (`idImagenesForm`) REFERENCES `archivos` (`idArchivos`),
  CONSTRAINT `fk_Formularios_Tickets1` FOREIGN KEY (`idTickets`) REFERENCES `tickets` (`idTickets`),
  CONSTRAINT `fk_formularios_tipoticket1` FOREIGN KEY (`idTipoTicket`) REFERENCES `tipoticket` (`idTipoTicket`),
  CONSTRAINT `fk_Formularios_Usuarios1` FOREIGN KEY (`idTecnico`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formularios`
--

LOCK TABLES `formularios` WRITE;
/*!40000 ALTER TABLE `formularios` DISABLE KEYS */;
INSERT INTO `formularios` VALUES (1,'2023-09-10 05:30:00','Correcto',1,8,33,1,2,'Se instalo correctamente',1,1,'Tulio Rodriguez',72986654,25,'Falta',1,0,1,'Reparacion'),(2,'2023-09-10 05:30:00','Correcto',1,13,33,2,2,'Se realizo buen clabeado',0,0,'Angie Mariaca',74869953,32,'Falla en trabajo',0,0,0,'Cableado'),(3,'2023-09-10 05:30:00','Aceptable',1,14,33,3,2,'Hubo problemas',0,1,'Einer Fuster',851236695,21,'Realizar de nuevo',1,1,1,'Programacion'),(4,'2023-09-10 05:30:00','Realizado',1,15,33,4,2,'Se instalo correctamente',1,1,'Rosa Rodriguez',42589663,39,'Volver en x meses',0,1,1,'Cableado'),(5,'2023-09-10 05:30:00','Aceptable',1,16,33,5,2,'Se realizo buen clabeado',0,1,'Victor Tasayco',41258896,40,'No se pudo resolver problema',1,0,0,'Reparacion'),(6,'2023-09-10 06:30:00','En curso',1,7,33,6,2,'Hubo problemas',1,0,'Natali Gonzales',42587733,45,'Realizar de nuevo',0,1,1,'Cableado'),(7,'2023-09-10 06:30:00','En curso',1,9,33,7,2,'Se instalo correctamente',1,1,'Alexandra Dagnino',25478836,42,'Falta',1,0,0,'Cableado'),(8,'2023-09-10 06:30:00','Por terminar',1,10,33,8,2,'Hubo problemas',1,0,'Salvador Salazar',25486697,36,'Volver en x meses',1,1,1,'Programacion'),(9,'2023-09-10 06:30:00','Aceptable',1,11,33,9,2,'Se realizo buen clabeado',0,1,'Katy Masias',21470058,58,'No se pudo resolver problema',1,0,1,'Reparacion'),(10,'2023-09-10 06:30:00','Inconcluso',1,12,33,10,2,'Se instalo correctamente',0,0,'Rodrigo Palma',48259966,96,'Falla en trabajo',0,1,0,'Programacion'),(11,'2023-09-10 06:30:00','Por terminar',1,16,33,11,1,'Se realizo buen clabeado',1,1,'Manuel Zambrano',32658899,52,'No se pudo resolver problema',1,1,1,'Programacion'),(12,'2023-09-10 06:30:00','Aceptable',1,15,33,12,2,'Hubo problemas',0,0,'Pablo Masco',14725896,47,'Realizar de nuevo',1,1,1,'Cableado'),(13,'2023-09-10 06:30:00','Por terminar',1,13,33,13,1,'Se instalo correctamente',1,1,'Nallely Tasayco',12548863,72,'Falla en trabajo',0,0,0,'Reparacion'),(14,'2023-09-10 06:30:00','Inconcluso',1,9,33,14,1,'Hubo problemas',1,1,'Maria Lizano',24578866,31,'No se pudo resolver problema',0,1,0,'Programacion'),(15,'2023-09-10 06:30:00','Aceptable',1,7,33,15,1,'Se realizo buen clabeado',0,0,'Samira Yataco',25478866,63,'Volver en x meses',1,0,1,'Reparacion'),(16,'2023-09-10 06:30:00','Aceptable',1,11,33,16,1,'Se instalo correctamente',1,1,'Axel Guzman',23584411,28,'Falta',0,1,0,'Reparacion'),(17,'2023-09-10 06:30:00','En curso',1,14,33,17,1,'Hubo problemas',1,0,'Daniel Carpio',26587744,29,'Falla en trabajo',1,0,1,'Cableado'),(18,'2023-09-10 06:30:00','Aceptable',1,8,33,18,1,'Se instalo correctamente',0,1,'Fernando Levano',26589933,34,'Realizar de nuevo',0,1,0,'Programacion'),(19,'2023-09-10 06:30:00','Inconcluso',1,12,33,19,1,'Se realizo buen clabeado',0,1,'Ana Diaz',25478861,64,'Falta',1,1,0,'Reparacion');
/*!40000 ALTER TABLE `formularios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historialtickets`
--

DROP TABLE IF EXISTS `historialtickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historialtickets` (
  `idhistorialTickets` int NOT NULL AUTO_INCREMENT,
  `estado` int NOT NULL,
  `fechaCambioEstado` datetime NOT NULL,
  `idTickets` int NOT NULL,
  `idUsuarios` int NOT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `idUsuariosReasignados` int DEFAULT NULL,
  PRIMARY KEY (`idhistorialTickets`),
  KEY `fk_historialTickets_tickets_idx` (`idTickets`),
  KEY `fk_historialtickets_usuarios1_idx` (`idUsuarios`),
  KEY `fk_historialtickets_usuarios2_idx` (`idUsuariosReasignados`),
  CONSTRAINT `fk_historialTickets_tickets` FOREIGN KEY (`idTickets`) REFERENCES `tickets` (`idTickets`),
  CONSTRAINT `fk_historialtickets_usuarios1` FOREIGN KEY (`idUsuarios`) REFERENCES `usuarios` (`idUsuarios`),
  CONSTRAINT `fk_historialtickets_usuarios2` FOREIGN KEY (`idUsuariosReasignados`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historialtickets`
--

LOCK TABLES `historialtickets` WRITE;
/*!40000 ALTER TABLE `historialtickets` DISABLE KEYS */;
INSERT INTO `historialtickets` VALUES (1,1,'2023-09-10 00:00:00',1,4,'Ticket Creado',NULL),(2,2,'2023-09-10 01:00:00',1,6,'Supervisor Asignado',NULL),(3,2,'2023-09-10 02:00:00',1,6,'Pasando a Tecnico',NULL),(4,3,'2023-09-10 03:00:00',1,8,'bien',NULL),(5,4,'2023-09-10 04:00:00',1,8,'bien',NULL),(6,5,'2023-09-10 05:00:00',1,8,'bien',NULL),(7,6,'2023-09-10 06:00:00',1,6,'Pasando a Analista',NULL),(9,1,'2023-09-10 01:00:00',2,4,'Ticket Creado',NULL),(10,2,'2023-09-10 02:00:00',2,5,'Supervisor Asignado',NULL),(11,2,'2023-09-10 03:00:00',2,5,'Pasando a Tecnico',NULL),(12,3,'2023-09-10 04:00:00',2,7,'si',NULL),(13,4,'2023-09-10 05:00:00',2,7,'bien',NULL),(14,5,'2023-09-10 06:00:00',2,7,'bien',NULL),(15,1,'2023-09-10 02:00:00',3,3,'Ticket Creado',NULL),(16,2,'2023-09-10 03:00:00',3,6,'Supervisor Asignado',NULL),(17,2,'2023-09-10 04:00:00',3,6,'Pasando a Tecnico',NULL),(18,3,'2023-09-10 05:00:00',3,8,'bien',NULL),(19,4,'2023-09-10 06:00:00',3,8,'bien',NULL),(20,1,'2023-09-10 03:00:00',4,3,'Ticket Creado',NULL),(21,2,'2023-09-10 04:00:00',4,5,'Supervisor Asignado',NULL),(22,2,'2023-09-10 05:00:00',4,5,'Pasando a Tecnico',NULL),(23,3,'2023-09-10 06:00:00',4,7,'tal',NULL),(24,1,'2023-09-10 04:00:00',5,4,'Ticket Creado',NULL),(25,2,'2023-09-10 05:00:00',5,5,'Supervisor Asignado',NULL),(26,1,'2023-09-10 05:00:00',6,3,'Ticket Creado',NULL),(27,1,'2023-09-10 05:00:00',7,4,'Ticket Creado',NULL),(28,1,'2023-09-10 05:00:00',8,4,'Ticket Creado',NULL),(29,2,'2023-09-10 06:00:00',8,6,'Supervisor Asignado',NULL),(30,1,'2023-09-10 05:00:00',9,3,'Ticket Creado',NULL),(31,2,'2023-09-10 06:00:00',9,6,'Supervisor Asignado',NULL),(32,2,'2023-09-10 07:00:00',9,6,'Pasando a Tecnico',NULL),(33,3,'2023-09-10 08:00:00',9,8,'bien',NULL),(34,4,'2023-09-10 09:00:00',9,8,'bien',NULL),(35,1,'2023-09-10 05:00:00',10,4,'Ticket Creado',NULL),(36,2,'2023-09-10 06:00:00',10,5,'Supervisor Asignado',NULL),(37,2,'2023-09-10 07:00:00',10,5,'Pasando a Tecnico',NULL),(38,3,'2023-09-10 08:00:00',10,7,'si',NULL),(39,4,'2023-09-10 09:00:00',10,7,'bien',NULL),(40,5,'2023-09-10 10:00:00',10,7,'bien',NULL),(41,1,'2023-09-10 05:00:00',11,3,'Ticket Creado',NULL),(42,2,'2023-09-10 06:00:00',11,6,'Supervisor Asignado',NULL),(43,2,'2023-09-10 07:00:00',11,6,'Pasando a Tecnico',NULL),(44,3,'2023-09-10 08:00:00',11,8,'bien',NULL),(45,4,'2023-09-10 09:00:00',11,8,'bien',NULL),(46,5,'2023-09-10 10:00:00',11,8,'bien',NULL),(47,6,'2023-09-10 11:00:00',11,6,'Pasando a Analista',NULL),(48,1,'2023-09-10 05:00:00',12,4,'Ticket Creado',NULL),(49,2,'2023-09-10 06:00:00',12,5,'Supervisor Asignado',NULL),(50,2,'2023-09-10 07:00:00',12,5,'Pasando a Tecnico',NULL),(51,1,'2023-09-10 05:00:00',13,3,'Ticket Creado',NULL),(52,2,'2023-09-10 06:00:00',13,5,'Supervisor Asignado',NULL),(53,2,'2023-09-10 07:00:00',13,5,'Pasando a Tecnico',NULL),(54,3,'2023-09-10 08:00:00',13,8,'tal',NULL),(55,1,'2023-09-10 05:00:00',14,4,'Ticket Creado',NULL),(56,2,'2023-09-10 06:00:00',14,5,'Supervisor Asignado',NULL),(57,2,'2023-09-10 07:00:00',14,5,'Pasando a Tecnico',NULL),(58,3,'2023-09-10 08:00:00',14,7,'tal',NULL),(59,1,'2023-09-10 05:00:00',15,3,'Ticket Creado',NULL),(60,2,'2023-09-10 06:00:00',15,5,'Supervisor Asignado',NULL),(61,2,'2023-09-10 07:00:00',15,5,'Pasando a Tecnico',NULL),(62,3,'2023-09-10 08:00:00',15,7,'bien',NULL),(63,4,'2023-09-10 09:00:00',15,7,'bien',NULL),(64,1,'2023-09-10 05:00:00',16,3,'Ticket Creado',NULL),(65,2,'2023-09-10 06:00:00',16,6,'Supervisor Asignado',NULL),(66,2,'2023-09-10 07:00:00',16,6,'Pasando a Tecnico',NULL),(67,3,'2023-09-10 08:00:00',16,8,'bien',NULL),(68,4,'2023-09-10 09:00:00',16,8,'bien',NULL),(69,5,'2023-09-10 10:00:00',16,8,'bien',NULL),(70,6,'2023-09-10 11:00:00',16,6,'Pasando a Analista',NULL),(71,1,'2023-11-14 00:00:00',17,3,'Ticket Creado',NULL),(72,2,'2023-11-14 01:00:00',17,6,'Supervisor Asignado',NULL),(73,2,'2023-11-14 02:00:00',17,6,'Pasando a Tecnico',NULL),(74,1,'2023-09-10 05:00:00',18,4,'Ticket Creado',NULL),(75,2,'2023-09-10 06:00:00',18,5,'Supervisor Asignado',NULL),(76,2,'2023-09-10 07:00:00',18,5,'Pasando a Tecnico',NULL),(77,1,'2023-09-10 05:00:00',19,3,'Ticket Creado',NULL),(78,2,'2023-09-10 06:00:00',19,5,'Supervisor Asignado',NULL);
/*!40000 ALTER TABLE `historialtickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sitiocerrado`
--

DROP TABLE IF EXISTS `sitiocerrado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sitiocerrado` (
  `idsitioCerrado` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(45) NOT NULL,
  `provincia` varchar(45) NOT NULL,
  `distrito` varchar(45) NOT NULL,
  `ubigeo` int NOT NULL,
  `departamento` varchar(45) NOT NULL,
  `latitud` decimal(10,7) NOT NULL,
  `longitud` decimal(10,7) NOT NULL,
  `habilitado` tinyint(1) NOT NULL,
  `tipoZona` varchar(45) DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idsitioCerrado`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sitiocerrado`
--

LOCK TABLES `sitiocerrado` WRITE;
/*!40000 ALTER TABLE `sitiocerrado` DISABLE KEYS */;
INSERT INTO `sitiocerrado` VALUES (1,'Fijo','Tayacaja','Salcabamba',90714,'Huancavelica',-12.2681124,-74.8087287,1,'Rural','Salcabamba'),(2,'Movil','Huanta','Sivia',50407,'Ayacucho',-12.5082786,-73.8625876,1,'Rural','Hospital Sivia'),(3,'Fijo','Huamanga','Quinua',50108,'Ayacucho',-13.0488739,-74.1393247,1,'Rural','Cristo'),(4,'Movil','Puerto Inca','Puerto Inca',100901,'Huanuco',-9.3793225,-74.9659271,1,'Rural','Plaza de Armas'),(5,'Fijo','Huaraz','Huaraz',20101,'Ancash',-9.5298839,-77.5288857,1,'Rural','Colegio de Huanuco'),(6,'Movil','Aija','La Merced',20204,'Ancash',-9.7348069,-77.6147490,1,'Rural','Puesto de Salud'),(7,'Fijo','Arequipa','Cayma',40103,'Arequipa',-16.3341539,-71.5470543,1,'Rural','Enace');
/*!40000 ALTER TABLE `sitiocerrado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sitios`
--

DROP TABLE IF EXISTS `sitios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sitios` (
  `idSitios` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(45) NOT NULL,
  `provincia` varchar(45) NOT NULL,
  `distrito` varchar(45) NOT NULL,
  `ubigeo` int NOT NULL,
  `departamento` varchar(45) NOT NULL,
  `latitud` decimal(10,7) NOT NULL,
  `longitud` decimal(10,7) NOT NULL,
  `habilitado` tinyint(1) NOT NULL,
  `tipoZona` varchar(45) NOT NULL,
  `idArchivos` int DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idSitios`),
  KEY `fk_sitios_archivos1_idx` (`idArchivos`),
  CONSTRAINT `fk_sitios_archivos1` FOREIGN KEY (`idArchivos`) REFERENCES `archivos` (`idArchivos`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sitios`
--

LOCK TABLES `sitios` WRITE;
/*!40000 ALTER TABLE `sitios` DISABLE KEYS */;
INSERT INTO `sitios` VALUES (1,'Fijo','Lima','San Miguel',140126,'Lima',-12.0690694,-77.0786442,0,'Urbana',64,'LA PUCP '),(2,'Movil','Lima','Lima',140101,'Lima',-12.0460063,-77.0303511,1,'Rural',65,'Centro de Lima '),(9,'Movil','Cusco','Cusco',70101,'Cusco',-13.5167110,-71.9788230,1,'Urbana',66,'Plaza de Armas Cusco'),(10,'Fijo','Cusco','Cusco',70101,'Cusco',-13.5077780,-71.9822220,1,'Rural',67,'Saqsaywaman'),(11,'Movil','Lima','Miraflores',140115,'Lima',-12.1219000,-77.0297000,1,'Urbana',68,'Parque Kennedy'),(12,'Fijo','Lima','Lima',123465,'San Miguel',-12.0772001,-77.0825000,1,'Urbana',69,'Plaza San Miguel ');
/*!40000 ALTER TABLE `sitios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sitios_has_equipos`
--

DROP TABLE IF EXISTS `sitios_has_equipos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sitios_has_equipos` (
  `idSitios` int NOT NULL,
  `idEquipos` int NOT NULL,
  PRIMARY KEY (`idSitios`,`idEquipos`),
  KEY `fk_sitios_has_equipos_equipos1_idx` (`idEquipos`),
  KEY `fk_sitios_has_equipos_sitios1_idx` (`idSitios`),
  CONSTRAINT `fk_sitios_has_equipos_equipos1` FOREIGN KEY (`idEquipos`) REFERENCES `equipos` (`idEquipos`),
  CONSTRAINT `fk_sitios_has_equipos_sitios1` FOREIGN KEY (`idSitios`) REFERENCES `sitios` (`idSitios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sitios_has_equipos`
--

LOCK TABLES `sitios_has_equipos` WRITE;
/*!40000 ALTER TABLE `sitios_has_equipos` DISABLE KEYS */;
INSERT INTO `sitios_has_equipos` VALUES (2,2),(9,2),(2,4),(2,6),(2,8),(2,10),(2,12),(2,14);
/*!40000 ALTER TABLE `sitios_has_equipos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session`
--

DROP TABLE IF EXISTS `spring_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session`
--

LOCK TABLES `spring_session` WRITE;
/*!40000 ALTER TABLE `spring_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `spring_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session_attributes`
--

DROP TABLE IF EXISTS `spring_session_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session_attributes`
--

LOCK TABLES `spring_session_attributes` WRITE;
/*!40000 ALTER TABLE `spring_session_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `spring_session_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tecnicoscuadrillas`
--

DROP TABLE IF EXISTS `tecnicoscuadrillas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tecnicoscuadrillas` (
  `idAsignacion` int NOT NULL AUTO_INCREMENT,
  `idTecnico` int NOT NULL,
  `idCuadrilla` int NOT NULL,
  `liderTecnico` int NOT NULL,
  PRIMARY KEY (`idAsignacion`),
  KEY `fk_AsignacionTecnicosCuadrillas_Usuarios` (`idTecnico`),
  KEY `fk_AsignacionTecnicosCuadrillas_Cuadrillas` (`idCuadrilla`),
  CONSTRAINT `fk_AsignacionTecnicosCuadrillas_Cuadrillas` FOREIGN KEY (`idCuadrilla`) REFERENCES `cuadrillas` (`idCuadrillas`),
  CONSTRAINT `fk_AsignacionTecnicosCuadrillas_Usuarios` FOREIGN KEY (`idTecnico`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tecnicoscuadrillas`
--

LOCK TABLES `tecnicoscuadrillas` WRITE;
/*!40000 ALTER TABLE `tecnicoscuadrillas` DISABLE KEYS */;
INSERT INTO `tecnicoscuadrillas` VALUES (1,7,1,1),(2,9,1,0),(3,11,1,0),(4,12,1,0),(5,17,1,0),(6,8,2,1),(7,13,2,0),(8,14,2,0),(9,15,2,0),(10,16,2,0);
/*!40000 ALTER TABLE `tecnicoscuadrillas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tecnologiainstalada`
--

DROP TABLE IF EXISTS `tecnologiainstalada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tecnologiainstalada` (
  `idTecnologiaInstalada` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`idTecnologiaInstalada`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tecnologiainstalada`
--

LOCK TABLES `tecnologiainstalada` WRITE;
/*!40000 ALTER TABLE `tecnologiainstalada` DISABLE KEYS */;
/*!40000 ALTER TABLE `tecnologiainstalada` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tecnologiainstalada_formularios`
--

DROP TABLE IF EXISTS `tecnologiainstalada_formularios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tecnologiainstalada_formularios` (
  `idTecnologiaInstalada` int NOT NULL,
  `idFormularios` int NOT NULL,
  PRIMARY KEY (`idTecnologiaInstalada`,`idFormularios`),
  KEY `idFormularios` (`idFormularios`),
  CONSTRAINT `tecnologiainstalada_formularios_ibfk_1` FOREIGN KEY (`idTecnologiaInstalada`) REFERENCES `tecnologiainstalada` (`idTecnologiaInstalada`),
  CONSTRAINT `tecnologiainstalada_formularios_ibfk_2` FOREIGN KEY (`idFormularios`) REFERENCES `formularios` (`idFormularios`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tecnologiainstalada_formularios`
--

LOCK TABLES `tecnologiainstalada_formularios` WRITE;
/*!40000 ALTER TABLE `tecnologiainstalada_formularios` DISABLE KEYS */;
/*!40000 ALTER TABLE `tecnologiainstalada_formularios` ENABLE KEYS */;
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
  `idSupervisorEncargado` int DEFAULT NULL,
  `idUsuarioCreador` int NOT NULL,
  `idEmpresaAsignada` int NOT NULL,
  `idCuadrilla` int DEFAULT NULL,
  `idSitios` int NOT NULL,
  `idTipoTicket` int NOT NULL,
  `prioridad` varchar(45) DEFAULT NULL,
  `idsitioCerrado` int NOT NULL,
  `reasignado` int DEFAULT NULL,
  PRIMARY KEY (`idTickets`),
  KEY `fk_Tickets_Usuarios1_idx` (`idSupervisorEncargado`),
  KEY `fk_Tickets_Usuarios2_idx` (`idUsuarioCreador`),
  KEY `fk_Tickets_Empresas1_idx` (`idEmpresaAsignada`),
  KEY `fk_Tickets_Cuadrillas1_idx` (`idCuadrilla`),
  KEY `fk_Tickets_Sitios1_idx` (`idSitios`),
  KEY `fk_tickets_tipoticket1_idx` (`idTipoTicket`),
  KEY `idx_tickets_idsitiosCerrados` (`idsitioCerrado`),
  CONSTRAINT `fk_Tickets_Cuadrillas1` FOREIGN KEY (`idCuadrilla`) REFERENCES `cuadrillas` (`idCuadrillas`),
  CONSTRAINT `fk_Tickets_Empresas1` FOREIGN KEY (`idEmpresaAsignada`) REFERENCES `empresas` (`idEmpresas`),
  CONSTRAINT `fk_Tickets_Sitios1` FOREIGN KEY (`idSitios`) REFERENCES `sitios` (`idSitios`),
  CONSTRAINT `fk_tickets_tipoticket1` FOREIGN KEY (`idTipoTicket`) REFERENCES `tipoticket` (`idTipoTicket`),
  CONSTRAINT `fk_Tickets_Usuarios1` FOREIGN KEY (`idSupervisorEncargado`) REFERENCES `usuarios` (`idUsuarios`),
  CONSTRAINT `fk_Tickets_Usuarios2` FOREIGN KEY (`idUsuarioCreador`) REFERENCES `usuarios` (`idUsuarios`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
-- 

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (1,'Falla de panel en San Miguel',7,'2023-09-10 00:00:00','2023-09-15 00:00:00','Santiago Martinez',6,4,3,2,1,2,'Baja prioridad',1,0),(2,'Falla bateria Lima',6,'2023-09-10 01:00:00','2023-09-15 01:00:00','Valentina Rodriguez',5,4,2,1,2,2,'Hacer',2,0),(3,'Mantenimiento preventivo San Miguel',5,'2023-09-10 02:00:00','2023-09-15 02:00:00','Mateo Lopez',6,3,2,2,1,1,'No critico',3,0),(4,'Mantenimiento baterias Lima',4,'2023-09-10 03:00:00','2023-09-15 03:00:00','Leonardo Perez',5,3,3,1,2,1,'Baja prioridad',4,0),(5,'Corte de energia Lima',2,'2023-09-10 04:00:00','2023-09-15 04:00:00','Sofia Gomez',5,4,2,NULL,2,2,'Hacer',5,0),(6,'Mantenimiento baterias San Miguel',1,'2023-09-10 05:00:00','2023-09-15 05:00:00','Leonardo Perez',NULL,3,2,NULL,1,1,'Urgente',6,0),(7,'Falla bateria Lima',1,'2023-09-10 05:00:00','2023-09-15 05:00:00','Mariana Mar',NULL,4,3,NULL,1,2,'Baja prioridad',7,0),(8,'Mantenimiento preventivo San Miguel',2,'2023-09-10 05:00:00','2023-09-15 05:00:00','Santiago Lopez',6,4,2,NULL,1,2,'Urgente',7,0),(9,'Corte de energia Lima',5,'2023-09-10 05:00:00','2023-09-15 05:00:00','Piero Diaz',6,3,3,2,2,1,'No critico',6,0),(10,'Falla bateria Lima',6,'2023-09-10 05:00:00','2023-09-15 05:00:00','Micaela Fuentes',5,4,3,1,2,2,'Baja prioridad',5,0),(11,'Mantenimiento baterias San Miguel',7,'2023-09-10 05:00:00','2023-09-15 05:00:00','Roxana Atauje',6,3,2,2,2,2,'Hacer',4,0),(12,'Corte de energia Lima',3,'2023-09-10 05:00:00','2023-09-15 05:00:00','Lucia Rivera',5,4,3,2,1,1,'Hacer',3,0),(13,'Mantenimiento baterias San Miguel',4,'2023-09-10 05:00:00','2023-09-15 05:00:00','Brian Zambrano',5,3,3,2,2,2,'No critico',1,0),(14,'Mantenimiento baterias San Miguel',4,'2023-09-10 05:00:00','2023-09-15 05:00:00','Fernando Casas',5,4,3,1,1,2,'Urgente',2,0),(15,'Corte de energia Lima',5,'2023-09-10 05:00:00','2023-09-15 05:00:00','Daniel Moran',5,3,2,1,2,1,'No critico',4,0),(16,'Mantenimiento preventivo San Miguel',7,'2023-09-10 05:00:00','2023-09-15 05:00:00','Diego Jaime',6,3,3,2,1,2,'Urgente',5,0),(17,'Falla bateria Lima',3,'2023-11-14 00:00:00','2023-09-15 05:00:00','Rosa Benavides DXX',6,3,2,1,2,1,'Hacer',3,0),(18,'Corte de energia Lima',3,'2023-09-10 05:00:00','2023-09-15 05:00:00','Hector Lizano',5,4,3,2,1,2,'Urgente',7,0),(19,'Mantenimiento preventivo San Miguel',2,'2023-09-10 05:00:00','2023-09-15 05:00:00','Patricia Yataco',5,3,2,NULL,2,2,'Baja prioridad',1,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoequipo`
--

LOCK TABLES `tipoequipo` WRITE;
/*!40000 ALTER TABLE `tipoequipo` DISABLE KEYS */;
INSERT INTO `tipoequipo` VALUES (1,'Router'),(2,'Switch'),(3,'Panel de energia'),(4,'Rectificador'),(5,'Gabinete'),(6,'BBU'),(7,'RRU');
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
INSERT INTO `tipoticket` VALUES (1,'Mantenimiento'),(2,'Despliegue');
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
  `contrasenia` varchar(100) DEFAULT NULL,
  `habilitado` tinyint(1) DEFAULT NULL,
  `fechaRegistro` date DEFAULT NULL,
  `tecnicoConCuadrilla` tinyint(1) DEFAULT NULL,
  `idCargos` int NOT NULL,
  `idEmpresas` int NOT NULL,
  `idImagenPerfil` int DEFAULT NULL,
  `dni` varchar(8) DEFAULT NULL,
  `descripcion` varchar(130) DEFAULT NULL,
  PRIMARY KEY (`idUsuarios`),
  KEY `fk_Usuarios_Cargos_idx` (`idCargos`),
  KEY `fk_Usuarios_Empresas1_idx` (`idEmpresas`),
  KEY `fk_Usuarios_Imagenes1_idx` (`idImagenPerfil`),
  CONSTRAINT `fk_Usuarios_Cargos` FOREIGN KEY (`idCargos`) REFERENCES `cargos` (`idCargos`),
  CONSTRAINT `fk_Usuarios_Empresas1` FOREIGN KEY (`idEmpresas`) REFERENCES `empresas` (`idEmpresas`),
  CONSTRAINT `fk_Usuarios_Imagenes1` FOREIGN KEY (`idImagenPerfil`) REFERENCES `archivos` (`idArchivos`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Julio Hiro ','Fibra Toxica','superadmin@gmail.com','$2a$04$qTEnOW5QHh/epwJMQ/rfg.ry51RIsMhQ0LS.gzWr7jxb4xhhfcMIq',1,'2022-09-10',0,1,1,1,'17771230','Soy superadmin'),(2,'Juan','Williams','administrador@gmail.com','$2a$04$M.wxSUIZQGh9XTJZBoXwcepRTTbjBXcBgIIela4qbFYN0Vw7OnQpe',1,'2022-09-11',0,2,1,2,'47791231','Soy admin'),(3,'David','Andersson','analistam@gmail.com','$2a$04$gqvQjLgf3jts89rBeSI9HOLm.fizkuqZzOoy3.lJ8b0zLILnX8zO.',1,'2022-09-12',0,3,3,3,'27971234','Soy analista'),(4,'Robert ','Harris','analistad@gmail.com','$2a$04$gqvQjLgf3jts89rBeSI9HOLm.fizkuqZzOoy3.lJ8b0zLILnX8zO.',1,'2022-09-13',0,4,1,4,'74771235','Soy analista'),(5,'William ','Martin','supervisor1@gmail.com','$2a$12$efzEnOR5ybTd5QNibY8Uq.MVv.ddqmqeb1BllenazgAttR9S2Fh22',1,'2022-09-14',0,5,2,5,'27471236','Soy supevisor'),(6,'Daniel ','Mitchell','supervisor2@gmail.com','$2a$12$efzEnOR5ybTd5QNibY8Uq.MVv.ddqmqeb1BllenazgAttR9S2Fh22',1,'2022-09-15',0,5,3,6,'47071237','Soy supevisor'),(7,'James ','Brown','jefetecnico1@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-16',1,6,2,7,'37771238','Soy jefetecnico'),(8,'Joseph ','Wilson','jefetecnico2@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-17',1,6,3,8,'77771239','Soy jefetecnico'),(9,'Christopher ','Jackson','tecnico1@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-18',1,6,2,9,'38110050','Soy tecnico1'),(10,'Andrew ','Davis','tecnico2@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-19',0,6,2,10,'98110051','Soy tecnico2'),(11,'Benjamin ','White','tecnico3@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-20',1,6,2,11,'28110052','Soy tecnico3'),(12,'Richard ','Clark','tecnico4@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-21',1,6,2,12,'80110053','Soy tecnico4'),(13,'Charles ','Turner','tecnico5@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-22',1,6,3,13,'18110054','Soy tecnico5'),(14,'Thomas ','Nelson','tecnico6@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-23',1,6,3,14,'88110055','Soy tecnico6'),(15,'Steven ','Carter','tecnico7@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-24',1,6,3,15,'22345610','Soy tecnico7'),(16,'Brian ','Smith','tecnico8@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-25',1,6,3,16,'20345612','Soy tecnico8'),(17,'Patrick ','Lewis','tecnico9@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-26',1,6,2,17,'23456185','Soy tecnico9'),(18,'Matthew ','Johnson','tecnico10@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-27',0,6,3,18,'20345617','Soy tecnico10'),(19,'Marcelo ','Gonzales','supervisor3@gmail.com','$2a$12$efzEnOR5ybTd5QNibY8Uq.MVv.ddqmqeb1BllenazgAttR9S2Fh22',1,'2022-08-14',0,5,2,33,'54124248','Soy supevisor'),(20,'Fernando ','Mendoza','supervisor4@gmail.com','$2a$12$efzEnOR5ybTd5QNibY8Uq.MVv.ddqmqeb1BllenazgAttR9S2Fh22',1,'2022-08-14',0,5,2,34,'41043953','Soy supevisor'),(21,'Marcelo ','Gonzales','supervisor5@gmail.com','$2a$12$efzEnOR5ybTd5QNibY8Uq.MVv.ddqmqeb1BllenazgAttR9S2Fh22',1,'2022-07-14',0,5,3,35,'15783218','Soy supevisor'),(22,'Mateo ','Bendezu','supervisor6@gmail.com','$2a$12$efzEnOR5ybTd5QNibY8Uq.MVv.ddqmqeb1BllenazgAttR9S2Fh22',1,'2022-07-14',0,5,3,36,'50420389','Soy supevisor'),(23,'Augusto ','Ayala','supervisor7@gmail.com','$2a$12$efzEnOR5ybTd5QNibY8Uq.MVv.ddqmqeb1BllenazgAttR9S2Fh22',1,'2022-07-14',0,5,2,37,'10248036','Soy supevisor'),(24,'Marco ','Torres','supervisor8@gmail.com','$2a$12$efzEnOR5ybTd5QNibY8Uq.MVv.ddqmqeb1BllenazgAttR9S2Fh22',1,'2022-06-14',0,5,2,38,'23056070','Soy supevisor'),(25,'Angel ','Grados','tecnico11@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-23',0,6,3,39,'10245780','Soy tecnico11'),(26,'Alex ','Niera','tecnico12@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-09-23',0,6,2,40,'20332100','Soy tecnico12'),(27,'Brown ','Jones','tecnico13@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,3,41,'20147801','Soy tecnico13'),(28,'Davis ','Miller','tecnico14@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,2,42,'20001697','Soy tecnico14'),(29,'Wilson ','Lopez','tecnico15@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,3,43,'65700001','Soy tecnico15'),(30,'Octavio ','Foelster','tecnico16@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,2,44,'29887450','Soy tecnico16'),(31,'German ','Murphy','tecnico17@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-06-23',0,6,2,45,'07895610','Soy tecnico17'),(32,'Julio ','Brock','tecnico12@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-06-23',0,6,2,46,'23888850','Soy tecnico18'),(33,'Cameron ','Diaz','tecnico13@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-06-23',0,6,3,47,'69877771','Soy tecnico19'),(34,'Luis ','Miller','tecnico14@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-06-23',0,6,2,48,'67451277','Soy tecnico20'),(35,'Wilson ','Lamper','tecnico15@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-06-23',0,6,3,49,'21036899','Soy tecnico21'),(36,'Murphy ','Jones','tecnico16@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-06-23',0,6,2,50,'99987444','Soy tecnico22'),(37,'Isaac ','Richardson','tecnico17@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-06-23',0,6,3,51,'11120110','Soy tecnico23'),(38,'Christopher ','Parker','tecnico18@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,2,52,'21222555','Soy tecnico24'),(39,'Benjamin  ','Turner','tecnico19@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,3,53,'77848510','Soy tecnico25'),(40,'Nathan  ','Brooks','tecnico20@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,2,54,'10002310','Soy tecnico26'),(41,'Parker ','Reed','tecnico21@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,3,55,'00122397','Soy tecnico27'),(42,'Mason  ','Powell','tecnico22@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,2,56,'97979710','Soy tecnico28'),(43,'Liam  ','Griffin','tecnico23@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,3,57,'15151420','Soy tecnico29'),(44,'Ethan  ','Reed','tecnico24@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-07-23',0,6,2,58,'78373730','Soy tecnico30'),(45,'Brown ','Turner','tecnico25@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-08-23',0,6,2,59,'12063400','Soy tecnico31'),(46,'Owen ','Klopp','tecnico26@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-08-23',0,6,3,60,'69870002','Soy tecnico32'),(47,'Gavin  ','Bennett','tecnico27@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-08-23',0,6,2,61,'02123333','Soy tecnico33'),(48,'Bennett ','Reed','tecnico28@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-08-23',0,6,3,62,'98741110','Soy tecnico34'),(49,'Christopher ','Lamper','tecnico29@gmail.com','$2a$12$bFIezHyq8yQouRnOgpu9KeeI4KWQko7ZYcb9OPZNP3dqsAWG9L9TW',1,'2022-08-23',0,6,2,63,'02100023','Soy tecnico35');
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

-- Dump completed on 2023-11-15 21:59:02
