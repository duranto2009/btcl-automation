
DROP TABLE IF EXISTS `at_client_document_type`;
CREATE TABLE `at_client_document_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `docTypeId` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `lastModificationTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14118 DEFAULT CHARSET=utf8;

INSERT INTO `at_client_document_type` VALUES (1,101,'National ID',0,1538371670982),(2,102,'Passport',0,0),(3,103,'TIN',0,0),(4,104,'Trade License',0,0),(5,105,'Forwarding Letter',0,0),(6,106,'Signature',0,0),(7,107,'VAT',0,0),(8,108,'Registration',0,0),(9,109,'Tax',0,0),(10,110,'Memorandum of Article',0,0),(11,111,'Photograph',0,0),(12,112,'Updated Company Tax Certificate',0,0),(13,113,'Valid BTRC License',0,0),(14,114,'Deed of Home Office Rent',0,0),(15,115,'Holding Tax of Home Office',0,0),(16,116,'Updated VAT Registration and VAT Certificate',0,0),(17,117,'BTRC Certificate',0,1537937719847),(14117,118,'BTRC Cert',0,1538371724756);

/*********** renamed column ***************/
DROP TABLE IF EXISTS `at_client_doc`;
CREATE TABLE `at_client_doc` (
  `id` bigint(20) NOT NULL,
  `clientID` bigint(20) DEFAULT NULL,
  `doc` blob,
  `description` varchar(1000) DEFAULT NULL,
  `isCommentDoc` tinyint(1) DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT NULL,
  `lastModificationTime` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*********** modified ***************/
ALTER TABLE `at_client`
  ADD COLUMN `isCorporate` TINYINT NOT NULL AFTER `clLatestStatus`;

/*********** modified ***************/
ALTER TABLE `at_client_module`
  ADD COLUMN `moduleId` bigint(20) NOT NULL default 0 AFTER `id`;

UPDATE `at_client_module` table1,`at_client_module` table2
SET table1.moduleId = table2.id
WHERE table1.id = table2.id;


/*********** modified ***************/
ALTER TABLE `at_client_reg_category`
  ADD COLUMN `regCatId` bigint(20) NOT NULL default 0 AFTER `id`;

UPDATE `at_client_reg_category` table1,`at_client_reg_category` table2
SET table1.regCatId = table2.id
WHERE table1.id = table2.id;

/*********** modified ***************/
ALTER TABLE `at_client_reg_type`
  ADD COLUMN `regTypeId` bigint(20) NOT NULL default 0 AFTER `id`;

UPDATE `at_client_reg_type` table1,`at_client_reg_type` table2
SET table1.regTypeId = table2.id
WHERE table1.id = table2.id;




/*********** new ***************/

DROP TABLE IF EXISTS `at_reg_type_vs_module`;
CREATE TABLE `at_reg_type_vs_module` (
  `id` bigint(20) NOT NULL,
  `moduleId` bigint(20) NOT NULL,
  `registrantTypeId` bigint(20) NOT NULL,
  `isDeleted` bigint(20) DEFAULT NULL,
  `lastModificationTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `at_reg_type_vs_module` VALUES
  (2000, 7, 1, 0, 1538390430825),
  (3000, 7, 2, 0, 1538474266635),
  (4000, 7, 3, 0, 1538646203568),
  (6000, 7, 4, 0, 1538895126175),
  (7000, 7, 5, 0, 1538895782285),
  (8000, 1, 1, 0, 1539251356462),
  (8001, 1, 2, 0, 1539251356608),
  (8002, 1, 3, 0, 1539251356673),
  (8003, 1, 4, 0, 1539251356753),
  (8004, 1, 5, 0, 1539251356831);


DROP TABLE IF EXISTS `at_reg_category_vs_type`;
CREATE TABLE `at_reg_category_vs_type` (
  `id` bigint(20) NOT NULL,
  `regTypeInAModuleId` bigint(20) NOT NULL,
  `registrantCategoryId` bigint(20) NOT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL,
  `lastModificationTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `at_reg_category_vs_type` VALUES
  (3001, 2000, 1, NULL, NULL),
  (3002, 2000, 2, NULL, NULL),
  (3003, 2000, 3, NULL, NULL),
  (4000, 2000, 5, 0, 1538645263689),
  (5000, 2000, 6, 0, 1538645271633),
  (6000, 2000, 18, 0, 1538645285513),
  (7000, 2000, 21, 0, 1538645850313),
  (8000, 4000, 2, 0, 1538646203713),
  (9000, 3000, 1, 0, 1538895895560),
  (9001, 3000, 2, 0, 1538895895722),
  (9002, 3000, 3, 0, 1538895895774),
  (9003, 3000, 4, 0, 1538895895843),
  (9004, 3000, 5, 0, 1538895895889),
  (9005, 3000, 6, 0, 1538895895919),
  (9006, 3000, 7, 0, 1538895895943),
  (9007, 3000, 8, 0, 1538895895988),
  (9008, 3000, 9, 0, 1538895896010),
  (10000, 4000, 2, 0, 1538895923773),
  (10001, 4000, 3, 0, 1538895923933),
  (10002, 4000, 4, 0, 1538895923980),
  (10003, 4000, 5, 0, 1538895924049),
  (10004, 4000, 6, 0, 1538895924158),
  (10005, 4000, 7, 0, 1538895924256),
  (10006, 4000, 8, 0, 1538895924286),
  (10007, 4000, 9, 0, 1538895924430),
  (10008, 4000, 10, 0, 1538895924498),
  (10009, 4000, 11, 0, 1538895924531),
  (10010, 4000, 12, 0, 1538895924552),
  (10011, 4000, 13, 0, 1538895924585),
  (10012, 4000, 14, 0, 1538895924610),
  (11000, 6000, 2, 0, 1538895938725),
  (11001, 6000, 3, 0, 1538895938916),
  (11002, 6000, 4, 0, 1538895938974),
  (11003, 6000, 5, 0, 1538895939032),
  (11004, 6000, 6, 0, 1538895939060),
  (11005, 6000, 7, 0, 1538895939103),
  (11006, 6000, 8, 0, 1538895939127),
  (11007, 6000, 9, 0, 1538895939185),
  (11008, 6000, 10, 0, 1538895939211),
  (11009, 6000, 11, 0, 1538895939237),
  (11010, 6000, 12, 0, 1538895939295),
  (11011, 6000, 13, 0, 1538895939317),
  (11012, 6000, 14, 0, 1538895939342),
  (12000, 7000, 2, 0, 1538895962286),
  (12001, 7000, 3, 0, 1538895962441),
  (12002, 7000, 4, 0, 1538895962472),
  (12003, 7000, 5, 0, 1538895962495),
  (12004, 7000, 6, 0, 1538895962533),
  (12005, 7000, 7, 0, 1538895962597),
  (12006, 7000, 8, 0, 1538895962627),
  (12007, 7000, 9, 0, 1538895962711),
  (12008, 7000, 10, 0, 1538895962771),
  (12009, 7000, 11, 0, 1538895962795),
  (12010, 7000, 12, 0, 1538895962826),
  (12011, 7000, 13, 0, 1538895962851),
  (12012, 7000, 14, 0, 1538895962875),
  (13000, 8000, 2, 0, 1539251620641),
  (13001, 8000, 3, 0, 1539251620921),
  (13002, 8000, 4, 0, 1539251620958),
  (13003, 8000, 5, 0, 1539251620991),
  (13004, 8000, 6, 0, 1539251621043),
  (13005, 8000, 7, 0, 1539251621090),
  (13006, 8000, 8, 0, 1539251621123),
  (13007, 8000, 9, 0, 1539251621148),
  (13008, 8000, 10, 0, 1539251621180),
  (13009, 8000, 11, 0, 1539251621230),
  (14000, 8001, 2, 0, 1539251628439),
  (14001, 8001, 3, 0, 1539251628567),
  (14002, 8001, 4, 0, 1539251628623),
  (14003, 8001, 5, 0, 1539251628689),
  (14004, 8001, 6, 0, 1539251628733),
  (14005, 8001, 7, 0, 1539251628770),
  (14006, 8001, 8, 0, 1539251628836),
  (14007, 8001, 9, 0, 1539251628864),
  (14008, 8001, 10, 0, 1539251628889),
  (14009, 8001, 11, 0, 1539251628917),
  (15000, 8002, 2, 0, 1539251634361),
  (15001, 8002, 3, 0, 1539251634516),
  (15002, 8002, 4, 0, 1539251634551),
  (15003, 8002, 5, 0, 1539251634597),
  (15004, 8002, 6, 0, 1539251634652),
  (15005, 8002, 7, 0, 1539251634673),
  (15006, 8002, 8, 0, 1539251634713),
  (15007, 8002, 9, 0, 1539251634752),
  (15008, 8002, 10, 0, 1539251634772),
  (15009, 8002, 11, 0, 1539251634801),
  (16000, 8003, 2, 0, 1539251645189),
  (16001, 8003, 3, 0, 1539251645273),
  (16002, 8003, 4, 0, 1539251645343),
  (16003, 8003, 5, 0, 1539251645375),
  (16004, 8003, 6, 0, 1539251645414),
  (16005, 8003, 7, 0, 1539251645441),
  (16006, 8003, 8, 0, 1539251645473),
  (16007, 8003, 9, 0, 1539251645498),
  (16008, 8003, 10, 0, 1539251645523),
  (16009, 8003, 11, 0, 1539251645563),
  (17000, 8004, 2, 0, 1539251652029),
  (17001, 8004, 3, 0, 1539251652092),
  (17002, 8004, 4, 0, 1539251652126),
  (17003, 8004, 5, 0, 1539251652159),
  (17004, 8004, 6, 0, 1539251652196),
  (17005, 8004, 7, 0, 1539251652224),
  (17006, 8004, 8, 0, 1539251652269),
  (17007, 8004, 9, 0, 1539251652310),
  (17008, 8004, 10, 0, 1539251652331),
  (17009, 8004, 11, 0, 1539251652358);


DROP TABLE IF EXISTS `at_req_doc_vs_category`;
CREATE TABLE `at_req_doc_vs_category` (
  `id` bigint(20) NOT NULL,
  `regCategoryInATypeId` bigint(20) NOT NULL,
  `documentId` bigint(20) NOT NULL,
  `isMandatory` tinyint(4) DEFAULT '0',
  `isDeleted` tinyint(4) DEFAULT NULL,
  `lastModificationTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `at_uploaded_file`;
CREATE TABLE `at_uploaded_file` (
  `Id` bigint(20) NOT NULL,
  `moduleId` bigint(20) NOT NULL,
  `componentId` bigint(20) NOT NULL,
  `applicationId` bigint(20) NOT NULL,
  `stateId` bigint(20) NOT NULL,
  `realName` varchar(80) NOT NULL,
  `localName` varchar(255) NOT NULL,
  `ownerId` bigint(20) NOT NULL,
  `size` bigint(20) DEFAULT NULL,
  `uploadTime` bigint(20) DEFAULT NULL,
  `directoryPath` varchar(255) DEFAULT NULL,
  `isDeleted` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




INSERT IGNORE INTO vbSequencer VALUES
  ('at_client_document_type',1000,0),
  ('at_client_doc',1000,0),
  ('at_client_module',1000,0),
  ('at_client_reg_category',1000,0),
  ('at_client_reg_type',1000,0),
  ('at_reg_type_vs_module',1000,0),
  ('at_reg_category_vs_type',1000,0),
  ('at_req_doc_vs_category',1000,0),
  ('at_uploaded_file',1000,0);



--2 January--

ALTER TABLE `at_client_details`
	ADD COLUMN `btrcLicenseDate` VARCHAR(50) NULL AFTER `vclClientType`;


CREATE TABLE `at_client_reg_sub_category` (
	`id` BIGINT NOT NULL,
	`regSubCatId` BIGINT NOT NULL,
	`parentCatId` BIGINT(20) NOT NULL,
	`name` VARCHAR(100) NOT NULL,
	`isDeleted` TINYINT NOT NULL,
	`lastModificationTime` BIGINT NOT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_general_ci'
;
INSERT INTO `vbsequencer` (`table_name`, `next_id`) VALUES ('at_client_reg_sub_category', '0');



INSERT INTO `at_client_reg_sub_category` (`id`, `regSubCatId`, `parentCatId`, `name`, `isDeleted`, `lastModificationTime`) VALUES ('1', '1', '2', 'ISP-Nationwide', '0', '0');
INSERT INTO `at_client_reg_sub_category` (`id`, `regSubCatId`, `parentCatId`, `name`, `isDeleted`, `lastModificationTime`) VALUES ('2', '2', '2', 'ISP-Central Zone', '0', '0');
INSERT INTO `at_client_reg_sub_category` (`id`, `regSubCatId`, `parentCatId`, `name`, `isDeleted`, `lastModificationTime`) VALUES ('3', '3', '2', 'ISP-Zonal', '0', '0');
INSERT INTO `at_client_reg_sub_category` (`id`, `regSubCatId`, `parentCatId`, `name`, `isDeleted`, `lastModificationTime`) VALUES ('4', '4', '2', 'ISP-Category A', '0', '0');
INSERT INTO `at_client_reg_sub_category` (`id`, `regSubCatId`, `parentCatId`, `name`, `isDeleted`, `lastModificationTime`) VALUES ('5', '5', '2', 'ISP-Category B', '0', '0');
INSERT INTO `at_client_reg_sub_category` (`id`, `regSubCatId`, `parentCatId`, `name`, `isDeleted`, `lastModificationTime`) VALUES ('6', '6', '2', 'ISP-Category C', '0', '0');






---03 Jan


ALTER TABLE `at_client_details`
	ADD COLUMN `registrationSubCategory` BIGINT(20) NULL DEFAULT '0' AFTER `vclRegistrationCategory`;





--- 06 january

UPDATE `at_global_config` SET `gcValue`='0' WHERE  `gcID`=10;

-- 08 january

ALTER TABLE `at_client_tariff_category`
	ALTER `id` DROP DEFAULT;
ALTER TABLE `at_client_tariff_category`
	CHANGE COLUMN `id` `id` INT NOT NULL FIRST,
	ADD COLUMN `tariffCatId` INT(20) NOT NULL AFTER `id`;


UPDATE `at_client_tariff_category` SET `tariffCatId`='1' WHERE  `id`=1;
UPDATE `at_client_tariff_category` SET `tariffCatId`='2' WHERE  `id`=2;
UPDATE `at_client_tariff_category` SET `tariffCatId`='3' WHERE  `id`=3;
UPDATE `at_client_tariff_category` SET `tariffCatId`='4' WHERE  `id`=4;


ALTER TABLE `at_reg_category_vs_type`
	ADD COLUMN `tariffCatId` INT NOT NULL AFTER `registrantCategoryId`;

UPDATE `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=2004;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=2005;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='2' WHERE  `id`=2006;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='4' WHERE  `id`=2002;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='4' WHERE  `id`=2000;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='4' WHERE  `id`=2001;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='4' WHERE  `id`=2003;

UPDATE `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=4000;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=4001;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='2' WHERE  `id`=4002;

UPDATE `at_reg_category_vs_type` SET `tariffCatId`='2' WHERE  `id`=3005;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='2' WHERE  `id`=3007;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='2' WHERE  `id`=3008;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='3' WHERE  `id`=3009;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='3' WHERE  `id`=3006;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='3' WHERE  `id`=3010;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='3' WHERE  `id`=3000;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='4' WHERE  `id`=3003;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='4' WHERE  `id`=3001;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='4' WHERE  `id`=3002;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='4' WHERE  `id`=3004;

UPDATE `at_reg_category_vs_type` SET `tariffCatId`='2' WHERE  `id`=1000;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='2' WHERE  `id`=1001;
UPDATE `at_reg_category_vs_type` SET `tariffCatId`='2' WHERE  `id`=1002;

UPDATE `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=5000;







--17 January

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9100', '9001', 'Monthly Bill', '9');

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9101', '9100', 'Search', '9');

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9105', '9001', 'Monthly Usage', '9');

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9106', '9105', 'Search', '9');

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9110', '9001', 'Monthly Bill Summary', '9');

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9112', '9110', 'Search', '9');

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9111', '9110', 'Check', '9');

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9113', '9110', 'Generate', '9');

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9120', '9001', 'Outsource Bill', '9');

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('9121', '9120', 'Search', '9');



--23 Jan


UPDATE `at_state` SET `stActivationStatus`='2' WHERE  `stID`=45009;
UPDATE `at_state` SET `stActivationStatus`='2' WHERE  `stID`=95009;


--30 Jan

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('7120', '7001', 'Outsource Bill', '7');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('7121', '7120', 'Search', '7');


--12 Feb

ALTER TABLE `at_client_reg_category` ADD COLUMN `discount` FLOAT NOT NULL DEFAULT '0.0' AFTER `name`;
UPDATE `at_client_reg_category` SET `discount`='15' WHERE  `id`=2;
UPDATE `at_client_reg_category` SET `discount`='15' WHERE  `id`=1;



--16 April

INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('50000', '-1', 'Client Classification Management', '200');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('50001', '50000', 'Add Client Type', '200');
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('50002', '50000', 'Add Client Type', '200');
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('60001', '50000', 'Add Client Type', '200');
UPDATE  `admenu` SET `mnMenuID`='60000' WHERE  `mnMenuID`=50000;
UPDATE  `admenu` SET `mnParentMenuID`='60000' WHERE  `mnMenuID`=60001;
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('60002', '60000', 'Add Client Category', '200');
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('60003', '60000', 'Modify Client Type', '200');
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('60004', '60000', 'Modify Client Category', '200');


ALTER TABLE `vpn_monthly_usage_link`
	CHANGE COLUMN `linkName` `linkName` VARCHAR(245) NULL DEFAULT NULL AFTER `linkId`;

	ALTER TABLE `vpn_monthly_bill_link`
	CHANGE COLUMN `linkName` `linkName` VARCHAR(245) NULL DEFAULT NULL AFTER `linkStatus`;


--18 APril

UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=19000;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=19001;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=19002;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=19003;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=19004;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=19005;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=19006;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=20000;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=20001;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=20002;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21000;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21001;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21002;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21003;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21004;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21005;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21006;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21007;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21008;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21009;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=21010;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=22000;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=22001;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=22002;
UPDATE  `at_reg_category_vs_type` SET `tariffCatId`='1' WHERE  `id`=23000;


INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`)
VALUES ('60005', '60000', 'Modify Client Tariff Category', '200');


-- 12/05/19

INSERT INTO `at_action_state` (`asActionTypeID`, `asRootActionTypeID`, `asDescription`, `asType`,
 `asNextStateID`, `asIsSystemAction`, `asIsClientAction`,
  `asIsVisibleToSystem`, `asIsVisibleToClient`, `asEntityTypeID`)
VALUES ('65006', '65001', 'Correct Application', 'updateApp', '65001', '1', '1', '1', '1', '651');


INSERT INTO `at_action_state` (`asRootActionTypeID`, `asDescription`, `asType`, `asNextStateID`,
`asIsSystemAction`, `asIsClientAction`, `asIsVisibleToSystem`, `asIsVisibleToClient`, `asEntityTypeID`)
 VALUES ('75001', 'Update Application', 'updateApp', '75001', '1', '1', '1', '1', '751');

UPDATE `at_action_state` SET `asActionTypeID`='75006', `asDescription`='Correct Application' WHERE  `asActionTypeID`=0;




INSERT INTO `at_action_state` (`asRootActionTypeID`, `asDescription`, `asType`, `asNextStateID`,
 `asIsSystemAction`, `asIsClientAction`, `asIsVisibleToSystem`, `asIsVisibleToClient`, `asEntityTypeID`)
 VALUES ('45001', 'Update Application', 'updateApp', '45001', '1', '1', '1', '1', '451');

UPDATE `at_action_state` SET `asActionTypeID`='45006', `asDescription`='Correct Application' WHERE  `asActionTypeID`=0;



INSERT INTO `at_action_state` (`asRootActionTypeID`, `asDescription`, `asType`,
 `asNextStateID`, `asIsSystemAction`, `asIsClientAction`, `asIsVisibleToSystem`, `asIsVisibleToClient`,
  `asEntityTypeID`) VALUES ('95001', 'Update Application', 'updateApp', '95001', '1', '1', '1', '1', '951');

UPDATE `at_action_state` SET `asActionTypeID`='95006', `asDescription`='Correct Application' WHERE  `asActionTypeID`=0;




INSERT INTO `at_universal_table` (`tableName`, `columnName`, `value`) VALUES ('AllConfigurations', 'MAXIMUM_FILE_SIZE', '5242880L');




-- 26/05

CREATE TABLE `temporary_client` (
	`id` BIGINT NOT NULL,
	`emailId` VARCHAR(100) NOT NULL,
	`mobileNumber` VARCHAR(100) NOT NULL,
	`isEmailVerified` TINYINT NOT NULL DEFAULT '0',
	`isMobileNumberVerified` TINYINT NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
;

INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('temporary_client', '100000');
