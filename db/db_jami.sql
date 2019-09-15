CREATE TABLE `nix_application` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`client` BIGINT(20) NOT NULL DEFAULT '0',
	`type` INT(11) NOT NULL DEFAULT '0',
	`state` INT(11) NOT NULL DEFAULT '0',
	`demand_note` BIGINT(20) NOT NULL DEFAULT '0',
	`loop_provider` INT(11) NOT NULL DEFAULT '0',
	`zone` BIGINT(20) NOT NULL DEFAULT '0',
	`submission_date` BIGINT(20) NOT NULL DEFAULT '0',
	`suggested_date` BIGINT(20) NOT NULL DEFAULT '0',
	`rejection_comment` VARCHAR(255) NULL DEFAULT NULL,
	`skip_payment` INT(11) NOT NULL DEFAULT '0',
	`created` BIGINT(20) NOT NULL DEFAULT '0',
	`modified` BIGINT(20) NOT NULL DEFAULT '0',
	`port_count` INT(11) NULL DEFAULT '0',
	`port_type` INT(11) NULL DEFAULT '0',
	`is_forwarded` INT(11) NULL DEFAULT '0',
	`is_service_started` INT(11) NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=16002
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_application', 1, 0);


CREATE TABLE `nix_application_local_loop` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`office_id` BIGINT(20) NOT NULL DEFAULT '0',
	`ofc_type` INT(11) NOT NULL DEFAULT '0',
	`pop_id` BIGINT(20) NOT NULL DEFAULT '0',
	`vlan_id` BIGINT(20) NOT NULL DEFAULT '0',
	`router_switch_id` BIGINT(20) NOT NULL DEFAULT '0',
	`port_id` BIGINT(20) NOT NULL DEFAULT '0',
	`port_type` BIGINT(20) NOT NULL DEFAULT '0',
	`client_distance` BIGINT(20) NOT NULL DEFAULT '0',
	`btcl_distance` BIGINT(20) NOT NULL DEFAULT '0',
	`ocd_distance` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=14002
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_application_local_loop', 1, 0);


CREATE TABLE `nix_application_office` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(127) NULL DEFAULT NULL,
	`address` VARCHAR(1023) NULL DEFAULT NULL,
	`application` BIGINT(20) NOT NULL DEFAULT '0',
	`history` BIGINT(20) NOT NULL DEFAULT '0',
	`created` BIGINT(20) NOT NULL DEFAULT '0',
	`modified` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=10002
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_application_office', 1, 0);


CREATE TABLE `nix_connection` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`application_id` BIGINT(20) NOT NULL DEFAULT '0',
	`status` INT(11) NOT NULL DEFAULT '0',
	`client` BIGINT(20) NULL DEFAULT '0',
	`name` VARCHAR(200) NULL DEFAULT NULL,
	`connection` BIGINT(20) NULL DEFAULT '0',
	`active_from` BIGINT(20) NULL DEFAULT '0',
	`active_to` BIGINT(20) NULL DEFAULT '0',
	`valid_from` BIGINT(20) NULL DEFAULT '0',
	`start_date` BIGINT(20) NULL DEFAULT '0',
	`incident` INT(11) NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=4002
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_connection', 1, 0);



CREATE TABLE `nix_demand_note` (
	`id` BIGINT(20) NOT NULL,
	`security_money` DOUBLE NULL DEFAULT '0',
	`registration_charge` DOUBLE NULL DEFAULT '0',
	`fiber_charge` DOUBLE NULL DEFAULT '0',
	`local_loop_charge` DOUBLE NULL DEFAULT '0',
	`advance_adjustment` DOUBLE NULL DEFAULT '0',
	`lli_nc_dn_parent_bill_id` BIGINT(20) NULL DEFAULT '0',
	`port_charge` DOUBLE NULL DEFAULT '0',
	`port_upgrade_charge` DOUBLE NULL DEFAULT '0',
	`port_downgrade_charge` DOUBLE NULL DEFAULT '0',
	`instant_degradation_charge` DOUBLE NULL DEFAULT '0',
	`closing_charge` DOUBLE NULL DEFAULT '0',
	`reconnect_charge` DOUBLE NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_demand_note', 1, 0);


CREATE TABLE `nix_downgrade_application` (
	`id` BIGINT(20) NOT NULL,
	`parent_application` BIGINT(20) NULL DEFAULT '0',
	`office_id` BIGINT(20) NULL DEFAULT '0',
	`old_port_id` BIGINT(20) NULL DEFAULT '0',
	`old_port_type` INT(11) NULL DEFAULT '0',
	`new_port_type` INT(11) NULL DEFAULT '0',
	`new_port_id` BIGINT(20) NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_downgrade_application', 1, 0);


CREATE TABLE `nix_efr` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`application` BIGINT(20) NOT NULL DEFAULT '0',
	`source` VARCHAR(1023) NULL DEFAULT NULL,
	`source_type` INT(11) NOT NULL DEFAULT '0',
	`destination` VARCHAR(1023) NULL DEFAULT NULL,
	`destination_type` INT(11) NOT NULL DEFAULT '0',
	`vendor` BIGINT(20) NOT NULL DEFAULT '0',
	`vendor_type` INT(11) NOT NULL DEFAULT '0',
	`work_given` INT(11) NOT NULL DEFAULT '0',
	`work_completed` INT(11) NOT NULL DEFAULT '0',
	`deadline` BIGINT(20) NOT NULL DEFAULT '0',
	`proposed_loop_distance` DOUBLE NOT NULL DEFAULT '0',
	`actual_loop_distance` DOUBLE NOT NULL DEFAULT '0',
	`loop_distance_approved` DOUBLE NOT NULL DEFAULT '0',
	`pop` BIGINT(20) NOT NULL DEFAULT '0',
	`ofc_type` INT(11) NOT NULL DEFAULT '0',
	`created` BIGINT(20) NOT NULL DEFAULT '0',
	`modified` BIGINT(20) NOT NULL DEFAULT '0',
	`is_forwarded` INT(11) NULL DEFAULT '0',
	`office` BIGINT(20) NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=13002
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_efr', 1, 0);


CREATE TABLE `nix_ifr` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`application` BIGINT(20) NOT NULL DEFAULT '0',
	`office` BIGINT(20) NOT NULL DEFAULT '0',
	`pop` BIGINT(20) NOT NULL DEFAULT '0',
	`replied` INT(11) NOT NULL DEFAULT '0',
	`selected` INT(11) NOT NULL DEFAULT '0',
	`ignored` INT(11) NOT NULL DEFAULT '0',
	`submission_date` BIGINT(20) NOT NULL DEFAULT '0',
	`created` BIGINT(20) NOT NULL DEFAULT '0',
	`modified` BIGINT(20) NOT NULL DEFAULT '0',
	`is_forwarded` INT(11) NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=20003
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_ifr', 1, 0);

CREATE TABLE `nix_local_loop` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`application_local_loop` BIGINT(20) NOT NULL DEFAULT '0',
	`connection` BIGINT(20) NOT NULL DEFAULT '0',
	`office` BIGINT(20) NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3002
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_local_loop', 1, 0);


CREATE TABLE `nix_office` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`application` BIGINT(20) NOT NULL DEFAULT '0',
	`connection` BIGINT(20) NOT NULL DEFAULT '0',
	`name` VARCHAR(200) NULL DEFAULT NULL,
	`application_office` BIGINT(20) NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=4002
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_office', 1, 0);

CREATE TABLE `nix_port_charge_conf` (
	`id` INT(11) NOT NULL,
	`port_type` INT(11) NULL DEFAULT '0',
	`port_charge` INT(11) NULL DEFAULT '0',
	`registration_charge` INT(11) NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_port_charge_conf', 1, 0);


CREATE TABLE `nix_upgrade_application` (
	`id` BIGINT(20) NOT NULL,
	`parent_application` BIGINT(20) NULL DEFAULT '0',
	`office_id` BIGINT(20) NULL DEFAULT '0',
	`old_port_id` BIGINT(20) NULL DEFAULT '0',
	`old_port_type` INT(11) NULL DEFAULT '0',
	`new_port_type` INT(11) NULL DEFAULT '0',
	`new_port_id` BIGINT(20) NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_upgrade_application', 1, 0);

-- 31/12/018


-- flow state NIX Application with loop provider BTCL

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5001, 'NIX_APPLICATION_SUBMIT', 501, 'Nix application submitted', '#008000', 'Nix application submited');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5002, 'NIX_APPLICATION_REJECT', 501, 'Nix application rejected', '#008000', 'Nix application rejected');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5003, 'NIX_APPLICATION_REOPEN', 501, 'Nix application reopened', '#008000', 'Nix application reopened');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5004, 'NIX_APPLICATION_CORRECTION', 501, 'Nix application correction', '#008000', 'Nix application correction');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5005, 'NIX_APPLICATION_CORRECTION_DONE', 501, 'Nix application correction done', '#008000', 'Nix application correction done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5006, 'NIX_APPLICATION_IFR', 501, 'Nix application ifr requested', '#008000', 'Nix application ifr requested');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5007, 'NIX_APPLICATION_IFR_RESPONDED', 501, 'Nix application ifr responded', '#008000', 'Nix application ifr responded');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5008, 'NIX_APPLICATION_EFR', 501, 'Nix application efr requested', '#008000', 'Nix application efr requested');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5009, 'NIX_APPLICATION_EFR_RESPONDED', 501, 'Nix application responded', '#008000', 'Nix application responded');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5010, 'NIX_APPLICATION_DEMAND_NOTE_GENERATED', 501, 'Nix application demand note generated', '#008000', 'Nix application demand note generated');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5011, 'NIX_APPLICATION_PAYMENT_DONE', 501, 'Nix application payment done', '#008000', 'Nix application payment done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5012, 'NIX_APPLICATION_PAYMENT_VERIFIED', 501, 'Nix application payment verified', '#008000', 'Nix application payment verified');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5013, 'NIX_APPLICATION_WORK_ORDER_GENERATED', 501, 'Nix application work order generated ', '#008000', 'Nix application work order generated');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5014, 'NIX_APPLICATION_WORK_DONE', 501, 'Nix application work done ', '#008000', 'Nix application work done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5015, 'NIX_APPLICATION_PUBLISH_ADVICE_NOTE', 501, 'Nix application advice note published', '#008000', 'Nix application advice note published');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5016, 'SERVER_ROOM_TESTING_AND_COMPLETE', 501, 'Server room test and complete', '#008000', 'Server room test and complete ');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5018, 'FORWARD_TO_LDGM_FOR_EFR', 501, 'Forward to LDGM for EFR', '#008000', 'Forwareded to LDGM for EFR ');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5019, 'GENERATE_WORK_ORDER', 501, 'Generate work order', '#008000', 'Work Order generated');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5017, 'FORWARD_TO_LDGM', 501, 'FORWARD_TO_LDGM', '#89CFF0', 'forwarded to ldgm');

-- flow state with loop provider client
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5024, 'WITHOUT_LOOP_IFR_RESPONDED', 501, 'Without loop ifr responded', '#008000', 'without loop ifr responded');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5025, 'WITHOUT_LOOP_DEMAND_NOTE_GENERATED', 501, 'Without loop demand note generated', '#008000', 'without loop demand note generated');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5026, 'WITHOUT_LOOP_PAYMENT_DONE', 501, 'Without loop payment done', '#008000', 'Without loop payment done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5027, 'WITHOUT_LOOP_PAYMENT_VERIFIED', 501, 'Without loop payment verified', '#008000', 'Without loop payment verified');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5028, 'WITHOUT_LOOP_PUBLISH_ADVICE_NOTE', 501, 'Without loop advice note published', '#008000', 'Without loop advice note published');


-- 1/9/2019

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5020, 'REQUEST_FOR_CC', 501, 'Request for cc', '#008000', 'Requested for cc');

-- INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5017, 'NIX_APPLICATION_FORWARD_ADVICE_NOTE', 501, 'Nix application advice note forwarded', '#008000', 'Nix application advice note forwarded');


-- flow state transition

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5001, 5001, 5002, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5002, 5002, 5003, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5003, 5001, 5006, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5004, 5001, 5004, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5005, 5003, 5006, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5006, 5004, 5005, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5007, 5005, 5006, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5008, 5006, 5007, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5009, 5007, 5008, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5010, 5008, 5009, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5011, 5009, 5010, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5012, 5010, 5011, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5013, 5011, 5012, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5014, 5012, 5013, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5015, 5013, 5014, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5016, 5014, 5015, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5017, 5012, 5017, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5019, 5015, 5016, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5020, 5017, 5013, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5021, 5007, 5018, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5022, 5018, 5008, 'NULL');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5029, 5012, 5019, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5030, 5019, 5014, 'NULL');

-- flow state transition

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5024, 5024, 5025, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5025, 5025, 5026, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5026, 5026, 5027, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5027, 5027, 5028, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5028, 5028, 5016, 'NULL');


-- 1/9/2019
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5023, 5001, 5020, 'NULL');



 -- flow state transition role
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5001, 5001, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5002, 5002, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5003, 5003, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5004, 5004, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5005, 5005, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5006, 5006, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5007, 5007, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5008, 5008, 16021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5009, 5009, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5010, 5010, 16020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5011, 5011, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5012, 5012, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5013, 5013, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5014, 5014, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5015, 5015, 16020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5016, 5016, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5017, 5017, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5018, 5018, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5019, 5019, 16021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5020, 5020, 22020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5021, 5021, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5022, 5022, 22020);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5029, 5021, 22020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5030, 5022, 16020);

-- 1/9/2019
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5023, 5023, 22021);
-- INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5017, 'NIX_APPLICATION_FORWARD_ADVICE_NOTE', 501, 'Nix application advice note forwarded', '#008000', 'Nix application advice note forwarded');

-- flow state transition role
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5024, 5024, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5025, 5025, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5026, 5026, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5027, 5027, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5028, 5028, 16021);

-- INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5020, 5020, 16021);



-- 1/13/2019
ALTER TABLE `nix_application` ADD COLUMN `status` int(11) NULL DEFAULT '0';
ALTER TABLE `nix_application_local_loop` ADD COLUMN `vendor_id` BIGINT NULL default 0;

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5021, 'ACCOUNT_CC_POSITIVE', 501, 'Account Payment Condition Positive', '#008000', 'Account Payment Condition Positive');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5022, 'ACCOUNT_CC_NEGETIVE', 501, 'Account Payment Condition Negative', '#ff0000', 'Account Payment Condition Negative');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5023, 'REJECT_WO_FORWARD_TO_VENDOR', 501, 'Reject Work order and forward to vendor', '#ff0000', 'Reject Work order and forward to vendor');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5031, 5020, 5021, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5032, 5020, 5022, 'NULL');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5033, 5022, 5020, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5034, 5022, 5002, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5035, 5022, 5006, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5036, 5022, 5004, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5037, 5021, 5002, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5038, 5021, 5006, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5039, 5021, 5004, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5040, 5005, 5020, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5041, 5005, 5004, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5042, 5005, 5002, 'NULL');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5043, 5003, 5002, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5044, 5003, 5004, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5045, 5003, 5020, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5046, 5023, 5014, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5047, 5014, 5023, 'NULL');


INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5031, 5031, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5032, 5032, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5033, 5033, 22021);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5034, 5034, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5035, 5035, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5036, 5036, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5037, 5037, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5038, 5038, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5039, 5039, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5040, 5040, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5041, 5041, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5042, 5042, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5043, 5043, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5044, 5044, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5045, 5045, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5046, 5046, 16020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5047, 5047,22021);


-- 15/01/2019


-- 16/01/2019

CREATE TABLE `nix_ip_vs_connection` (
	`id` BIGINT(20) NOT NULL,
	`connection_id` BIGINT(20) NOT NULL,
	`ip_usage_id` BIGINT(20) NOT NULL,
	`routing_info_id` BIGINT(20) NOT NULL,
	`ip_usage_type` VARCHAR(50) NOT NULL,
	`creation_time` BIGINT(20) NOT NULL,
	`last_modification_time` BIGINT(20) NOT NULL,
	`active_from` BIGINT(20) NOT NULL,
	`active_to` BIGINT(20) NOT NULL,
	`is_deleted` TINYINT(1) NOT NULL,
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
ROW_FORMAT=DYNAMIC
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_ip_vs_connection', 1, 0);
ALTER TABLE `nix_application` ADD COLUMN `connection` BIGINT  NULL DEFAULT '0';
ALTER TABLE `nix_efr` ADD COLUMN `is_selected` INT NULL default 0;

-- 1/20/19

CREATE TABLE `nix_probable_td_client` (
	`id` BIGINT(20) NOT NULL,
	`clientID` BIGINT(20) NULL DEFAULT NULL,
	`tdDate` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`id`) USING BTREE
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_probable_td_client', 1, 0);
ALTER TABLE `nix_efr` ADD COLUMN `parent_efr` BIGINT  NULL DEFAULT '0';

-- flow state NIX close Application

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5200, 'NIX_CLOSE_APPLICATION_SUBMIT', 501, 'Nix close application submitted', '#008000', 'Nix close application submited');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5201, 'NIX_CLOSE_APPLICATION_REJECT', 501, 'Nix close application rejected', '#008000', 'Nix close application rejected');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5202, 'NIX_CLOSE_APPLICATION_REOPEN', 501, 'Nix close application reopened', '#008000', 'Nix close application reopened');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5203, 'NIX_CLOSE_APPLICATION_CORRECTION', 501, 'Nix close application correction', '#008000', 'Nix close application correction');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5204, 'NIX_CLOSE_APPLICATION_CORRECTION_DONE', 501, 'Nix close  application correction done', '#008000', 'Nix close application correction done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5205, 'NIX_CLOSE_APPLICATION_DEMAND_NOTE_GENERATED', 501, 'Nix close application demand note generated', '#008000', 'Nix close application demand note generated');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5206, 'NIX_CLOSE_APPLICATION_PAYMENT_DONE', 501, 'Nix close  application payment done', '#008000', 'Nix close application payment done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5207, 'NIX_CLOSE_APPLICATION_PAYMENT_VERIFIED', 501, 'Nix close application payment verified', '#008000', 'Nix close application payment verified');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5208, 'NIX_CLOSE_APPLICATION_WORK_ORDER_GENERATE', 501, 'Nix close application Work order generate', '#008000', 'Nix close application work order genrate');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5209, 'NIX_CLOSE_APPLICATION_JOB_DONE', 501, 'Nix close application job done', '#008000', 'Nix close application work order job done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5210, 'NIX_CLOSE_APPLICATION_PUBLISH_ADVICE_NOTE', 501, 'Nix close application advice note published', '#008000', 'Nix close application advice note published');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5111, 'SERVER_ROOM_TESTING_AND_COMPLETE', 501, 'Server room test and complete', '#008000', 'Server room test and complete ');


-- flow state transition

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5200, 5200, 5201, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5201, 5201, 5202, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5202, 5200, 5205, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5203, 5200, 5203, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5204, 5202, 5205, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5205, 5203, 5204, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5206, 5204, 5205, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5207, 5205, 5206, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5208, 5206, 5207, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5209, 5207, 5208, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5210, 5208, 5209, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5211, 5209, 5210, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5212, 5210, 5211, 'NULL');


-- flow state transition role

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5200, 5200, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5201, 5201, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5202, 5202, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5203, 5203, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5204, 5204, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5205, 5205, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5206, 5206, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5207, 5207, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5208, 5208, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5209, 5209, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5210, 5210, 16020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5211, 5211, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5212, 5212, 16021);

-- 1/21/19

CREATE TABLE `nix_client_td` (
	`id` BIGINT(20) NOT NULL,
	`clientID` BIGINT(20) NOT NULL,
	`tdFrom` BIGINT(20) NOT NULL,
	`tdTo` BIGINT(20) NOT NULL,
	`state` INT(11) NULL DEFAULT NULL,
	`comment` VARCHAR(255) NULL DEFAULT NULL,
	`advicedDate` BIGINT(20) NULL DEFAULT NULL,
	`appliedDate` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`id`) USING BTREE
)
	COLLATE='latin1_swedish_ci'
	ENGINE=InnoDB
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_client_td', 1, 0);


CREATE TABLE `nix_connection_revise_client` (
	`id` BIGINT(20) NOT NULL,
	`clientID` BIGINT(20) NOT NULL,
	`applicationType` INT(11) NOT NULL,
	`state` INT(11) NULL DEFAULT NULL,
	`isDemandNoteNeeded` TINYINT(255) NOT NULL,
	`description` VARCHAR(255) NULL DEFAULT NULL,
	`suggestedDate` BIGINT(20) NULL DEFAULT NULL,
	`appliedDate` BIGINT(20) NULL DEFAULT NULL,
	`demandNoteID` BIGINT(20) NULL DEFAULT NULL,
	`bandwidth` FLOAT NULL DEFAULT '0',
	`referenceContract` BIGINT(20) NULL DEFAULT '0',
	`isCompleted` INT(11) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_connection_revise_client', 1, 0);

-- 1/22/19
ALTER TABLE nix_connection ADD valid_to BIGINT DEFAULT 0 NULL;

ALTER TABLE nix_port_charge_conf ADD upgrade_charge INT DEFAULT 0 NULL;
ALTER TABLE nix_port_charge_conf ADD downgrade_charge INT DEFAULT 0 NULL;
ALTER TABLE nix_port_charge_conf ADD close_charge INT DEFAULT 0 NULL;
ALTER TABLE nix_port_charge_conf ADD reconnect_charge INT DEFAULT 0 NULL;

-- 1/23/19
ALTER TABLE nix_connection ADD zone INT(11) DEFAULT 0 NULL;


-- flow state NIX Downgrade Application

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5100, 'NIX_APPLICATION_SUBMIT', 501, 'Nix downgrade application submitted', '#008000', 'Nix downgrade application submited');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5101, 'NIX_APPLICATION_REJECT', 501, 'Nix downgrade application rejected', '#008000', 'Nix downgrade application rejected');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5102, 'NIX_APPLICATION_REOPEN', 501, 'Nix downgrade application reopened', '#008000', 'Nix downgrade application reopened');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5103, 'NIX_APPLICATION_CORRECTION', 501, 'Nix downgrade application correction', '#008000', 'Nix downgrade application correction');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5104, 'NIX_APPLICATION_CORRECTION_DONE', 501, 'Nix downgrade  application correction done', '#008000', 'Nix downgrade application correction done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5105, 'NIX_APPLICATION_DEMAND_NOTE_GENERATED', 501, 'Nix downgrade application demand note generated', '#008000', 'Nix downgrade application demand note generated');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5106, 'NIX_APPLICATION_PAYMENT_DONE', 501, 'Nix downgrade  application payment done', '#008000', 'Nix downgrade application payment done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5107, 'NIX_APPLICATION_PAYMENT_VERIFIED', 501, 'Nix downgrade application payment verified', '#008000', 'Nix downgrade application payment verified');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5108, 'NIX_APPLICATION_PUBLISH_ADVICE_NOTE', 501, 'Nix downgrade application advice note published', '#008000', 'Nix downgrade application advice note published');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (5109, 'SERVER_ROOM_TESTING_AND_COMPLETE', 501, 'Server room test and complete', '#008000', 'Server room test and complete ');


-- flow state transition

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5100, 5100, 5101, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5101, 5101, 5102, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5102, 5100, 5105, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5103, 5100, 5103, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5104, 5102, 5105, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5105, 5103, 5104, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5106, 5104, 5105, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5107, 5105, 5106, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5108, 5106, 5107, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5109, 5107, 5108, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5110, 5108, 5109, 'NULL');


-- flow state transition role

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5100, 5100, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5101, 5101, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5102, 5102, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5103, 5103, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5104, 5104, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5105, 5105, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5106, 5106, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5107, 5107, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5108, 5108, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5109, 5109, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5110, 5110, 16021);

-- 1/24/19

ALTER TABLE nix_application_local_loop ADD application BIGINT DEFAULT 0 NULL;

-- 1/27/19
CREATE TABLE `nix_close_application` (
	`id` BIGINT(20) NOT NULL,
	`parent_application` BIGINT(20) NOT NULL,
	`port_id` BIGINT(11) NOT NULL,
	`port_type` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_close_application', 1, 0);

INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9008, 9001, 'Connection', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9009, 9001, 'New Connection', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9010, 9001, 'Existing Connection', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9011, 9001, 'Application Search', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9012, 9001, 'Connection Search', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9013, 9010, 'Revise Connection', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9014, 9010, 'Connected Client Management', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9015, 9013, 'Upgrade', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9016, 9013, 'Downgrade', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9017, 9013, 'Close', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9018, 9014, 'Revise Search', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9019, 9014, 'Probable TD', 9);
INSERT INTO admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9020, 9014, 'Reconnect', 9);

-- 1/29/19


CREATE TABLE `nix_common_config` (
	`id` BIGINT(20) NOT NULL,
	`type` INT(11) NOT NULL,
	`price` BIGINT(20) NOT NULL,
	PRIMARY KEY (`id`)
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
;
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_common_config', 1, 0);
ALTER TABLE nix_efr ADD quotation_status INT DEFAULT 0 NULL;

-- 1/31/19


CREATE TABLE IF NOT EXISTS `nix_demand_note_adjustment` (
	`id` BIGINT(20) NOT NULL,
	`clientId` BIGINT(20) NOT NULL,
	`demandNoteId` BIGINT(20) NOT NULL,
	`connectionType` INT(8) NOT NULL,
	`createdDate` BIGINT(20) NULL DEFAULT NULL,
	`lastModifiedDate` BIGINT(20) NULL DEFAULT NULL,
	`activeFrom` BIGINT(20) NULL DEFAULT NULL,
	`loopCharge` DOUBLE NULL DEFAULT '0',
	`totalDue` DOUBLE NULL DEFAULT '0',
	`vatRate` DOUBLE NULL DEFAULT '0',
	`vat` DOUBLE NULL DEFAULT '0',
	`isPending` TINYINT(4) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`) USING BTREE
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
;

INSERT IGNORE INTO `vbsequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_demand_note_adjustment', 1, 0);



-- 2/4/19

-- flow state Application

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (500, 'APPLICATION_SUBMIT', 50, 'application submitted', '#008000', 'Application submited');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (501, 'APPLICATION_REJECT', 50, 'Reject application', '#008000', 'Application rejected');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (502, 'APPLICATION_REOPEN', 50, 'Reopen application', '#008000', 'Application reopened');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (503, 'APPLICATION_CORRECTION', 50, 'Request for client correction', '#008000', 'Client correction requested');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (504, 'APPLICATION_CORRECTION_DONE', 50, ' Submit Correction', '#008000', 'Client correction submitted');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (505, 'REQUEST_FOR_CC', 50, 'Request for cc', '#008000', 'Requested for cc');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (506, 'ACCOUNT_CC_POSITIVE', 50, 'Account Payment Condition Positive', '#008000', 'Account Payment Condition Positive');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (507, 'ACCOUNT_CC_NEGETIVE', 50, 'Account Payment Condition Negative', '#ff0000', 'Account Payment Condition Negative');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (508, 'FORWARD_TO_BUYER', 50, 'Forward to Buyer', '#008000', 'Forwared to Buyer');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (509, 'RESPONSE_BACK', 50, 'Response Back', '#008000', 'Responded Back');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (510, 'APPLICATION_DEMAND_NOTE_GENERATED', 50, 'Generate Demand Note', '#008000', 'Demand Note Generated');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (511, 'APPLICATION_PAYMENT_DONE', 50, 'Give Payment', '#008000', 'Payment Done');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (512, 'APPLICATION_PAYMENT_VERIFIED', 50, 'Verify Payment', '#008000', 'Payment Verified');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (513, 'APPLICATION_PUBLISH_ADVICE_NOTE', 50, 'Publish Advice Note', '#008000', 'Advice Note Published');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (514, 'SERVER_ROOM_TESTING_AND_COMPLETE', 50, 'Test and Complete', '#008000', 'Application Completed');

-- flow state transition

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (500, 500, 501, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (501, 500, 503, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (503, 500, 505, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (504, 500, 508, 'NULL');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (505, 501, 502, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (506, 502, 508, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (507, 503, 504, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (508, 504, 508, 'NULL');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (509, 505, 506, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (510, 505, 507, 'NULL');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (511, 507, 501, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (512, 507, 503, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (513, 507, 508, 'NULL');


INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (514, 506, 501, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (515, 506, 503, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (516, 506, 508, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (517, 506, 505, 'NULL');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (518, 508, 509, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (519, 509, 510, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (520, 510, 511, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (521, 511, 512, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (522, 512, 513, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (523, 513, 514, 'NULL');


-- flow state transition role

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (500, 500, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (501, 501, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (503, 503, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (504, 504, 22021);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (505, 505, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (506, 506, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (507, 507, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (508, 508, 22021);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (509, 509, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (510, 510, 6020);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (511, 511, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (512, 512, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (513, 513, 22021);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (514, 514, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (515, 515, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (516, 516, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (517, 517, 22021);

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (518, 518, -2);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (519, 519, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (520, 520, -2);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (521, 521, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (522, 522, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (523, 523, 16021);




CREATE TABLE IF NOT EXISTS `lli_owner_change_application` (
	`id` BIGINT(20) NOT NULL,
	`demand_note` BIGINT(20) NOT NULL,
	`source_client` BIGINT(20) NOT NULL,
	`destination_client` BIGINT(20) NOT NULL,
	`submission_date` BIGINT(20) NULL DEFAULT NULL,
	`suggested_date` BIGINT(20) NULL DEFAULT NULL,
	`state` INT(11) NULL DEFAULT '0',
	`status` INT(11) NULL DEFAULT '0',
	`skip_payment` BIGINT(20) NULL DEFAULT '0',
	`comment` VARCHAR(500) NULL DEFAULT NULL,
	`description` VARCHAR(500) NULL DEFAULT NULL,
	`is_forwarded` TINYINT(4) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`) USING BTREE
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('lli_owner_change_application', 1, 0);


CREATE TABLE IF NOT EXISTS `lli_on_process_connection` (
	`id` BIGINT(20) NOT NULL,
	`application` BIGINT(20) NOT NULL,
	`connection` BIGINT(20) NOT NULL,
	PRIMARY KEY (`id`) USING BTREE
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('lli_on_process_connection', 1, 0);

-- 2/5/19
ALTER TABLE `lli_owner_change_application` ADD COLUMN `type` int(11) NULL DEFAULT '0';


-- 2/6/19


CREATE TABLE IF NOT EXISTS `lli_owner_change_dn` (
	`id` BIGINT(20) NOT NULL,
	`transfer_charge` BIGINT(20) NOT NULL,
	`parent_bill_id` BIGINT(20) NULL DEFAULT '0',

	PRIMARY KEY (`id`) USING BTREE
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('lli_owner_change_dn', 1, 0);

-- 2/11/19

ALTER TABLE at_lli_fixed_cost_config ADD ownership_change_charge DOUBLE DEFAULT 0 NULL;

-- 2/12/19
ALTER TABLE lli_owner_change_application ADD zone BIGINT DEFAULT 0 NULL;


-- 2/6/19
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (515, 'RESPONSE_NEGETIVE', 50, 'Response Negetive', '#008000', 'Responded Negetive');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (516, 'FORWARD_TO_SOURCE', 50, 'Forward the Negetive Response Back to Seller', '#008000', 'Forwarded the Negetive Response Back to Client');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (517, 'RECHECK', 50, 'Recheck Application', '#008000', 'Rechecked Application');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (518, 'RECHECK_RESPONSE', 50, 'Recheck Response Back', '#008000', 'Rresponded to Recheck Request');

-- 2/6/19
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (524, 515, 516, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (525, 515, 516, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (526, 516, 517, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (527, 517, 508, 'NULL');

-- 2/7/19
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (528, 508, 515, 'NULL');




-- 2/6/19

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (524, 524, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (525, 525, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (526, 526, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (527, 527, 22021);

-- 2/7/19

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (528, 528, -1);
-- 2/24/19

alter table nix_efr add loop_id bigint default 0 null;

-- 2/25/19
alter table nix_application_local_loop   add history_id BIGINT default 0 null;

alter table at_lli_application_localloop	add OCID bigint default 0 null;

alter table at_lli_application_localloop 	add is_removable int default 0 null;

alter table at_lli_application_localloop 	add old_loop_id BIGINT default 0 null;

alter table at_lli_local_loop add is_deleted boolean default false null;


--  ASN FOR LLI Module


-- 3/14/19

INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (8000, 'APPLICATION_SUBMIT',7001, 'Submit Application', '#008000', 'Application Submited');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (8001, 'REQUEST_REJECT',7001, 'Reject Request', '#008000', 'Request Rejected');
INSERT INTO `flow_state` (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (8002, 'REQUEST_ACCEPT', 7001, 'Approve Request', '#008000', 'Request Approved');

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (8000, 8000, 8001, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (8001, 8000, 8002, 'NULL');


INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (8000, 8000, 23020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (8001, 8001, 23020);

-- ASN entity table



CREATE TABLE `asn` (
										 `id` BIGINT(20) NOT NULL,
										 `application_id` BIGINT(20) NOT NULL,
										 `asn_no` INT(11) NOT NULL,
										 `client` BIGINT(20) NOT NULL,
										 `created_date` BIGINT(20) NOT NULL,
										 `status` INT(11) NULL DEFAULT NULL,
										 `state` INT(11) NULL DEFAULT NULL,
										 PRIMARY KEY (`id`) USING BTREE
)
	COLLATE='latin1_swedish_ci'
	ENGINE=InnoDB
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('asn', 1, 0);


create table `asn_application`(
																`asn_application_id` BIGINT(20) NOT NULL,
																`parent_application_id`  BIGINT(20) not null,
																`asn_no` INT(11) NOT NULL,
																PRIMARY KEY (`asn_application_id`) USING BTREE
)COLLATE='latin1_swedish_ci'
 ENGINE=InnoDB
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('asn_application', 1, 0);

create table `asn_to_ip`(
													`id` BIGINT(20) NOT NULL,
													`asn_id`  BIGINT(20) NOT NULL,
													`ip` VARCHAR(40) NOT NULL,
													PRIMARY KEY (`id`) USING BTREE
)COLLATE='latin1_swedish_ci'
 ENGINE=InnoDB
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('asn_to_ip', 1, 0);

-- 3/26/19
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (7300, 7001, 'ASN', 7);
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (7301, 7300, 'ASN Add', 7);
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (7302, 7300, 'ASN Search App', 7);
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (7303, 7300, 'ASN Search', 7);


alter table asn_to_ip add ip_version int default 1 null;

alter table asn_to_ip add last_modification_time BIGINT default 0 null;

alter table asn add created_by BIGINT default 0 null;

alter table asn add last_modification_time bigint default 0 null;

alter table asn_to_ip add is_deleted int default 0 null;

-- 4/1/19
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (9200, 9001, 'Bill', 9);
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (9201, 9200, 'Bill Search', 9);
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (9202, 9200, 'Payment Search', 9);
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (9203, 9200, 'Bank Add', 9);
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (9204, 9200, 'Bank Search', 9);

-- 4/3/19
alter table asn_to_ip add application_id BIGINT default 0 null after last_modification_time;

-- 04/28/19
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9091, 9001, 'Report', 9);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9092, 9091, 'NIX Report', 9);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9093, 9091, 'Client Report', 9);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9094, 9091, 'Bill Report', 9);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9095, 9091, 'Payment Report', 9);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9096, 9091, 'Connection Report', 9);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (9097, 9091, 'Application Report', 9);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (7096, 7091, 'Application Report', 7);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (7097, 7091, 'Connection Report', 7);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (6091, 6001, 'Report', 6);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (6092, 6091, 'Payment Report', 6);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (6093, 6091, 'VPN Report', 6);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (6094, 6091, 'Client Report', 6);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (6095, 6091, 'Bill Report', 6);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (6096, 6091, 'Application Report', 6);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (6097, 6091, 'Connection Report', 6);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (50091, 50001, 'Report', 11);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (50092, 50091, 'Client Report', 11);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (50093, 50091, 'Contract Report', 11);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (50094, 50091, 'Application Report', 11);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (100021, 100011, 'Application Report', 100);
 INSERT INTO btcl_refactor_new.admenu (mnMenuID, mnParentMenuID, mnMenuName, mnModuleTypeID) VALUES (100022, 100011, 'Connection Report', 100);


-- 04/30/19
INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_connection_revise_client', 1, 0);


INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('nix_client_td', 100000, 0);


-- 3/7/19
alter table nix_application	add seconde_zone int null;
alter table nix_ifr	add is_Ignored int null;
alter table nix_application change rejection_comment comment varchar(255) null;

-- 7/7/19
alter table at_lli_additional_port 	add officeType int null;
alter table at_lli_additional_port 	add loopType int null;
-- 8/7/19
alter table at_lli_application_office modify office_address varchar(250) null;
alter table at_lli_application_office modify office_name varchar(250) null;

-- 7/919


INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5250, 'NIX_CLOSE_APPLICATION_SUBMIT', 501, 'Nix close application submitted', '#008000', 'Close Application Submited', 'SUBMIT');
INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5251, 'NIX_CLOSE_APPLICATION_REJECT', 501, 'Reject Application', '#008000', 'Application Rejected', 'REJECT');
INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5252, 'NIX_CLOSE_APPLICATION_REOPEN', 501, 'Reopen application', '#008000', 'Application Reopened', 'REOPEN');
INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5253, 'NIX_CLOSE_APPLICATION_CORRECTION', 501, 'Request for client correction', '#008000', 'Client Correction Requested', 'CORRECTION');
INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5254, 'NIX_CLOSE_APPLICATION_CORRECTION_DONE', 501, 'Submit correction', '#008000', 'Client Correction Submitted', 'CORRECTION');
INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5255, 'NIX_CLOSE_APPLICATION_DEMAND_NOTE_GENERATED', 501, 'Generate demand note', '#008000', 'Demand Note Generated', 'DEMAND_NOTE');
INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5256, 'NIX_CLOSE_APPLICATION_PAYMENT_DONE', 501, 'Give payment', '#008000', 'Payment Verification Needed', 'PAYMENT_GIVEN');
INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5257, 'NIX_CLOSE_APPLICATION_PAYMENT_VERIFIED', 501, 'Verify payment', '#008000', 'Payment Verified', 'PAYMENT_VERIFIED');


INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5258, 'NIX_CLOSE_APPLICATION_PUBLISH_ADVICE_NOTE', 501, 'Publish advice note', '#008000', 'Advice Note Published', 'ADVICE_NOTE');
INSERT INTO btcl_refactor_new_DEV.flow_state (id, name, flow, description, color, view_description, phase) VALUES (5259, 'SERVER_ROOM_TESTING_AND_COMPLETE', 501, 'Test and complete', '#008000', 'Server Room Testing Completed ', 'TESTING');



INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5250, 5250, 5251, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5251, 5251, 5252, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5252, 5250, 5255, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5253, 5250, 5253, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5254, 5252, 5255, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5255, 5253, 5254, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5256, 5254, 5255, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5257, 5255, 5256, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5258, 5256, 5257, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5259, 5257, 5258, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5260, 5258, 5259, 'NULL');

-- flow state transition role

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5250, 5250, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5251, 5251, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5252, 5252, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5253, 5253, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5254, 5254, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5255, 5255, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5256, 5256, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5257, 5257, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5258, 5258, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5259, 5259, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5260, 5260, 16021);

-- 7/11/19
INSERT INTO `btcl_refactor_new_DEV`.`flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5056, 5024, 5002, 'WITHOUT LOOP Reject');
INSERT INTO `btcl_refactor_new_DEV`.`flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (5057, 5007, 5002, null);

INSERT INTO `btcl_refactor_new_DEV`.`flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5056, 5056, 22021);
INSERT INTO `btcl_refactor_new_DEV`.`flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (5057, 5057, 22021);
