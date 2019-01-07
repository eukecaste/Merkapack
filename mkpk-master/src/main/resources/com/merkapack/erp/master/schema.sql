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
  	,`blows` double(6,2) DEFAULT '0.00' COMMENT 'Golpes por minuto'
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
	,`code` varchar(15) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Codigo del material'
	,`name` varchar(32) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre del material'
	,`raw_material` varchar(32) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Material de composicion'
	,`raw_composition` varchar(32) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Composicion'
	,`thickness` double(6,2) DEFAULT '0.00' COMMENT 'Grosor del material'
	,`creation_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de creacion'
	,`creation_date` datetime DEFAULT NULL COMMENT 'Fecha de creacion'
	,`modification_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de modificacion'
	,`modification_date` datetime DEFAULT NULL COMMENT 'Fecha de modificacion'
	,PRIMARY KEY (`id`)
	,KEY `IDX_MATERIAL_DOMAIN` (`domain`)
	,CONSTRAINT `FK_MATERIAL_DOMAIN` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`)	
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Materiales';

CREATE TABLE `roll` (
	 `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico de la bobina'
	,`domain` int(4) NOT NULL COMMENT 'Identificador del Dominio'
	,`material` int(4) NOT NULL COMMENT 'Identificador del material'
	,`name` varchar(32) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre de la bobina'
	,`width` double(8,2) DEFAULT '0.00' COMMENT 'Ancho bobina'
	,`length` double(8,2) DEFAULT '0.00' COMMENT 'Largo bobina'
	,`creation_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de creacion'
	,`creation_date` datetime DEFAULT NULL COMMENT 'Fecha de creacion'
	,`modification_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de modificacion'
	,`modification_date` datetime DEFAULT NULL COMMENT 'Fecha de modificacion'
	,PRIMARY KEY (`id`)
	,KEY `IDX_ROLL_DOMAIN` (`domain`)
	,CONSTRAINT `FK_ROLL_DOMAIN` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`)	
	,CONSTRAINT `FK_ROLL_MATERIAL` FOREIGN KEY (`material`) REFERENCES `material` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Bobinas';

CREATE TABLE `product` (
	 `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico del producto'
	,`domain` int(4) NOT NULL COMMENT 'Identificador del Dominio'
	,`code` varchar(15) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Codigo del producto'
	,`name` varchar(128) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre del producto'
	,`material_up` int(4) NOT NULL COMMENT 'Identificador del material superior'
	,`material_down` int(4) NOT NULL COMMENT 'Identificador del material inferior'
	,`width` double(8,2) DEFAULT '0.00' COMMENT 'Ancho'
	,`length` double(8,2) DEFAULT '0.00' COMMENT 'Largo'
	,`box_units` double(8,2) DEFAULT '0.00' COMMENT 'Unidades por caja'
	,`mold` varchar(15) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Molde'
	,`creation_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de creacion'
	,`creation_date` datetime DEFAULT NULL COMMENT 'Fecha de creacion'
	,`modification_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de modificacion'
	,`modification_date` datetime DEFAULT NULL COMMENT 'Fecha de modificacion'
	,PRIMARY KEY (`id`)
	,KEY `IDX_PRODUCT_DOMAIN` (`domain`)
	,KEY `IDX_PRODUCT_MATERIAL` (`domain`)
	,CONSTRAINT `FK_PRODUCT_DOMAIN` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`)	
	,CONSTRAINT `FK_PRODUCT_MATERIAL_UP` FOREIGN KEY (`material_up`) REFERENCES `material` (`id`)
	,CONSTRAINT `FK_PRODUCT_MATERIAL_DOWN` FOREIGN KEY (`material_down`) REFERENCES `material` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Productos';

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

CREATE TABLE `client` (
	 `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico'
	,`domain` int(4) NOT NULL COMMENT 'Identificador del Dominio'
	,`name` varchar(64) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Nombre del cliente'
	,`creation_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de creacion'
	,`creation_date` datetime DEFAULT NULL COMMENT 'Fecha de creacion'
	,`modification_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de modificacion'
	,`modification_date` datetime DEFAULT NULL COMMENT 'Fecha de modificacion'
	,PRIMARY KEY (`id`)
	,KEY `IDX_CLIENT_DOMAIN` (`domain`)
	,CONSTRAINT `FK_CLIENT_DOMAIN` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`)	
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Clientes';

CREATE TABLE `planning` (
	 `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'Identificador unico'
	,`domain` int(4) NOT NULL COMMENT 'Identificador del Dominio'
	,`date` date DEFAULT NULL COMMENT 'Fecha del plan'
	,`order` int(4) NOT NULL COMMENT 'Orden'
	,`machine` int(4) NOT NULL COMMENT 'Identificador de la maquina'
	,`product` int(4) NOT NULL COMMENT 'Identificador del producto'
	,`width` double(8,2) DEFAULT '0.00' COMMENT 'Ancho bolsa'
	,`length` double(8,2) DEFAULT '0.00' COMMENT 'Largo bolsa'
	,`material_up` int(4) NOT NULL COMMENT 'Identificador del material superior'
	,`roll_up` int(4) NOT NULL COMMENT 'Identificador de la bobina superior'
 	,`roll_up_width` double(8,2) DEFAULT 0.00 COMMENT 'Ancho bobina superior'
  	,`roll_up_length` double(8,2) DEFAULT 0.00 COMMENT 'Largo bobina superior'
	,`material_down` int(4) NOT NULL COMMENT 'Identificador del material inferior'
	,`roll_down` int(4) NOT NULL COMMENT 'Identificador de la bobina inferior'
 	,`roll_down_width` double(8,2) DEFAULT 0.00 COMMENT 'Ancho bobina inferior'
  	,`roll_down_length` double(8,2) DEFAULT 0.00 COMMENT 'Largo bobina inferior'
	,`amount` double(8,2) DEFAULT '0.00' COMMENT 'Cantidad'
	,`blow_units` int(11) DEFAULT 0 COMMENT 'Golpe unidades'	
	,`meters` double(8,2) DEFAULT '0.00' COMMENT 'Metros'
	,`blows` double(8,2) DEFAULT '0.00' COMMENT 'Golpes'
  	,`blows_minute` double(6,2) DEFAULT '0.00' COMMENT 'Golpes por minuto'
  	,`minutes` double(10,2) DEFAULT '0.00' COMMENT 'Minutos'
	,`client` int(4) NOT NULL COMMENT 'Identificador del cliente'
	,`comment` varchar(128) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Comentario'
	,`creation_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de creacion'
	,`creation_date` datetime DEFAULT NULL COMMENT 'Fecha de creacion'
	,`modification_user` varchar(16) COLLATE latin1_spanish_ci DEFAULT NULL COMMENT 'Usuario de modificacion'
	,`modification_date` datetime DEFAULT NULL COMMENT 'Fecha de modificacion'
	,PRIMARY KEY (`id`)
	,KEY `IDX_PLANNING_DOMAIN` (`domain`)
	,KEY `IDX_PLANNING_MACHINE` (`machine`)
	,KEY `IDX_PLANNING_PRODUCT` (`product`)
	,KEY `IDX_PLANNING_MATERIAL_UP` (`material_up`)
	,KEY `IDX_PLANNING_MATERIAL_DOWN` (`material_down`)
	,KEY `IDX_PLANNING_ROLL_UP` (`roll_up`)
	,KEY `IDX_PLANNING_ROLL_DOWN` (`roll_down`)
	,KEY `IDX_PLANNING_CLIENT` (`client`)
	,CONSTRAINT `FK_PLANNING_DOMAIN` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`)
	,CONSTRAINT `FK_PLANNING_MACHINE` FOREIGN KEY (`machine`) REFERENCES `machine` (`id`)
	,CONSTRAINT `FK_PLANNING_PRODUCT` FOREIGN KEY (`product`) REFERENCES `product` (`id`)  
	,CONSTRAINT `FK_PLANNING_MATERIAL_UP` FOREIGN KEY (`material_up`) REFERENCES `material` (`id`)
	,CONSTRAINT `FK_PLANNING_ROLL_UP` FOREIGN KEY (`roll_up`) REFERENCES `roll` (`id`)
	,CONSTRAINT `FK_PLANNING_MATERIAL_DOWN` FOREIGN KEY (`material_down`) REFERENCES `material` (`id`)
	,CONSTRAINT `FK_PLANNING_ROLL_DOWN` FOREIGN KEY (`roll_down`) REFERENCES `roll` (`id`)
	,CONSTRAINT `FK_PLANNING_CLIENT` FOREIGN KEY (`client`) REFERENCES `client` (`id`)  
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Plan de fabricacion';

CREATE TABLE `db_version` (
  `version_number` varchar(10) COLLATE latin1_spanish_ci NOT NULL COMMENT 'Numero de Version de la Base de Datos',
  PRIMARY KEY (`version_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Version de la Base de Datos';

