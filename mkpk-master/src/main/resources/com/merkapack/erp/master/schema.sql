CREATE TABLE `domain` (
	 `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico'
	,`name` varchar(64) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre del dominio'
	,`creation_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de creacion'
	,`creation_date` datetime DEFAULT NULL COMMENT 'Fecha de creacion'
	,`modification_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de modificacion'
	,`modification_date` datetime DEFAULT NULL COMMENT 'Fecha de modificacion'
	,PRIMARY KEY (`id`)
	,UNIQUE KEY `IDX_UNQ_DOMAIN_NAME` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Dominios';

CREATE TABLE `machine` (
	 `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico'
	,`domain` int(4) NOT NULL COMMENT 'Identificador del Dominio'
	,`name` varchar(32) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre de la maquina'
	,`creation_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de creacion'
	,`creation_date` datetime DEFAULT NULL COMMENT 'Fecha de creacion'
	,`modification_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de modificacion'
	,`modification_date` datetime DEFAULT NULL COMMENT 'Fecha de modificacion'
	,PRIMARY KEY (`id`)
	,KEY `IDX_MACHINE_DOMAIN` (`domain`)
	,CONSTRAINT `FK_MACHINE_DOMAIN` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`)	
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Maquinas';

CREATE TABLE `material` (
	 `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico del material'
	,`domain` int(4) NOT NULL COMMENT 'Identificador del Dominio'
	,`name` varchar(32) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre del material'
	,`thickness` double(6,4) DEFAULT '0.00' COMMENT 'Grosor del material'
	,`creation_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de creacion'
	,`creation_date` datetime DEFAULT NULL COMMENT 'Fecha de creacion'
	,`modification_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de modificacion'
	,`modification_date` datetime DEFAULT NULL COMMENT 'Fecha de modificacion'
	,PRIMARY KEY (`id`)
	,KEY `IDX_MATERIAL_DOMAIN` (`domain`)
	,CONSTRAINT `FK_MATERIAL_DOMAIN` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`)	
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Materiales';


CREATE TABLE `user` (
  `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico'
  ,`domain` int(4) NOT NULL COMMENT 'Identificador del Dominio'
  ,`name` varchar(64) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre del Usuario'
  ,`login` varchar(16) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Login del Usuario'
  ,`active` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Indica si el Usuario esta activo o no'
  ,`password` varchar(128) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Contraseña del Usuario'
  ,PRIMARY KEY (`id`)
  ,UNIQUE KEY `IDX_UNQ_USER_DOMAIN_LOGIN` (`login`)
  ,KEY `IDX_USER_DOMAIN` (`domain`)
  ,CONSTRAINT `FK_USER_DOMAIN` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Usuarios';

CREATE TABLE `db_version` (
  `version_number` varchar(10) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Numero de Version de la Base de Datos',
  PRIMARY KEY (`version_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Version de la Base de Datos';

INSERT INTO `db_version` (`version_number`) VALUES ('1.0.0');
