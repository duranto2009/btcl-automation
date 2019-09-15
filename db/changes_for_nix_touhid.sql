UPDATE at_global_config SET `gcValue`='1' WHERE  `gcID`=9;
INSERT INTO at_client_module (`id`, `moduleId`, `name`, `isDeleted`, `lastModificationTime`) VALUES ('9', '9', 'NIX', '0', '0');


INSERT INTO `at_client_contact_details` (`vclcID`, `vclcVpnClientID`, `vclcName`, `vclcLastName`, `vclcFatherName`, `vclcMotherName`, `vclcGender`, `vclcDateOfBirth`, `vclcWebAddress`, `vclcDetailsType`, `vclcAddress`, `vclcCity`, `vclcPostCode`, `vclcCountry`, `vclcEmail`, `vclcIsEmailVerified`, `vclcOrganization`, `vclcDate`, `vclcFaxNumber`, `vclcPhoneNumber`, `vclcIsPhoneNumberVerified`, `vclcSignature`, `vclcContactInfoName`, `vclcLastModificationTime`, `vclcIsDeleted`, `vclcOccupation`, `vclcJobType`, `vclcLandlineNumber`) VALUES
	(541000, 400002, 'Alpha Technology', '', NULL, NULL, NULL, NULL, '', 0, 'Rayerbagh, Dhaka', 'Dhaka', '1346', 'BD', 'alphaf54@gmail.com', 0, NULL, NULL, '', '+8801413646563', 0, NULL, NULL, 1545216565556, 0, NULL, NULL, ''),
	(541001, 400002, 'Alpha Tech', '', NULL, NULL, NULL, NULL, NULL, 1, 'Rayerbagh, Dhaka', 'Dhaka', '1346', NULL, 'alphaf54@gmail.com', 0, NULL, NULL, '', '+8801413646564', 0, NULL, NULL, 1545216565584, 0, NULL, NULL, ''),
	(541002, 400002, 'Alpha Tech', '', NULL, NULL, NULL, NULL, NULL, 2, 'Rayerbagh, Dhaka', 'Dhaka', '1346', NULL, 'alphaf54@gmail.com', 0, NULL, NULL, '', '+8801413646564', 0, NULL, NULL, 1545216565595, 0, NULL, NULL, '466466464'),
	(541003, 400002, 'Alpha Tech', '', NULL, NULL, NULL, NULL, NULL, 3, 'Rayerbagh, Dhaka', 'Dhaka', '1346', NULL, 'alphaf54@gmail.com', 0, NULL, NULL, '', '+8801413646564', 0, NULL, NULL, 1545216565596, 0, NULL, NULL, ''),
	(542000, 401002, 'flowman', '', NULL, NULL, NULL, NULL, '', 0, 'Dhaka', 'Dhaka', '', 'BD', 'flowman@hejhd.com', 0, NULL, NULL, '', '+8801411134343', 0, NULL, NULL, 1545276413285, 0, NULL, NULL, ''),
	(542001, 401002, 'flowman', '', NULL, NULL, NULL, NULL, NULL, 1, 'Dhaka', 'Dhaka', '', NULL, 'flowman@hejhd.com', 0, NULL, NULL, '', '+8801411134343', 0, NULL, NULL, 1545276413349, 0, NULL, NULL, ''),
	(542002, 401002, 'flowman', '', NULL, NULL, NULL, NULL, NULL, 2, 'Dhaka', 'Dhaka', '', NULL, 'flowman@hejhd.com', 0, NULL, NULL, '', '+8801411134343', 0, NULL, NULL, 1545276413350, 0, NULL, NULL, ''),
	(542003, 401002, 'flowman', '', NULL, NULL, NULL, NULL, NULL, 3, 'Dhaka', 'Dhaka', '', NULL, 'flowman@hejhd.com', 0, NULL, NULL, '', '+8801411134343', 0, NULL, NULL, 1545276413351, 0, NULL, NULL, ''),
	(542004, 401003, 'Access Telecom (BD) LTD', '', NULL, NULL, NULL, NULL, 'www.accesstel.net', 0, 'Suite-901,Concord Tower-113,Kazi Nazrul Avenue', 'Dhaka', '', 'BD', 'mijanur.rahman@accesstel.net', 0, NULL, NULL, '', '+8801411111111', 0, NULL, NULL, 1545276455793, 0, NULL, NULL, '029335607'),
	(542005, 401003, 'Access Telecom (BD) LTD', '', NULL, NULL, NULL, NULL, NULL, 1, 'Suite-901,Concord Tower-113,Kazi Nazrul Avenue', 'Dhaka', '', NULL, 'mijanur.rahman@accesstel.net', 0, NULL, NULL, '', '+8801411111111', 0, NULL, NULL, 1545276455807, 0, NULL, NULL, '029335607'),
	(542006, 401003, 'Access Telecom (BD) LTD', '', NULL, NULL, NULL, NULL, NULL, 2, 'Suite-901,Concord Tower-113,Kazi Nazrul Avenue', 'Dhaka', '', NULL, 'mijanur.rahman@accesstel.net', 0, NULL, NULL, '', '+8801411111111', 0, NULL, NULL, 1545276455808, 0, NULL, NULL, '029335607'),
	(542007, 401003, 'Access Telecom (BD) LTD', '', NULL, NULL, NULL, NULL, NULL, 3, 'Suite-901,Concord Tower-113,Kazi Nazrul Avenue', 'Dhaka', '', NULL, 'mijanur.rahman@accesstel.net', 0, NULL, NULL, '', '+8801411111111', 0, NULL, NULL, 1545276455809, 0, NULL, NULL, '029335607');

INSERT INTO `at_client_details` (`vclID`, `vclClientID`, `vclModuleID`, `vclIdentity`, `vclCurrentStatus`, `vclLatestStatus`, `vclRegType_bak`, `vclRegType`, `vclConEligibleForDiscount`, `vclRegistrationCategory`, `vclRegistrationCategoryBackup`, `vclLastModificationTime`, `vclIsDeleted`, `vclActivationDate`, `vclClientType`) VALUES
	(400002, 402002, 9, '', 95001, 95001, -1, 1, NULL, 2, NULL, 1545216565201, 0, 0, 2),
	(401002, 404002, 9, '', 95001, 95001, -1, 1, NULL, 2, NULL, 1545276412905, 0, 0, 2),
	(401003, 400004, 9, '', 95001, 95001, -1, 3, NULL, 14, NULL, 1545276455699, 0, 0, 2);

  INSERT INTO `at_reg_category_vs_type` (`id`, `regTypeInAModuleId`, `registrantCategoryId`,`tariffCatId`, `isDeleted`, `lastModificationTime`) VALUES
    (14000, 9000, 1, 0, 1545215526016),
    (14001, 9000, 17, 0, 1545215526132),
    (14002, 9000, 18, 0, 1545215526181),
    (14003, 9000, 5, 0, 1545215526230),
    (14004, 9000, 6, 0, 1545215526262),
    (14005, 9000, 8, 0, 1545215526306),
    (14006, 9000, 2, 0, 1545215526345),
    (15000, 9002, 16, 0, 1545216325153),
    (15001, 9002, 1, 0, 1545216325304),
    (15002, 9002, 17, 0, 1545216325364),
    (15003, 9002, 2, 0, 1545216325405),
    (15004, 9002, 18, 0, 1545216325451),
    (15005, 9002, 5, 0, 1545216325480),
    (15006, 9002, 8, 0, 1545216325510),
    (15007, 9002, 12, 0, 1545216325536),
    (15008, 9002, 13, 0, 1545216325598),
    (15009, 9002, 14, 0, 1545216325662),
    (15010, 9002, 15, 0, 1545216325709),
    (16000, 9001, 5, 0, 1545216419033),
    (16001, 9001, 6, 0, 1545216419159),
    (16002, 9001, 8, 0, 1545216419214),
    (17000, 9003, 9, 0, 1545216475725),
    (17001, 9003, 10, 0, 1545216475886),
    (17002, 9003, 11, 0, 1545216475950),
    (18000, 9004, 7, 0, 1545216505274);

  INSERT INTO `at_reg_type_vs_module` (`id`, `moduleId`, `registrantTypeId`, `isDeleted`, `lastModificationTime`) VALUES
    (9000, 9, 1, 0, 1545212562274),
    (9001, 9, 2, 0, 1545212562453),
    (9002, 9, 3, 0, 1545212562474),
    (9003, 9, 4, 0, 1545212562551),
    (9004, 9, 5, 0, 1545212562619);
