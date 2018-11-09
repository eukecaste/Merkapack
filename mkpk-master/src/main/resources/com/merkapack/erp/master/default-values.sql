-- MySQL dump 10.16  Distrib 10.2.18-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: merkapack
-- ------------------------------------------------------
-- Server version	10.2.18-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,1,'PACKAGING','default','2018-11-09 17:23:04',NULL,NULL),(2,1,'TOFER','default','2018-11-09 17:23:12',NULL,NULL),(3,1,'VAC','default','2018-11-09 17:23:19',NULL,NULL),(4,1,'SACOPISA','default','2018-11-09 17:23:26',NULL,NULL),(5,1,'PAST. CIES','default','2018-11-09 17:23:38',NULL,NULL);
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `db_version`
--

LOCK TABLES `db_version` WRITE;
/*!40000 ALTER TABLE `db_version` DISABLE KEYS */;
INSERT INTO `db_version` VALUES ('1.0.0');
/*!40000 ALTER TABLE `db_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `domain`
--

LOCK TABLES `domain` WRITE;
/*!40000 ALTER TABLE `domain` DISABLE KEYS */;
INSERT INTO `domain` VALUES (1,'Merkapack Packaging',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `domain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `machine`
--

LOCK TABLES `machine` WRITE;
/*!40000 ALTER TABLE `machine` DISABLE KEYS */;
INSERT INTO `machine` VALUES (1,1,'GRANDE',80.0000,'default','2018-11-09 16:39:02','default','2018-11-09 16:39:03'),(2,1,'VERDE',70.0000,'default','2018-11-09 16:39:06','default','2018-11-09 16:39:08'),(3,1,'ITALIANA',90.0000,'default','2018-11-09 16:39:21','default','2018-11-09 16:39:23');
/*!40000 ALTER TABLE `machine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `material`
--

LOCK TABLES `material` WRITE;
/*!40000 ALTER TABLE `material` DISABLE KEYS */;
INSERT INTO `material` VALUES (2,1,'15/65',0.6500,'default','2018-11-02 19:18:39','default','2018-11-09 16:39:41'),(3,1,'20/65',0.6500,'default','2018-11-02 19:18:58','default','2018-11-09 16:39:51'),(4,1,'PLATA',0.3000,'default','2018-11-02 19:19:46','default','2018-11-09 16:39:59'),(5,1,'20/100',0.5000,'default','2018-11-02 19:19:52','default','2018-11-09 16:52:00'),(6,1,'CPA90/CPPP100',0.0900,'default','2018-11-09 16:40:31','default','2018-11-09 16:55:34');
/*!40000 ALTER TABLE `material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `planning`
--

LOCK TABLES `planning` WRITE;
/*!40000 ALTER TABLE `planning` DISABLE KEYS */;
/*!40000 ALTER TABLE `planning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,1,'200x350',3,200.00,300.00,'default','2018-11-08 19:37:19','default','2018-11-09 16:46:21'),(2,1,'170x250',2,170.00,250.00,'default','2018-11-08 19:37:54','default','2018-11-09 16:46:51'),(3,1,'175x250',2,175.00,250.00,'default','2018-11-08 19:38:23','default','2018-11-09 16:47:08'),(4,1,'300x500',2,300.00,500.00,'default','2018-11-08 19:50:34','default','2018-11-09 16:47:28'),(7,1,'350x500',2,350.00,500.00,'default','2018-11-09 16:47:41','default','2018-11-09 16:47:46'),(8,1,'150x450',2,150.00,450.00,'default','2018-11-09 16:47:55','default','2018-11-09 16:48:00'),(9,1,'250x350',5,250.00,350.00,'default','2018-11-09 16:48:23','default','2018-11-09 16:52:35'),(10,1,'200x300',4,200.00,300.00,'default','2018-11-09 16:48:40','default','2018-11-09 16:48:47'),(11,1,'130x300',3,130.00,300.00,'default','2018-11-09 16:49:03','default','2018-11-09 16:49:08'),(12,1,'150x300',3,150.00,300.00,'default','2018-11-09 16:49:15','default','2018-11-09 16:49:18'),(13,1,'180x300',3,180.00,300.00,'default','2018-11-09 16:49:25','default','2018-11-09 16:49:29'),(14,1,'200x300',3,200.00,300.00,'default','2018-11-09 16:49:36','default','2018-11-09 16:49:39'),(15,1,'220x300',3,220.00,300.00,'default','2018-11-09 16:49:49','default','2018-11-09 16:49:52'),(16,1,'250x300',3,250.00,300.00,'default','2018-11-09 16:49:58','default','2018-11-09 16:50:01'),(17,1,'250x400',3,250.00,400.00,'default','2018-11-09 16:50:11','default','2018-11-09 16:50:14'),(18,1,'300x400',3,300.00,400.00,'default','2018-11-09 16:50:38','default','2018-11-09 16:50:42'),(19,1,'125x400',3,125.00,400.00,'default','2018-11-09 16:50:53','default','2018-11-09 16:51:19'),(20,1,'350x600',3,350.00,600.00,'default','2018-11-09 16:53:46','default','2018-11-09 16:53:49'),(21,1,'400x600',3,400.00,600.00,'default','2018-11-09 16:53:58','default','2018-11-09 16:54:01'),(22,1,'200x200',2,200.00,200.00,'default','2018-11-09 16:54:12','default','2018-11-09 16:54:16'),(23,1,'300x350',3,300.00,350.00,'default','2018-11-09 16:54:46','default','2018-11-09 16:54:50'),(24,1,'240x350',6,240.00,350.00,'default','2018-11-09 16:55:06','default','2018-11-09 16:55:19');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1,'Administrador','admin',1,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-09 17:26:05
