
INSERT IGNORE INTO at_client_module (`id`, `moduleId`, `name`, `isDeleted`, `lastModificationTime`) VALUES ('4', '4', 'Co-Location', '0', '0');
UPDATE  at_global_config SET `gcValue`='1' WHERE  `gcID`=4;



INSERT IGNORE INTO at_client_details (`vclID`, `vclClientID`, `vclModuleID`, `vclIdentity`, `vclCurrentStatus`, `vclLatestStatus`, `vclRegType_bak`, `vclRegType`, `vclConEligibleForDiscount`, `vclRegistrationCategory`, `vclRegistrationCategoryBackup`, `vclLastModificationTime`, `vclIsDeleted`, `vclActivationDate`, `vclClientType`) VALUES
	(398002, 402002, 4, '', 45001, 45001, -1, 3, NULL, 2, NULL, 1545125860620, 0, 0, 2),
	(399002, 403002, 4, '', 45001, 45001, -1, 2, NULL, 8, NULL, 1545193184764, 0, 0, 2),
	(399003, 404002, 4, '', 45001, 45001, -1, 1, NULL, 2, NULL, 1545193258451, 0, 0, 2),
	(399004, 400004, 4, '', 45001, 45001, -1, 1, NULL, 2, NULL, 1545193285700, 0, 0, 2),
	(399005, 400002, 4, '', 45001, 45001, -1, 3, NULL, 18, NULL, 1545193307764, 0, 0, 2),
	(399006, 401004, 4, '', 45001, 45001, -1, 1, NULL, 8, NULL, 1545193332088, 0, 0, 2);

INSERT IGNORE INTO at_client_contact_details (`vclcID`, `vclcVpnClientID`, `vclcName`, `vclcLastName`, `vclcFatherName`, `vclcMotherName`, `vclcGender`, `vclcDateOfBirth`, `vclcWebAddress`, `vclcDetailsType`, `vclcAddress`, `vclcCity`, `vclcPostCode`, `vclcCountry`, `vclcEmail`, `vclcIsEmailVerified`, `vclcOrganization`, `vclcDate`, `vclcFaxNumber`, `vclcPhoneNumber`, `vclcIsPhoneNumberVerified`, `vclcSignature`, `vclcContactInfoName`, `vclcLastModificationTime`, `vclcIsDeleted`, `vclcOccupation`, `vclcJobType`, `vclcLandlineNumber`) VALUES
	(539000, 398002, 'Alpha Technology', '', NULL, NULL, NULL, NULL, '', 0, 'Rayerbagh, Dhaka', 'Dhaka', '1346', 'BD', 'alphaf54@gmail.com', 0, NULL, NULL, '', '+8801413646563', 0, NULL, NULL, 1545125860939, 0, NULL, NULL, ''),
	(539001, 398002, 'Alpha Tech', '', NULL, NULL, NULL, NULL, NULL, 1, 'Rayerbagh, Dhaka', 'Dhaka', '1346', NULL, 'alphaf54@gmail.com', 0, NULL, NULL, '', '+8801413646564', 0, NULL, NULL, 1545125860968, 0, NULL, NULL, ''),
	(539002, 398002, 'Alpha Tech', '', NULL, NULL, NULL, NULL, NULL, 2, 'Rayerbagh, Dhaka', 'Dhaka', '1346', NULL, 'alphaf54@gmail.com', 0, NULL, NULL, '', '+8801413646564', 0, NULL, NULL, 1545125860969, 0, NULL, NULL, '466466464'),
	(539003, 398002, 'Alpha Tech', '', NULL, NULL, NULL, NULL, NULL, 3, 'Rayerbagh, Dhaka', 'Dhaka', '1346', NULL, 'alphaf54@gmail.com', 0, NULL, NULL, '', '+8801413646564', 0, NULL, NULL, 1545125860970, 0, NULL, NULL, ''),
	(540000, 399002, 'Md. Jaminur Islam', '', NULL, NULL, NULL, NULL, '', 0, 'Polashi', 'Dhaka', '1200', 'BD', 'jaminur@revesoft.com', 0, NULL, NULL, '', '+8801520100542', 0, NULL, NULL, 1545193184885, 0, NULL, NULL, ''),
	(540001, 399002, 'Md. Jaminur Islam', '', NULL, NULL, NULL, NULL, NULL, 1, 'Polashi', 'Dhaka', '1200', NULL, 'jaminur@revesoft.com', 0, NULL, NULL, '', '+8801520100542', 0, NULL, NULL, 1545193184915, 0, NULL, NULL, ''),
	(540002, 399002, 'Md. Jaminur Islam', '', NULL, NULL, NULL, NULL, NULL, 2, 'Polashi', 'Dhaka', '1200', NULL, 'jaminur@revesoft.com', 0, NULL, NULL, '', '+8801520100542', 0, NULL, NULL, 1545193184916, 0, NULL, NULL, ''),
	(540003, 399002, 'Md. Jaminur Islam', '', NULL, NULL, NULL, NULL, NULL, 3, 'Polashi', 'Dhaka', '1200', NULL, 'jaminur@revesoft.com', 0, NULL, NULL, '', '+8801520100542', 0, NULL, NULL, 1545193184919, 0, NULL, NULL, ''),
	(540004, 399003, 'flowman', '', NULL, NULL, NULL, NULL, '', 0, 'Dhaka', 'Dhaka', '', 'BD', 'flowman@hejhd.com', 0, NULL, NULL, '', '+8801411134343', 0, NULL, NULL, 1545193258527, 0, NULL, NULL, ''),
	(540005, 399003, 'flowman', '', NULL, NULL, NULL, NULL, NULL, 1, 'Dhaka', 'Dhaka', '', NULL, 'flowman@hejhd.com', 0, NULL, NULL, '', '+8801411134343', 0, NULL, NULL, 1545193258528, 0, NULL, NULL, ''),
	(540006, 399003, 'flowman', '', NULL, NULL, NULL, NULL, NULL, 2, 'Dhaka', 'Dhaka', '', NULL, 'flowman@hejhd.com', 0, NULL, NULL, '', '+8801411134343', 0, NULL, NULL, 1545193258529, 0, NULL, NULL, ''),
	(540007, 399003, 'flowman', '', NULL, NULL, NULL, NULL, NULL, 3, 'Dhaka', 'Dhaka', '', NULL, 'flowman@hejhd.com', 0, NULL, NULL, '', '+8801411134343', 0, NULL, NULL, 1545193258529, 0, NULL, NULL, ''),
	(540008, 399004, 'Access Telecom (BD) LTD', '', NULL, NULL, NULL, NULL, 'www.accesstel.net', 0, 'Suite-901,Concord Tower-113,Kazi Nazrul Avenue', 'Dhaka', '', 'BD', 'mijanur.rahman@accesstel.net', 0, NULL, NULL, '', '+8801411111111', 0, NULL, NULL, 1545193285773, 0, NULL, NULL, '029335607'),
	(540009, 399004, 'Access Telecom (BD) LTD', '', NULL, NULL, NULL, NULL, NULL, 1, 'Suite-901,Concord Tower-113,Kazi Nazrul Avenue', 'Dhaka', '', NULL, 'mijanur.rahman@accesstel.net', 0, NULL, NULL, '', '+8801411111111', 0, NULL, NULL, 1545193285774, 0, NULL, NULL, '029335607'),
	(540010, 399004, 'Access Telecom (BD) LTD', '', NULL, NULL, NULL, NULL, NULL, 2, 'Suite-901,Concord Tower-113,Kazi Nazrul Avenue', 'Dhaka', '', NULL, 'mijanur.rahman@accesstel.net', 0, NULL, NULL, '', '+8801411111111', 0, NULL, NULL, 1545193285775, 0, NULL, NULL, '029335607'),
	(540011, 399004, 'Access Telecom (BD) LTD', '', NULL, NULL, NULL, NULL, NULL, 3, 'Suite-901,Concord Tower-113,Kazi Nazrul Avenue', 'Dhaka', '', NULL, 'mijanur.rahman@accesstel.net', 0, NULL, NULL, '', '+8801411111111', 0, NULL, NULL, 1545193285775, 0, NULL, NULL, '029335607'),
	(540012, 399005, 'hello test', '', NULL, NULL, NULL, NULL, '', 0, 'dhaka', 'dhaka', '', 'BD', 'hello@hgdh.com', 0, NULL, NULL, '', '+8801913437142', 0, NULL, NULL, 1545193307844, 0, NULL, NULL, ''),
	(540013, 399005, 'hello test', '', NULL, NULL, NULL, NULL, NULL, 1, 'dhaka', 'dhaka', '', NULL, 'hello@hgdh.com', 0, NULL, NULL, '', '+8801913437142', 0, NULL, NULL, 1545193307966, 0, NULL, NULL, ''),
	(540014, 399005, 'hello test', '', NULL, NULL, NULL, NULL, NULL, 2, 'dhaka', 'dhaka', '', NULL, 'hello@hgdh.com', 0, NULL, NULL, '', '+8801913437142', 0, NULL, NULL, 1545193307968, 0, NULL, NULL, ''),
	(540015, 399005, 'hello test', '', NULL, NULL, NULL, NULL, NULL, 3, 'dhaka', 'dhaka', '', NULL, 'hello@hgdh.com', 0, NULL, NULL, '', '+8801913437142', 0, NULL, NULL, 1545193307969, 0, NULL, NULL, ''),
	(540016, 399006, 'Rangpur Community Medical College', '', NULL, NULL, NULL, NULL, '', 0, 'Rangpur', 'Rangpur', '', 'BD', 'info@rcmc.com.bd', 0, NULL, NULL, '', '+8801444444444', 0, NULL, NULL, 1545193332185, 0, NULL, NULL, ''),
	(540017, 399006, 'Rangpur Community Medical College', '', NULL, NULL, NULL, NULL, NULL, 1, 'Rangpur', 'Rangpur', '', NULL, 'info@rcmc.com.bd', 0, NULL, NULL, '', '+8801444444444', 0, NULL, NULL, 1545193332186, 0, NULL, NULL, ''),
	(540018, 399006, 'Rangpur Community Medical College', '', NULL, NULL, NULL, NULL, NULL, 2, 'Rangpur', 'Rangpur', '', NULL, 'info@rcmc.com.bd', 0, NULL, NULL, '', '+8801444444444', 0, NULL, NULL, 1545193332187, 0, NULL, NULL, ''),
	(540019, 399006, 'Rangpur Community Medical College', '', NULL, NULL, NULL, NULL, NULL, 3, 'Rangpur', 'Rangpur', '', NULL, 'info@rcmc.com.bd', 0, NULL, NULL, '', '+8801444444444', 0, NULL, NULL, 1545193332188, 0, NULL, NULL, '');
/*!40000 ALTER TABLE `at_client_contact_details` ENABLE KEYS */;



/*!40000 ALTER TABLE `at_reg_category_vs_type` DISABLE KEYS */;
INSERT IGNORE INTO at_reg_category_vs_type (`id`, `regTypeInAModuleId`, `registrantCategoryId`, `isDeleted`, `lastModificationTime`) VALUES
	(6000, 1000, 2, 0, 1545111094014),
	(7000, 1000, 1, 0, 1545112083339),
	(7001, 1000, 17, 0, 1545112083520),
	(7002, 1000, 18, 0, 1545112083579),
	(7003, 1000, 5, 0, 1545112083637),
	(7004, 1000, 6, 0, 1545112083772),
	(7005, 1000, 8, 0, 1545112083826),
	(8000, 1002, 16, 0, 1545112150222),
	(8001, 1002, 1, 0, 1545112150353),
	(8002, 1002, 17, 0, 1545112150406),
	(8003, 1002, 2, 0, 1545112150486),
	(8004, 1002, 18, 0, 1545112150560),
	(8005, 1002, 5, 0, 1545112150596),
	(8006, 1002, 8, 0, 1545112150655),
	(8007, 1002, 12, 0, 1545112150707),
	(8008, 1002, 13, 0, 1545112150748),
	(8009, 1002, 14, 0, 1545112150786),
	(8010, 1002, 15, 0, 1545112150824),
	(9000, 1001, 5, 0, 1545112185439),
	(9001, 1001, 6, 0, 1545112185583),
	(9002, 1001, 8, 0, 1545112185634),
	(10000, 1003, 9, 0, 1545112215500),
	(10001, 1003, 10, 0, 1545112215630),
	(10002, 1003, 11, 0, 1545112215689),
	(11000, 1004, 7, 0, 1545112235638);


INSERT IGNORE INTO at_reg_type_vs_module (`id`, `moduleId`, `registrantTypeId`, `isDeleted`, `lastModificationTime`) VALUES
	(1000, 4, 1, 0, 1545110895694),
	(1001, 4, 2, 0, 1545110895904),
	(1002, 4, 3, 0, 1545110895963),
	(1003, 4, 4, 0, 1545110896034),
	(1004, 4, 5, 0, 1545110896113);



