-- 10 dec 2018

DROP TABLE if exists
at_colocation_req,
at_colocation,
at_coloc_space,
at_coloc_region,
at_coloc_rack_slice,
at_coloc_rack,
at_coloc_cost;


create table if not exists colocation_application
(
	application_id bigint not null
		primary key,
	client_id bigint null,
	user_id bigint null,
	submission_date bigint not null,
	application_type int(255) not null,
	connection_id int null,
	connection_type int null,
	state int null,
	demand_note_id bigint null,
	is_demand_note_needed tinyint(255) not null,
	suggested_date bigint null,
	description text null,
	is_service_started tinyint default '0' not null,
	comment text null,
	skip_payment int default '0' null,
	rack_needed int(1) null,
	rack_type_id int(2) null,
	rack_space double(11,2) null,
	power_needed int(1) null,
	power_amount double(11,2) null,
	power_type int(1) null,
	fiber_needed int(1) null,
	fiber_core int null,
	fiber_type int(1) null
);



INSERT INTO `vbSequencer` (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('colocation_application', 1, DEFAULT)

create table if not exists colocation_inventory_template
(
	id bigint auto_increment
		primary key,
	type int(2) not null,
	value varchar(25) not null
);

create table if not exists colocation_cost_config
(
	id bigint auto_increment
		primary key,
	type_id int(2) not null,
	price double(10,2) not null,
	quantity_id int null
);




create table if not exists colocation_connection
(
	id bigint auto_increment
		primary key,
	history_id bigint null,
	client_id bigint null,
	name varchar(255) null,
	connection_type int null,
	rack_needed int null,
	rack_size_id int null,
	rack_space_id int null,
	power_needed int(1) null,
	power_amount int null,
	power_type_id int null,
	fiber_needed int null,
	fiber_core int null,
	fiber_type_id int null,
	active_from bigint null,
	active_to bigint null,
	valid_from bigint null,
	valid_to bigint null,
	status int(255) null,
	start_date bigint null,
	incident int(255) null,
	discount_rate double default '0' null,
	constraint colocation_connection_ibfk_1
		foreign key (client_id) references at_client (clID)
);



create table if not exists colocation_inventory_category
(
	id bigint auto_increment
		primary key,
	value varchar(25) not null
);




-- 11 dec 2018
INSERT INTO module (`id`, `name`) VALUES (10, 'CoLocation');
INSERT INTO component (`id`, `name`, `module`) VALUES (10, 'Connection Application', 10);
INSERT INTO flow (`id`, `name`, `component`) VALUES (1001, 'New Connection Application', 10);

INSERT INTO adrole (`rlRoleID`, `rlLevel`, `rlRoleName`, `rlRoleDesc`, `rlMaxClientPerDay`, `rlRestrictedToOwn`, `rlLastModificationTime`, `rlIsDeleted`, `rlParentRoleID`)
VALUES (20000, 100, 'DGM Domain', 'Domain Deputy General Manager', 0, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO adrole (`rlRoleID`, `rlLevel`, `rlRoleName`, `rlRoleDesc`, `rlMaxClientPerDay`, `rlRestrictedToOwn`, `rlLastModificationTime`, `rlIsDeleted`, `rlParentRoleID`)
VALUES (20001, 100, 'Power', 'Power Division', 0, 0, 0, 0, 1);
INSERT INTO adrole (`rlRoleID`, `rlLevel`, `rlRoleName`, `rlRoleDesc`, `rlMaxClientPerDay`, `rlRestrictedToOwn`, `rlLastModificationTime`, `rlIsDeleted`, `rlParentRoleID`)
VALUES (20002, 100, 'Space&OFC', 'Space&OFC Division', 0, 0, 0, 0, 1);
INSERT INTO adrole (`rlRoleID`, `rlLevel`, `rlRoleName`, `rlRoleDesc`, `rlMaxClientPerDay`, `rlRestrictedToOwn`, `rlLastModificationTime`, `rlIsDeleted`, `rlParentRoleID`)
VALUES (20003, 100, 'AM Domain', 'Domain Assistant Manager', 0, 0, 0, 0, 1);

INSERT INTO aduser (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`,
`usStatus`, `usFullName`, `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`,
`usAddedBy`, `usAddTime`, `usLastModifiedBy`, `usLastModificationTime`, `usBalance`, `usExchange`,
`usProfilePicturePath`, `usLoginIPs`, `usDeptName`, `usDistrictID`, `usUpazilaID`, `usUnionID`,
`usIsDeleted`, `usIsBTCLPersonnel`, `usZoneID`)
VALUES (50227, 'dgm_domain', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', 20000, '', 0, 'Monirul', 'DGM Domain', '', '', '',
'', 0, 1505098390110, 4127, 1510554343227, 0.00000, 0, null, '', '', null, null, 0, 0, 1, 0);
INSERT INTO aduser (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`,
`usStatus`, `usFullName`, `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`,
`usAddedBy`, `usAddTime`, `usLastModifiedBy`, `usLastModificationTime`, `usBalance`, `usExchange`,
`usProfilePicturePath`, `usLoginIPs`, `usDeptName`, `usDistrictID`, `usUpazilaID`, `usUnionID`,
`usIsDeleted`, `usIsBTCLPersonnel`, `usZoneID`)
VALUES (50228, 'power', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', 20001, '', 0, 'mithun', 'Power Division', '', '', '',
'', 0, 1505098390110, 4127, 1510554343227, 0.00000, 0, null, '', '', null, null, 0, 0, 1, 0);
INSERT INTO aduser (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`,
`usStatus`, `usFullName`, `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`,
`usAddedBy`, `usAddTime`, `usLastModifiedBy`, `usLastModificationTime`, `usBalance`, `usExchange`,
`usProfilePicturePath`, `usLoginIPs`, `usDeptName`, `usDistrictID`, `usUpazilaID`, `usUnionID`,
`usIsDeleted`, `usIsBTCLPersonnel`, `usZoneID`)
VALUES (50229, 'space_ofc', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', 20002, '', 0, 'bony', 'Space OFC Division', '', '', '',
'', 0, 1505098390110, 4127, 1510554343227, 0.00000, 0, null, '', '', null, null, 0, 0, 1, 0);
INSERT INTO aduser (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`,
`usStatus`, `usFullName`, `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`,
`usAddedBy`, `usAddTime`, `usLastModifiedBy`, `usLastModificationTime`, `usBalance`, `usExchange`,
`usProfilePicturePath`, `usLoginIPs`, `usDeptName`, `usDistrictID`, `usUpazilaID`, `usUnionID`,
`usIsDeleted`, `usIsBTCLPersonnel`, `usZoneID`)
VALUES (50230, 'am_domain', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', 20003, '', 0, 'jaminur', 'AM Domain', '', '', '',
'', 0, 1505098390110, 4127, 1510554343227, 0.00000, 0, null, '', '', null, null, 0, 0, 1, 0);

-- 12 dec 18
create table if not exists colocation_application_ifr
(
	id int auto_increment
		primary key,
	parent_ifr_id int null,
	application_id int null,
	submission_date bigint null,
	state int null,
	type_name varchar(255) null,
	category_label varchar(255) null,
	category_name varchar(255) null,
	amount_label varchar(255) null,
	amount_name varchar(255) null,
	is_replied int(1) null,
	is_selected int(1) default '0' null,
	is_completed int(1) default '0' null,
	user_role_id int default '0' null,
	user_type int(1) default '0' null,
	deadline bigint null
);



create table if not exists colocation_dn
(
	id bigint not null
		primary key,
	parent_bill_id bigint not null,
	rack_cost double default '0' not null,
	ofc_cost double default '0' not null,
	power_cost double default '0' not null,
	advance_adjustment double default '0' not null,
	upgrade_cost double default '0' not null,
	downgrade_cost double default '0' not null,
	reconnect_cost double default '0' not null,
	closing_cost double default '0' not null
);


INSERT INTO `vbSequencer` (`table_name`, `next_id`, `table_LastModificationTime`) VALUES ('colocation_application_ifr', 1000, DEFAULT)

-- 18-12-18


INSERT INTO adrole (`rlRoleID`, `rlLevel`, `rlRoleName`, `rlRoleDesc`, `rlMaxClientPerDay`,
                    `rlRestrictedToOwn`, `rlLastModificationTime`, `rlIsDeleted`, `rlParentRoleID`) VALUES (20004, 100,
                                                                                                            'DGM Broadbrand-2', 'DGM Broadbrand-2', NULL, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO adrole (`rlRoleID`, `rlLevel`, `rlRoleName`, `rlRoleDesc`, `rlMaxClientPerDay`, `rlRestrictedToOwn`,
                    `rlLastModificationTime`, `rlIsDeleted`, `rlParentRoleID`) VALUES (20005, 100, 'AM Broadbrand-2', 'AM Broadbrand-2',
                                                                                       NULL, DEFAULT, DEFAULT, DEFAULT, DEFAULT);


INSERT INTO aduser (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`, `usStatus`, `usFullName`,
                    `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`, `usAddedBy`, `usAddTime`,
                    `usLastModifiedBy`, `usLastModificationTime`, `usBalance`, `usExchange`, `usProfilePicturePath`, `usLoginIPs`,
                    `usDeptName`, `usDistrictID`, `usUpazilaID`, `usUnionID`, `usIsDeleted`, `usIsBTCLPersonnel`, `usZoneID`)
VALUES (50231, 'dgm_broadbrand', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', 20004, '', 0, 'raihan', 'DGM Broadbrand-2',
        '', '', '', '', 0, 1505098390110, 4127, 1510554343227, 0.00000, 0, null, '', '', null, null, 0, 0, 1, 0);
INSERT INTO aduser (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`, `usStatus`, `usFullName`,
                    `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`, `usAddedBy`, `usAddTime`,
                    `usLastModifiedBy`, `usLastModificationTime`, `usBalance`, `usExchange`, `usProfilePicturePath`, `usLoginIPs`,
                    `usDeptName`, `usDistrictID`, `usUpazilaID`, `usUnionID`, `usIsDeleted`, `usIsBTCLPersonnel`, `usZoneID`)
VALUES (50232, 'am_broadbrand', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', 20005, '', 0, 'abc', 'AN Broadbrand-2', '', '',
        '', '', 0, 1505098390110, 4127, 1510554343227, 0.00000, 0, null, '', '', null, null, 0, 0, 1, 0);



 -- 19-12-18



create table if not exists colocation_common_cost_config_template
(
	id bigint auto_increment
		primary key,
	name varchar(255) not null,
	application_type int null
)
;



create table if not exists colocation_common_cost_config
(
	id bigint auto_increment
		primary key,
	type_id int(2) not null,
	price double(10,2) not null
)
;








-- dummy data insertion


INSERT INTO colocation_common_cost_config_template (id, name, application_type) VALUES (1, 'Securitymoney', 1);
INSERT INTO colocation_common_cost_config_template (id, name, application_type) VALUES (2, 'Vat', 1);
INSERT INTO colocation_cost_config (id, type_id, price, quantity_id) VALUES (5, 1, 10, 6);
INSERT INTO colocation_cost_config (id, type_id, price, quantity_id) VALUES (6, 1, 20, 7);
INSERT INTO colocation_cost_config (id, type_id, price, quantity_id) VALUES (7, 1, 35, 1);
INSERT INTO colocation_cost_config (id, type_id, price, quantity_id) VALUES (8, 4, 35, 1);
INSERT INTO colocation_cost_config (id, type_id, price, quantity_id) VALUES (9, 5, 100, 1);


INSERT INTO colocation_inventory_category (id, value) VALUES (1, 'Rack Size');
INSERT INTO colocation_inventory_category (id, value) VALUES (2, 'Power Type');
INSERT INTO colocation_inventory_category (id, value) VALUES (3, 'Fiber Type');
INSERT INTO colocation_inventory_category (id, value) VALUES (4, 'Rack Space');
INSERT INTO colocation_inventory_category (id, value) VALUES (5, 'Power Amount');

INSERT INTO colocation_inventory_template (id, type, value) VALUES (1, 1, '600*600');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (2, 1, '800*800');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (3, 1, '1000*1000');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (4, 2, 'AC');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (5, 3, 'Single');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (6, 4, 'Quarter');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (7, 4, 'Half');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (8, 4, 'Full');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (9, 2, 'DC');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (10, 5, '20');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (11, 6, '40');
INSERT INTO colocation_inventory_template (id, type, value) VALUES (12, 3, 'DUAL');


INSERT INTO colocation_common_cost_config (id, type_id, price) VALUES (1, 1, 6000);
INSERT INTO colocation_common_cost_config (id, type_id, price) VALUES (2, 2, 15);


-- end of dummy date insertion

-- 23-12-18

INSERT INTO colocation_connection (id, history_id, client_id, name, connection_type, rack_needed,
rack_size_id, rack_space_id, power_needed, power_amount, power_type_id, fiber_needed, fiber_core, fiber_type_id, active_from, active_to, valid_from, valid_to, status, start_date, incident, discount_rate)
VALUES (1002, 0, 413002, 'test', 1, 1, 1, 6, 1, 10, 9, 1, 2, 5, 1545216112992, 9223372036854775807, 1545216112992, 9223372036854775807, 1, 1545216112992, 1, 0);

INSERT INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('colocation_dn', 1, DEFAULT);

--27-dec-2018

create table if not exists colocation_inventory
(
	id bigint auto_increment
		primary key,
	cat_type_id int not null,
	template_type_id int not null,
	total_amount int not null,
	available_amount double default '0' not null,
	name varchar(255) null,
	model varchar(255) null,
	is_used int default '0' null
)
;




create table if not exists colocation_inventory_in_use
(
	id bigint auto_increment
		primary key,
	inventory_id int not null,
	client_id int not null,
	connection_id int not null,
	occupied_date bigint default '0' null,
	status int default '0' null
)
;


-- 7 jan 19



-- 8-jan-19



-- 14 jan 2018

-- flow

delete from flow_state where id>=6000;
delete from flow_state_transition where id>=6000;
delete from flow_state_transition_role where id>=6000;

INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6001, 'SUBMITTED', 1001, 'Submit Application', '#f99a2f', 'Application Submitted');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6002, 'PRECHECK_DONE', 1001, 'Submit Correction', '#008000', 'Client Correction Submitted');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6003, 'REJECTED', 1001, 'Reject Application', '#ff0000', 'Application Rejected');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6004, 'APPLICATION_REOPEN', 1001, 'Reopen Application', '#f99a2f', 'Application Reopened');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6005, 'IFR_SUBMITTED', 1001, ' Internal FR Request', '#f99a2f', 'Internal FR requested');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6006, 'IFR_RESPONDED', 1001, 'Response Internal FR ', '#008000', 'Internal FR Responded');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6007, 'DEMAND_NOTE_GENERATED', 1001, 'Generate Demand Note', '#f99a2f', 'Demand note generated');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6008, 'PAYMENT_DONE', 1001, 'Pay Now ', '#f99a2f', 'Payment Verification Needed');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6009, 'PAYMENT_VERIFIED', 1001, 'Verify Payment', '#008000', 'Payment Verified');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6010, 'ADVICE_NOTE_PUBLISH', 1001, 'Publish Advice Note', '#f99a2f', 'Advice note published');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6011, 'ADVICE_NOTE_FORWARD', 1001, 'Forward Advice Note to Power Division', '#008000', 'Advice Note Forwarded');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6012, 'SETUP_COMPLETE', 1001, 'Complete Setup Process', '#008000', 'Setup Process Completed');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6013, 'CLIENT_CORRECTION', 1001, 'Request Client For Correction', '#f99a2f', 'Client Correction Requested');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6014, 'REJECT_CLOSE', 1001, 'Reject and close application', '#f99a2f', 'Application Closed');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6015, 'ADVICE_NOTE_FORWARD_SPACE', 1001, 'Forward Advice Note to Fiber Division', '#008000', 'Advice Note Forwarded');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6016, 'CONNECTION_COMPLETE', 1001, 'Complete Connection', '#008000', 'Connection Completed');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6017, 'ADVICE_NOTE_FORWARD', 1001, 'Forward Advice Note', '#008000', 'Advice Note Forwarded');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6018, 'ADVICE_NOTE_FORWARD', 1001, 'Forward Advice Note', '#008000', 'Advice Note Forwarded');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6019, 'DEMAND_NOTE_SKIP', 1001, 'Generate Demand Note and Skip Payment', '#f99a2f', 'Payment Skipped');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6101, 'SUBMITTED', 1002, 'Submit Application', '#f99a2f', 'Application Submitted');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6102, 'PRECHECK_DONE', 1002, 'Submit Correction', '#008000', 'Client Correction Submitted');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6103, 'REJECTED', 1002, 'Reject Application', '#ff0000', 'Application Rejected');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6104, 'APPLICATION_REOPEN', 1002, 'Reopen Application', '#f99a2f', 'Application Reopened');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6105, 'ADVICE_NOTE_PUBLISH', 1002, 'Publish Advice Note', '#f99a2f', 'Advice note published');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6106, 'ADVICE_NOTE_FORWARD', 1002, 'Forward Advice Note to Power Division', '#008000', 'Advice Note Forwarded');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6107, 'SETUP_COMPLETE', 1002, 'Complete Setup Process', '#008000', 'Setup Process Completed');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6108, 'CLIENT_CORRECTION', 1002, 'Request Client For Correction', '#f99a2f', 'Client Correction Requested');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6109, 'REJECT_CLOSE', 1002, 'Reject and close application', '#f99a2f', 'Application Closed');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6110, 'ADVICE_NOTE_FORWARD_SPACE', 1002, 'Forward Advice Note to Fiber Division', '#008000', 'Advice Note Forwarded');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6112, 'CONNECTION_COMPLETE', 1002, 'Complete Connection', '#008000', 'Connection Completed');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6113, 'ADVICE_NOTE_FORWARD', 1002, 'Forward Advice Note', '#008000', 'Advice Note Forwarded');
INSERT INTO   flow_state (id, name, flow, description, color, view_description) VALUES (6114, 'ADVICE_NOTE_FORWARD', 1002, 'Forward Advice Note', '#008000', 'Advice Note Forwarded');

INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6001, 6001, 6003, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6002, 6001, 6013, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6003, 6001, 6005, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6004, 6003, 6004, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6005, 6004, 6013, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6006, 6004, 6005, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6007, 6005, 6006, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6008, 6006, 6007, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6009, 6007, 6008, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6010, 6008, 6009, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6011, 6009, 6010, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6012, 6010, 6011, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6013, 6011, 6012, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6014, 6006, 6014, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6015, 6002, 6003, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6016, 6002, 6005, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6017, 6010, 6015, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6018, 6015, 6012, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6019, 6015, 6017, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6020, 6017, 6012, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6021, 6011, 6018, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6022, 6018, 6012, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6023, 6012, 6016, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6024, 6013, 6002, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6025, 6006, 6019, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6026, 6019, 6010, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6101, 6101, 6103, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6102, 6101, 6108, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6103, 6101, 6105, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6104, 6103, 6104, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6105, 6104, 6108, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6106, 6104, 6105, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6107, 6105, 6106, '');
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6108, 6105, 6110, '');
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6109, 6106, 6107, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6110, 6106, 6113, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6111, 6113, 6107, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6112, 6114, 6107, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6113, 6110, 6107, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6114, 6110, 6114, null);
INSERT INTO   flow_state_transition (id, source, destination, comment) VALUES (6115, 6107, 6112, null);

INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6001, 6001, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6002, 6002, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6003, 6003, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6004, 6004, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6005, 6005, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6006, 6006, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6008, 6007, 20002);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6007, 6007, 20005);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6009, 6008, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6011, 6009, -1);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6012, 6010, 6020);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6013, 6011, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6015, 6012, 20004);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6016, 6013, 20005);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6010, 6014, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6014, 6017, 20003);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6017, 6018, 20002);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6018, 6018, 20005);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6019, 6019, 20004);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6020, 6020, 20002);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6021, 6020, 20005);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6022, 6021, 20003);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6023, 6022, 20002);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6024, 6022, 20005);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6025, 6023, 20003);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6026, 6024, -1);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6027, 6025, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6028, 6026, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6101, 6101, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6102, 6102, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6103, 6103, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6104, 6104, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6105, 6105, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6106, 6106, 20000);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6107, 6107, 20004);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6108, 6108, 20003);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6109, 6109, 20005);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6110, 6110, 20003);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6111, 6111, 20002);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6112, 6111, 20005);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6113, 6112, 20002);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6114, 6112, 20005);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6116, 6113, 20002);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6117, 6114, 20004);
INSERT INTO   flow_state_transition_role (id, flow_state_transition, role) VALUES (6118, 6115, 20003);


-- 15 jan 19

INSERT INTO adrole (rlRoleID, rlLevel, rlRoleName, rlRoleDesc, rlMaxClientPerDay, rlRestrictedToOwn, rlLastModificationTime, rlIsDeleted, rlParentRoleID)
VALUES (20006, 100, 'Administrative Authority', 'Administrative Authority', null, 0, 1547544521144, 0, 1);

INSERT INTO aduser (usUserID, usUserName, usPassword, usRoleID, usMailAddr, usStatus,
 usFullName, usDesignation, usAddress, usPhoneNo, usAdditionalInfo, usSecurityToken, usAddedBy,
 usAddTime, usLastModifiedBy, usLastModificationTime, usBalance, usExchange, usProfilePicturePath,
 usLoginIPs, usDeptName, usDistrictID, usUpazilaID, usUnionID, usIsDeleted, usIsBTCLPersonnel,
 usZoneID)
   VALUES (50233, 'admin_colocation', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', 20006, '', 0, 'abc',
 'Administrative Authority', '', '', '', '', 0, 1505098390110, 4127, 1510554343227, 0.00000, 0,
 null, '', '', null, null, 0, 0, 1, 0);

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (6020, 'APPROVE_BT_ADMIN', 1001, 'Approve and Proceed Application', '#008000', 'Application submitted and forwarded');

UPDATE flow_state_transition t SET t.`source` = 6020 WHERE t.`id` = 6001;
UPDATE flow_state_transition t SET t.`source` = 6020 WHERE t.`id` = 6002;
UPDATE flow_state_transition t SET t.`source` = 6020 WHERE t.`id` = 6003;
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (6027, 6001, 6020, 'NULL');
 INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
 VALUES (6029, 6027, 20006);

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (6028, 6001, 6003, 'NULL');
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (6030, 6028, 20006);

UPDATE flow_state_transition_role t SET t.`role` = 20006 WHERE t.`id` = 6004;

UPDATE flow_state_transition t SET t.`destination` = 6020
WHERE t.`id` = 6005;
DELETE FROM flow_state_transition_role WHERE `id` = 6001;
DELETE FROM flow_state_transition WHERE `id` = 6001;

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (6021, 'FORWARD_IFR_ADMIN', 1001, 'Forward for Admin Approval', '#008000', 'Application forwarded for Approval');
INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (6022, 'APPROVE_IFR_ADMIN', 1001, 'Approve IFR and Forward', '#008000', 'Application Approved for DN');


UPDATE flow_state_transition t SET t.`destination` = 6021 WHERE t.`id` = 6008;
UPDATE flow_state_transition t SET t.`source` = 6022 WHERE t.`id` = 6014;
UPDATE flow_state_transition t SET t.`source` = 6022 WHERE t.`id` = 6025;

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (6029, 6021, 6022, 'NULL');

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (6031, 6029, 20006);

UPDATE flow_state_transition t SET t.`destination` = 6007 WHERE t.`id` = 6014;

 INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
 VALUES (6030, 6021, 6014, 'NULL');

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (6032, 6030, 20006);

-- 16 jan 2019
INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (6115, 'APPROVE_BT_ADMIN', 1002, 'Approve and Proceed Application', '#008000', 'Application submitted and forwarded');
UPDATE flow_state_transition t SET t.`source` = 6115 WHERE t.`id` = 6103;
UPDATE flow_state_transition t SET t.`source` = 6115 WHERE t.`id` = 6101;
UPDATE flow_state_transition t SET t.`source` = 6115 WHERE t.`id` = 6102;
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (6116, 6101, 6115, 'NULL');
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (6119, 6116, 20006);
UPDATE flow_state_transition t SET t.`source` = 6101 WHERE t.`id` = 6101;
UPDATE flow_state_transition_role t SET t.`role` = 20006 WHERE t.`id` = 6101;
UPDATE flow_state_transition_role t SET t.`role` = 20006 WHERE t.`id` = 6104;
UPDATE flow_state_transition t SET t.`destination` = 6115 WHERE t.`id` = 6105;
UPDATE flow_state_transition_role t SET t.`role` = 20006 WHERE t.`id` = 6105;
DELETE FROM flow_state_transition_role WHERE `id` = 6106;
DELETE FROM flow_state_transition WHERE `id` = 6106;


-- 16 jan 2019 forhad
CREATE TABLE colocation_probable_td
(
    id bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    connection_id long NOT NULL,
    client_id long NOT NULL,
    day_left long,
    is_td_requested TINYINT DEFAULT 0
);
INSERT INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
 VALUES ('colocation_cost_config', 100, DEFAULT);

INSERT INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('colocation_inventory', 1, DEFAULT);

-- 17 jan 2019 forhad
-- CoLocation TD flow
INSERT INTO flow (`id`, `name`, `component`) VALUES (1003, 'Temporary Disconnect', 10);

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6201, 'SUBMITTED', 1003, 'Submit Application', '#f99a2f', 'Application Submitted');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6202, 'REJECTED', 1003, 'Reject Application', '#ff0000', 'Application Rejected');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6203, 'REJECT_CLOSE', 1003, 'Reject and close application', '#f99a2f', 'Application Closed');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6204, 'ADVICE_NOTE_PUBLISH', 1003, 'Publish Advice Note', '#f99a2f', 'Advice note published');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6205, 'CONNECTION_COMPLETE', 1003, 'Complete Connection', '#008000', 'Connection Completed');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6206, 'APPROVE_BT_ADMIN', 1003, 'Approve and Proceed Application', '#008000', 'Application submitted and forwarded');

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (6201, 6201, 6202, 'NULL');

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (6202, 6201, 6206, 'NULL');

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (6203, 6206, 6204, 'NULL');

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6201, 6201, 20006);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6202, 6202, 20006);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6203, 6203, 20000);

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (6204, 6204, 6205, 'NULL');

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6204, 6204, 20003);


-- 20 jan 2019 forhad

-- flow
INSERT INTO `flow` (`id`, `name`, `component`) VALUES (1005, 'Reconnect', 10);

-- flow state
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6501, 'RECONNECT_APPLICATION_SUBMITTED', 1005, 'Submit Application', '#f99a2f', 'Application Submitted');

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6502, 'RECONNECT_APPLICATION_REJECTED', 1005, 'Reject Application', '#ff0000', 'Application Rejected');

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6503, 'RECONNECT_APPLICATION_DEMAND_NOTE_GENERATED', 1005, 'Generate Demand Note', '#f99a2f', 'Demand note generated');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6508, 'RECONNECT_APPLICATION_APPROVE_BY_ADMIN', 1005, 'Approve and Proceed Application', '#f99a2f', 'Application submitted and forwarded');

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6504, 'RECONNECT_APPLICATION_PAYMENT_DONE', 1005, 'Pay Now', '#f99a2f', 'Payment Verification Needed');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6505, 'RECONNECT_APPLICATION_PAYMENT_VERIFIED', 1005, 'Verify Payment', '#008000', 'Payment Verified');

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6507, 'RECONNECT_APPLICATION_CONNECTION_COMPLETE', 1005, 'Complete Connection', '#008000', 'Connection Completed');

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (6506, 'RECONNECT_APPLICATION_ADVICE_NOTE_PUBLISH', 1005, 'Publish Advice Note', '#f99a2f', 'Advice note published');


-- transition
-- AO
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (6501, 6501, 6502, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (6502, 6501, 6508, 'NULL');

-- dgm_domain
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (6503, 6508, 6503, 'NULL');

 INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (6504, 6503, 6504, 'client to revenue');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (6505, 6504, 6505, 'revenue to dgm_domain');

 INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (6506, 6505, 6506, 'dgm domain to am domain');

 INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (6507, 6506, 6507, 'complete ');



-- transition role
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6501, 6501, 20006);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6502, 6502, 20006);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6503, 6503, 20000);

 INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6504, 6504, -1);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6505, 6505, 6020);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6506, 6506, 20000);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6507, 6507, 20003);


-- menu
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (4013, 4010, 'TD', 4);

 INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (4014, 4010, 'Reconnect', 4);

 INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (4015, 4010, 'Close Connection', 4);

-- 21 January 2019
ALTER TABLE colocation_probable_td ADD history_id long NULL;

-- 22 January 2019
UPDATE `flow_state` t SET t.`description` = 'Complete Close Connection', t.`view_description` = 'Connection Close Completed' WHERE t.`id` = 6205;

UPDATE `flow_state` t SET t.`view_description` = 'Reconnect Completed' WHERE t.`id` = 6507;

-- 23 January 2019
UPDATE `flow_state` t SET t.`description` = 'Complete Temporary Disconnect', t.`view_description` = 'Temporary Disconnect Completed' WHERE t.`id` = 6205;

-- 27 January 2019
ALTER TABLE colocation_inventory_template ADD description text NULL;

UPDATE `colocation_inventory_template` t SET t.`description` = 'Full ' WHERE t.`id` = 8;

UPDATE `colocation_inventory_template` t SET t.`description` = 'Half ' WHERE t.`id` = 7;

UPDATE `colocation_inventory_template` t SET t.`description` = 'Quarter ' WHERE t.`id` = 6;

UPDATE `colocation_inventory_template` t SET t.`value` = 'DC(Amp.)' WHERE t.`id` = 9;

UPDATE `colocation_inventory_template` t SET t.`value` = 'AC(Watt)' WHERE t.`id` = 4;

UPDATE `colocation_inventory_template` t SET t.`value` = 'Dual' WHERE t.`id` = 12;



INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (6040, 6001, 6013, 'NULL');

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (6040, 6040, 20006);

UPDATE `flow_state_transition_role` t SET t.`role` = 20000 WHERE t.`id` = 6040; -- administrative authority Client Correction commented


UPDATE `flow_state_transition_role` t SET t.`role` = 20000 WHERE t.`id` = 6041;
UPDATE `flow_state_transition_role` t SET t.`role` = 20000 WHERE t.`id` = 6042;


 UPDATE `flow_state` t SET t.`description` = 'Internal FR Request to Space & Fiber team and Power Team' WHERE t.`id` = 6005;


-- 16 April 2019
insert into colocation_inventory_category(id,value) values(9, 'Floor Space');
insert into colocation_inventory_template(type, value, description) values(9,'With Air Cooler',NULL), (9,'Without Air Cooler',NULL);


alter table colocation_application add column floor_space_needed int(1);
alter table colocation_application add column floor_space_amount double(11,2);
alter table colocation_application add column floor_space_type int(1);


alter table colocation_connection add column floor_space_needed int(1);
alter table colocation_connection add column floor_space_amount double(11,2);
alter table colocation_connection add column floor_space_type int(1);

ALTER TABLE `colocation_dn`
ADD COLUMN `floor_space_cost` double NOT NULL AFTER `is_yearly_demand_note`;
