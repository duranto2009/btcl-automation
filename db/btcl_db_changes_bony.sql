ALTER TABLE at_lli_application ADD skipPayment int DEFAULT 0 NULL;
CREATE TABLE `geo_area` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zone_id` int(11) NOT NULL,
  `name` varchar(2045) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `geo_district` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `division` int(8) NOT NULL,
  `name_eng` varchar(255) NOT NULL,
  `name_bng` varchar(255) NOT NULL,
  `bbs_code` char(4) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created` int(11) DEFAULT NULL,
  `modified` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `division` (`division`),
  CONSTRAINT `geo_district_ibfk_1` FOREIGN KEY (`division`) REFERENCES `geo_division` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `geo_division` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `name_eng` varchar(255) NOT NULL,
  `name_bng` varchar(255) NOT NULL,
  `bbs_code` char(4) DEFAULT '',
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `created` int(11) DEFAULT NULL,
  `modified` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `geo_zone` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `geo_zone_district_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zone_id` int(11) NOT NULL,
  `district_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `geo_zone_user_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zone_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table at_lli_application_ifr
(
  id int auto_increment
    primary key,
  parentIFRID int null,
  applicationID int null,
  popID int null,
  requestedBW int null,
  availableBW int null,
  selectedBW int null,
  priority int null,
  isReplied int null,
  serverRoomLocationID int null,
  submissionDate bigint null,
  state int null,
  isSelected int default '0' null,
  officeID int null,
  isForwarded int null
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `at_lli_application_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) DEFAULT NULL,
  `stateID` int(11) DEFAULT NULL,
  `sequenceID` int(11) DEFAULT '1',
  `comments` varchar(1055) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists at_lli_application_efr
(
  id int auto_increment
    primary key,
  applicationID int null,
  officeID int null,
  state int null,
  parentEFRID int null,
  bandwidth int null,
  sourceType int null,
  source varchar(255) null,
  destinationType int null,
  destination varchar(255) null,
  quotationStatus int null,
  quotationDeadline bigint null,
  proposedLoopDistance int null,
  vendorID int null,
  vendorType int null,
  workGiven int null,
  workCompleted int null,
  workDeadline bigint null,
  popID int null,
  ofcType int null,
  isForwarded int null
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table at_lli_application_localloop
(
  id int auto_increment
    primary key,
  applicationID int null,
  officeID int null,
  popID int null,
  bandwidth int null,
  router_switchID int null,
  clientDistances int null,
  BTCLDistances int null,
  OCDistances int null,
  OFCType int null,
  portID int null,
  VLANID int null
);

create table at_lli_application_office
(
  id int auto_increment
    primary key,
  application_id int null,
  connectionID int null,
  history_id int null,
  office_name varchar(45) null,
  office_address varchar(45) null
);



ALTER TABLE aduser
  ADD COLUMN `usZoneID` INT NULL DEFAULT 0 ;



ALTER TABLE at_lli_application
  ADD COLUMN `zone_id` INT NULL;

ALTER TABLE at_lli_application
  ADD COLUMN `state` INT NULL;

alter table at_lli_application
  add connection_id int null
;

alter table at_lli_application
  add prevPaymnetCheck int default '0' null
;

alter table at_lli_application
  add old_zone_id int null
;

alter table at_lli_application
  add is_forwarded int null
;

alter table at_lli_application
  add connectionType int null
;

alter table at_lli_application
  add loopProvider int null
;

alter table at_lli_application
  add duration bigint null
;

alter table at_lli_application
  add suggestedDate bigint null
;

alter table at_lli_application
  add bandwidth int null
;


alter table at_lli_office
  add application_id int null;

ALTER TABLE at_lli_local_loop
  ADD applicationID bigint(20) NULL;

ALTER TABLE at_lli_local_loop
  ADD popID bigint(20) NULL;

ALTER TABLE at_lli_local_loop
  ADD bandwidth int NULL;

ALTER TABLE at_lli_local_loop
  ADD router_switchID bigint(20) NULL;

ALTER TABLE at_lli_local_loop
  ADD portID bigint(20) NULL;

ALTER TABLE at_lli_client_td
  ADD isActive int DEFAULT 0 NULL;

ALTER TABLE at_lli_client_td
  ADD comment varchar(255) NULL;

ALTER TABLE at_lli_client_td
  ADD advicedDate BIGINT NULL;

ALTER TABLE at_lli_client_td
  ADD appliedDate BIGINT NULL;



create table at_lli_connection_revise_client_comments
(
  id int auto_increment primary key,
  userID int null,
  userType int null,
  stateID int null,
  applicationID int null,
  sequenceID int default '1' null,
  comments varchar(1055) null,
  submissionDate bigint null
)
;


create table at_lli_connection_revise_client
(
  id bigint not null primary key,
  clientID bigint not null,
  applicationType int not null,
  state int null,
  isDemandNoteNeeded tinyint(255) not null,

  description varchar(255) null,
  suggestedDate bigint null,
  appliedDate bigint null
)
  charset=utf8
;

ALTER TABLE at_lli_connection_revise_client ADD demandNoteID bigint NULL;


INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('geo_area', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('geo_district', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('geo_division', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('geo_zone', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('geo_zone_district_mapping', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('geo_zone_user_mapping', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('at_lli_application_comments', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('at_lli_application_efr', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('at_lli_application_localloop', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('at_lli_application_office', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('at_lli_connection_revise_client_comments', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('at_lli_connection_revise_client', 1, 0);


alter table at_lli_application_ifr
  add isIgnored int default '0' null
;

alter table at_lli_application
  add billID bigint null
;

-- change on 8-11-18
alter table at_lli_application_efr
  add actualLoopDistance int default '0' null
;


alter table at_lli_local_loop
  add adjustedBTClDistance int  default '0' null
;

alter table at_lli_local_loop
  add adjustedOCDistance int  default '0' null
;

alter table at_lli_application_efr
  add loopDistanceIsApproved int default '0' null
;


ALTER TABLE at_lli_connection ADD zoneID  int DEFAULT 0 NULL ;

-- change on 15 nov

alter table at_lli_application
	add source_connection_id bigint null
;


-- 22-nov-2018
-- shift-bw states

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (2001, 'SHIFT_BW_SUBMIT', 104, 'Submitted', '#f99a2f', 'Shift Bandwidth Submitted');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
  VALUES (2002, 'REJECT', 104, 'Reject', '#f99a2f', 'Shift Bandwidth Rejected By Local DGM');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2003, 'APPROVE_BY_SOURCE', 104, 'Approve Bandwidth Shifting', '#008000', 'Shift Bandwidth Approved By Local DGM');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2004, 'APPROVE_BY_CENTRAL', 104, 'Approve Bandwidth Shifting', '#008000', 'Shift Bandwidth Requested');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (2005, 'REJECTED_BY_CENTRAL', 104, 'Reject', '#ff0000', 'Shift Bandwidth Rejected');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
  VALUES (2006, 'REOPEN_BY_LOCAL', 104, 'Reopen Application', '#008000', 'Application Reopened by Local DGM');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (2007, 'REOPEN_BY_CENTRAL', 104, 'Reopen Application', '#008000', 'Application Reopened');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (2008, 'REQUEST_IFR', 104, 'Request for Internal FR', '#f99a2f', 'Internal FR Requested');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (2009, 'RESPONSE_IFR', 104, 'Response Internal FR', '#f99a2f', 'Internal FR Responded');
INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (2010, 'GENERATE_AN', 104, 'Generate Advice Note ', '#f99a2f', 'Advice Note Generated');
INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (2011, 'TRANSFER_AN', 104, 'Transfer Advice Note', '#f99a2f', 'Advice Note Transfered');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2012, 'COMPLETE_PROCESS', 104, 'Complete Process', '#008000', 'Process Completed');


-- with new connection shift bandwidth

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2101, 'SHIFT_BW_NEW_SUBMIT', 104, 'Submitted', '#f99a2f', 'Shift Bandwidth Submitted');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2102, 'NEW_REJECT', 104, 'Reject', '#f99a2f', 'Shift Bandwidth Rejected By Local DGM');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2103, 'APPROVE_BY_SOURCE_NEW', 104, 'Approve Bandwidth Shifting', '#008000', 'Shift Bandwidth Approved By Local DGM');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2104, 'APPROVE_BY_CENTRAL_NEW', 104, 'Approve Bandwidth Shifting', '#008000', 'Shift Bandwidth Requested');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2105, 'REJECTED_BY_CENTRAL_NEW', 104, 'Reject', '#ff0000', 'Shift Bandwidth Rejected');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2106, 'REOPEN_BY_LOCAL_NEW', 104, 'Reopen Application', '#008000', 'Application Reopened by Local DGM');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2107, 'REOPEN_BY_CENTRAL_NEW', 104, 'Reopen Application', '#008000', 'Application Reopened');


-- flow_state_transition
-- shift bw regular
 INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
 VALUES (2001, 2001, 2002, 'NULL');
 INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2001, 2003, 'NULL');
 INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2002, 2006, 'NULL');
 INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2003, 2004, 'NULL');
 INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2003, 2005, 'NULL');
 INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2005, 2007, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2006, 2003, 'NULL');
 INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2007, 2004, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2004, 2008, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
 VALUES (2008, 2009, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
 VALUES (2009, 2010, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2010, 2011, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2011, 2012, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2009, 2013, 'NULL');

-- flow_state_transition_role
-- shift bw regular
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (2001, 2001, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2002, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2003, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2007, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2004, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2005, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2006, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2008, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2009, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2010, 16021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2011, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2014, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2012, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2013, 16021);


ALTER TABLE at_lli_application CHANGE old_zone_id second_zone_id int(11);


-- 25 nov 2018
-- shift bw with new connection
-- transition table

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (2101, 2101, 2102, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2101, 2103, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2102, 2106, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2103, 2104, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2103, 2105, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2105, 2107, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2106, 2103, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2107, 2104, 'NULL');

INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2104, 30, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2104, 34, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2104, 7, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2104, 35, 'NULL');

-- transition role table
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (2101, 2101, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2102, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2103, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2107, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2104, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2105, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2106, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2108, 22021);

 INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
 VALUES (2109, 2109, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2110, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2111, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2112, 22020);


-- new shift with client loop
-- flow states

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2201, 'WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT', 104, 'Submitted', '#f99a2f', 'Shift Bandwidth Submitted');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2202, 'WITHOUT_LOOP_NEW_REJECT', 104, 'Reject', '#f99a2f', 'Shift Bandwidth Rejected By Local DGM');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2203, 'WITHOUT_LOOP_APPROVE_BY_SOURCE_NEW', 104, 'Approve Bandwidth Shifting', '#008000', 'Shift Bandwidth Approved By Local DGM');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2204, 'WITHOUT_LOOP_APPROVE_BY_CENTRAL_NEW', 104, 'Approve Bandwidth Shifting', '#008000', 'Shift Bandwidth Requested');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2205, 'WITHOUT_LOOP_REJECTED_BY_CENTRAL_NEW', 104, 'Reject', '#ff0000', 'Shift Bandwidth Rejected');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2206, 'WITHOUT_LOOP_REOPEN_BY_LOCAL_NEW', 104, 'Reopen Application', '#008000', 'Application Reopened by Local DGM');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (2207, 'WITHOUT_LOOP_REOPEN_BY_CENTRAL_NEW', 104, 'Reopen Application', '#008000', 'Application Reopened');

INSERT INTO flow_state (`id`,`name`, `flow`, `description`, `color`, `view_description`)
VALUES (2208,'WITHOUT_LOOP_DEMAND_NOTE_BYPASS', 104, 'Internal FR Responded', '#008000', 'Internal FR Responded');
-- flow-state-transition

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (2201, 2201, 2202, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2201, 2203, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2202, 2206, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2203, 2204, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2203, 2205, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2205, 2207, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2206, 2203, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2207, 2204, 'NULL');

INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2204, 55, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2204, 56, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2204, 57, 'NULL');
INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2204, 58, 'NULL');

INSERT INTO flow_state_transition (`source`, `destination`, `comment`)
VALUES (2208, 73, '');

-- flow-state-transition-role

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (2201, 2201, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2202, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2203, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2207, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2204, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2205, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2206, 22021);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2208, 22021);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (2209, 2209, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2210, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2211, 22020);
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2212, 22020);

INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (2213, 22020);


-- 26-11-18

ALTER TABLE at_lli_connection_revise_client ADD isCompleted int DEFAULT 0 NOT NULL;
UPDATE flow_state t SET t.`view_description` = 'Payment Skipped' WHERE t.`id` = 92;


-- 29 nov
INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`) VALUES (1017, 'BYPASS_WO', 101, 'Demand Note Paid', '#008000', 'Payment Verified');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (1028, 1017, 1014, 'NULL');
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (1225, 1028, 22020);

-- 2 dec 2018

-- flow-state
INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (1101, 'CONNECTION_CLOSE_POLICY_SUBMIT', 104, 'Submitted', '#f99a2f', 'Connection Close Submitted');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (1102, 'BYPASS_WO', 101, 'Bypass Work Order', '#f99a2f', 'Advice Note Pending');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (1103, 'POLICY_CLOSE_REJECT', 101, 'Reject Application', '#ff0000', 'Application Rejected');
 INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (1104, 'POLICY_CLOSE_REOPEN_APPLICATION', 101, 'Reopen Application', '#008000', 'Application Reopened');

 INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (1105, 'POLICY_CLOSE_REJECT', 101, 'Reject Application', '#ff0000', 'Application Rejected');
INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (1106, 'POLICY_CLOSE_REOPEN_APPLICATION', 101, 'Reopen Application', '#008000', 'Application Reopened');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (1201, 'CONNECTION_DOWNGRADE_POLICY_SUBMIT', 3, 'Submitted', '#f99a2f', 'Downgrade Bandwidth Submitted');

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (1202, 'CONNECTION_DOWNGRADE_POLICY_REJECT', 3, 'Reject', '#ff0000', 'Connection Downgrade Rejected');
INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (1203, 'CONNECTION_DOWNGRADE_POLICY_REOPEN', 3, 'Reopen Application', '#008000', 'Connection Downgrade Application Reopened');
-- flow-state-transition

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1101, 1101, 1012, 'NULL');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1102, 1102, 1014, 'NULL');

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1103, 1101, 1103, 'NULL');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1107, 1104, 1012, 'NULL');

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1105, 1102, 1105, 'NULL');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1108, 1106, 1014, 'NULL');

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1201, 1201, 1202, '');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1202, 1201, 73, 'NULL');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1203, 1202, 1203, 'NULL');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (1204, 1203, 73, 'NULL');


-- flow-state-transition-role

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1125, 1101, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1126, 1101, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1127, 1102, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1128, 1102, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1129, 1103, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1130, 1103, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1131, 1104, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1132, 1104, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1133, 1105, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1134, 1105, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1140, 1106, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1141, 1106, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1145, 1107, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1146, 1107, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1147, 1108, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1148, 1108, 22021);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1201, 1201, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1202, 1201, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1203, 1202, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1204, 1202, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1205, 1203, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1206, 1203, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1207, 1204, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (1208, 1204, 22021);

-- 03-12-218

-- flow-state

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (3001, 'FORWARD_LDGM_FOR_LOOP', 1, 'Forward To LDGM to Complete Local Loop', '#f99a2f', 'Application Forwarded For Local Loop Completion');

 INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (3002, 'FORWARD_LDGM_EFR_REQUEST_FOR_LOOP', 1, 'Request For External FR', '#f99a2f', 'External FR requested');

 INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (3003, 'FORWARD_LDGM_RESPONSE_EXTERNAL_FR', 1, 'Response External FR', '#008000', 'External FR Responded');

 INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
 VALUES (3004, 'FORWARD_LDGM_SELECT_EXTERNAL_FR', 1, 'Forward Back to CDGM', '#f99a2f', 'Application move back from Local DGM');

 -- flow-state-transition

 INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
 VALUES (3002, 3001, 3002, 'NULL');
  INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
 VALUES (3003, 3002, 3003, 'NULL');
  INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
 VALUES (3004, 3003, 3004, 'NULL');
  INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
 VALUES (3005, 3004, 33, 'NULL');
  INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
 VALUES (3006, 3004, 91, 'NULL');

 -- flow-state-transition-role

 INSERT INTO flow_state_transition_role (`id`,`flow_state_transition`, `role`)
 VALUES (3001,3001, 22021);
  INSERT INTO flow_state_transition_role (`id`,`flow_state_transition`, `role`)
 VALUES (3002,3002, 22020);
  INSERT INTO flow_state_transition_role (`id`,`flow_state_transition`, `role`)
 VALUES (3003,3003, 16020);
  INSERT INTO flow_state_transition_role (`id`,`flow_state_transition`, `role`)
 VALUES (3004,3004, 22020);
  INSERT INTO flow_state_transition_role (`id`,`flow_state_transition`, `role`)
 VALUES (3005,3005, 22021);
  INSERT INTO flow_state_transition_role (`id`,`flow_state_transition`, `role`)
 VALUES (3006,3006, 22021);

INSERT INTO flow_state_transition_role (`id`,`flow_state_transition`, `role`)
 VALUES (3006,3006, 22021);


--

-- 1-1-2019
INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (19, 'FORWARD_FOR_WORK_ORDER', 1, 'Forward LDGM to Work Order', '#f99a2f', 'Forwarded from CDGM for work order');
INSERT INTO flow_state_transition  (`id`, `source`, `destination`, `comment`)
VALUES (19, 25, 19, 'NULL');
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (17, 19, 22021);

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (17, 'GIVE_WORK_ORDER', 1, 'Give Work Order', '#f99a2f', 'Work Order Given');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (20, 19, 17, 'NULL');
 INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
 VALUES (18, 20, 22020);


INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (18, 'JOB_DONE', 1, 'Complete Work Order', '#f99a2f', 'Work Order Completed');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (23, 17, 18, 'NULL');

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (21, 23, 16020);

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`)
VALUES (15, 'APPROVE_AND_FORWRAD', 1, 'Approve and forward to CDGM', '#f99a2f', 'Job Completed and Forwarded');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (24, 18, 15, 'NULL');
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (22, 24, 22020);

 INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
 VALUES (26, 15, 29, 'NULL');

 INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
 VALUES (24, 26, 22021);


-- 6-jan-19

DELETE FROM flow_state_transition_role WHERE id = 1087;

UPDATE flow_state t
SET t.`description` = 'Send Advice To server Room', t.`view_description` = 'Advice Note Received'
WHERE t.`id` = 74;

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (95, 72, 74, 'NULL');

UPDATE flow_state_transition_role t SET t.`flow_state_transition` = 95 WHERE t.`id` = 1071;


INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (160, 91, 19, 'NULL');
INSERT INTO flow_state_transition_role (`flow_state_transition`, `role`)
VALUES (160, 22021);

UPDATE flow_state_transition t SET t.`destination` = 73 WHERE t.`id` = 95;


-- 22-5-2019

alter table geo_zone
    add is_central bit default 0 null;


-- 12-6-19


INSERT INTO flow_state (id, name, flow, description, color, view_description, phase)
VALUES (2013, 'SHIFT_BW_SUBMIT_NO_APPROVAL', 104, 'Shift Bandwidth Submitted',
        '#f99a2f', 'Shift Bandwidth Submitted', null);

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (2015, 2013, 2008, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (2015, 2015, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (2019, 2015, 22020);


INSERT INTO flow_state
(`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`)
VALUES (2014, 'SHIFT_BW_SUBMIT_ONLY_CDGM_APPROVAL', 104, 'Shift Bandwidth Submitted', '#f99a2f', 'Shift Bandwidth Submitted', null);

INSERT INTO flow_state_transition
(`id`, `source`, `destination`, `comment`) VALUES (2016, 2014, 2004, null);
INSERT INTO flow_state_transition
(`id`, `source`, `destination`, `comment`) VALUES (2017, 2014, 2005, null);


INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2016, 2016, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2017, 2017, 22021);

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`) VALUES (2015, 'SHIFT_BW_SUBMITTED_ONLY_LDGM_APPROVAL', 104, 'Shift Bandwidth Approve', '#008000', 'Shift Bandwidth Approved By Source', null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2018, 2015, 2008, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2018, 2018, 22021);

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`) VALUES (2108, 'SHIFT_BW_NEW_SUBMIT_NO_APPROVAL', 104, 'Submitted', '#f99a2f', 'Shift Bandwidth Submitted', 'SUBMIT');

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2113, 2108, 30, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2114, 2108, 34, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2115, 2108, 7, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2116, 2108, 35, null);

  INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2113, 2113, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2114, 2114, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2115, 2115, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2116, 2116, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2117, 2113, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2118, 2114, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2119, 2115, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2120, 2116, 22020);

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`) VALUES (2109, 'SHIFT_BW_NEW_SUBMIT_ONLY_CDGM_APPROVAL', 104, 'Submitted', '#f99a2f', 'Shift Bandwidth Submitted', 'SUBMIT');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2117, 2109, 2104, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2118, 2109, 2105, null);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2121, 2117, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2122, 2118, 22021);

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`) VALUES (2110, 'SHIFT_BW_NEW_SUBMIT_ONLY_LDGM_APPROVAL', 104, 'Shift Bandwidth Approved By Source', '#f99a2f', 'Shift Bandwidth Approved By Source', 'APPROVAL');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2119, 2110, 30, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2120, 2110, 34, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2121, 2110, 7, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2122, 2110, 35, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2123, 2119, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2124, 2120, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2125, 2121, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2126, 2122, 22021);


  INSERT INTO flow_state
  (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`)
  VALUES (2209, 'WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT_NO_APPROVAL', 104, 'Submitted', '#f99a2f', 'Shift Bandwidth Submitted', 'SUBMIT');
  INSERT INTO flow_state
  (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`)
  VALUES (2210, 'WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT_ONLY_CDGM_APPROVAL', 104, 'Submitted', '#f99a2f', 'Shift Bandwidth Submitted', 'SUBMIT');
  INSERT INTO flow_state
  (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`)
  VALUES (2211, 'WITHOUT_LOOP_SHIFT_BW_NEW_SUBMIT_ONLY_LDGM_APPROVAL', 104, 'Shift Bandwidth Approved By Source', '#f99a2f', 'Shift Bandwidth Approved By Source', 'APPROVAL');

  INSERT INTO flow_state_transition
  (`id`, `source`, `destination`, `comment`) VALUES (2214, 2209, 55, null);
  INSERT INTO flow_state_transition
  (`id`, `source`, `destination`, `comment`) VALUES (2215, 2209, 56, null);
  INSERT INTO flow_state_transition ( `id`, `source`, `destination`, `comment`) VALUES (2216, 2209, 57, null);
INSERT INTO flow_state_transition
(`id`, `source`, `destination`, `comment`) VALUES (2217, 2209, 58, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2214, 2214, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2215, 2215, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2216, 2216, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2217, 2217, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2218, 2214, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2219, 2215, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2220, 2216, 22020);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2221, 2217, 22020)
;
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2218, 2210, 2204, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2219, 2210, 2205, null);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2222, 2218, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2223, 2019, 22021);


INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2220, 2211, 55, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2221, 2211, 56, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2222, 2211, 57, null);
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2223, 2211, 58, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2224, 2220, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2225, 2221, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2226, 2222, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2227, 2223, 22021);









INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (3007, 3003, 3002, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (3009, 3007, 22020);

-- 3-7-2019

INSERT INTO flow_state (id, name, flow, description, color, view_description, phase)
VALUES (36, 'AN_GENERATE_AND_FORWARD', 1, 'Generate Advice Note and Forward To Server Room', '#f99a2f', 'Advice Note Forwarded', 'ADVICE_NOTE');


UPDATE flow_state_transition t SET t.`destination` = 36 WHERE t.`id` = 26;
DELETE FROM flow_state_transition_role WHERE `id` = 1101;

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (10, 36, 41, null);


INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (10, 10, 16021);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (25, 26, 16021);


INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (6, 27, 36, null);

UPDATE flow_state_transition_role t SET t.`flow_state_transition` = 6 WHERE t.`id` = 1037;

-- AN Forward new without loop

INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`)
VALUES (93, 'LLI_WITHOUT_LOOP_ADVICE_NOTE_PUBLISH_AND_FORWARD', 1, 'Publish Advice Note and forward', '#f99a2f', 'Advice Note Published and forwarded', 'ADVICE_NOTE');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (33, 92, 93, null);
UPDATE flow_state_transition t SET t.`destination` = 93 WHERE t.`id` = 95;
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (39, 93, 80, null);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (37, 39, 16021);



INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`)
VALUES (1018, 'GENERATE_AN_AND_FORWARD', 101, 'Advice note Generate and forward', '#f99a2f', 'Advice Note Generated and forwarded', 'ADVICE_NOTE');

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (1050, 1013, 1018, '');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (1051, 1017, 1018, 'NULL');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (1052, 1102, 1018, 'NULL');
INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (1053, 1106, 1018, 'NULL');


UPDATE flow_state_transition_role t SET t.`flow_state_transition` = 1050 WHERE t.`id` = 1123;
UPDATE flow_state_transition_role t SET t.`flow_state_transition` = 1051 WHERE t.`id` = 2311;
UPDATE flow_state_transition_role t SET t.`flow_state_transition` = 1053 WHERE t.`id` = 1148;
UPDATE flow_state_transition_role t SET t.`flow_state_transition` = 1052 WHERE t.`id` = 1128;


INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
 VALUES (1054, 1018, 1016, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (1154, 1054, 16021);


INSERT INTO flow_state (`id`, `name`, `flow`, `description`, `color`, `view_description`, `phase`)
VALUES (2016, 'GENERATE_AN_AND_FW', 104, 'Generate Advice Note and Forward', '#f99a2f', 'Advice Note Generated and Forwarded', 'ADVICE_NOTE');

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2021, 2009, 2016, null);

UPDATE flow_state_transition_role t SET t.`flow_state_transition` = 2021 WHERE t.`id` = 2317;


INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (2022, 2016, 2012, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (2022, 2022, 16021);

DELETE FROM flow_state_transition WHERE `id` = 2014;



-- 8-7-19
UPDATE  flow_state t SET t.`view_description` = 'Application Reopened' WHERE t.`id` = 2006;
UPDATE  flow_state t SET t.`view_description` = 'Shift Bandwidth Approved' WHERE t.`id` = 2203;
UPDATE  flow_state t SET t.`view_description` = 'Shift Bandwidth Approved' WHERE t.`id` = 2004;
UPDATE  flow_state t SET t.`view_description` = 'Shift Bandwidth Approved' WHERE t.`id` = 2003;
UPDATE  flow_state t SET t.`view_description` = 'Application Reopened' WHERE t.`id` = 2206;
UPDATE  flow_state t SET t.`view_description` = 'Shift Bandwidth Rejected' WHERE t.`id` = 2202;
UPDATE  flow_state t SET t.`view_description` = 'Shift Bandwidth Approved' WHERE t.`id` = 2015;
UPDATE  flow_state t SET t.`view_description` = 'Shift Bandwidth Approved' WHERE t.`id` = 2104;
UPDATE  flow_state t SET t.`view_description` = 'Shift Bandwidth Rejected' WHERE t.`id` = 2002;
UPDATE  flow_state t SET t.`view_description` = 'Shift Bandwidth Rejected' WHERE t.`id` = 2102;
UPDATE  flow_state t SET t.`view_description` = 'Shift Bandwidth Approved' WHERE t.`id` = 2103;
UPDATE  flow_state t SET t.`view_description` = 'Application Reopened by' WHERE t.`id` = 2106;

-- 9-7-19

alter table at_lli_connection_revise_client
  add advice_note_id bigint default 0 null;


-- 10-7-19

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (62, 59, 57, null);


INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (62, 62, 22021);
INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (63, 62, 22020);