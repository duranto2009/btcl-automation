INSERT INTO `at_client_module` (`id`, `moduleId`, `name`, `isDeleted`, `lastModificationTime`)
VALUES ('5', '5', 'Upstream', '0', '0');



INSERT INTO `module` (`id`, `name`)
VALUES ('5', 'Upstream');
INSERT INTO `component` (`id`, `name`)
VALUES ('5', 'Connection Application');
UPDATE `component`
SET `module`='5'
WHERE `id` = 5;
INSERT INTO `flow` (`id`, `name`, `component`)
VALUES ('5000', 'New Connection Application Flow', '5');


-- flow state
INSERT INTO flow_state (id, name, flow, description, color, view_description)
VALUES (50002, 'UPSTREAM_FORWARDED_TO_GM_DATA_AND_INTERNET', 5000, 'Submit Application', '#f99a2f',
        'Application Submitted'); -- 50003, 50007, 50009

INSERT INTO flow_state (id, name, flow, description, color, view_description)
VALUES (50003, 'UPSTREAM_APPROVED_AND_FORWARDED_TO_CGM_OVERSEAS', 5000, 'Approve & Forward to CGM (Overseas)',
        '#f99a2f', 'Forwarded to CGM (Overseas)'); -- 50004

INSERT INTO flow_state (id, name, flow, description, color, view_description)
VALUES (50007, 'UPSTREAM_REQUESTED_DGM_CORE_UPSTREAM_FOR_CORRECTION', 5000,
        'Request to DGM (Core and Upstream) for correction', '#f99a2f',
        'Requested correction for submitted app'); -- 50008

INSERT INTO flow_state (id, name, flow, description, color, view_description)
VALUES (50008, 'UPSTREAM_PRECHECK_DONE', 5000, 'Submit Correction', '#008000',
        'DGM (Core and Upstream) Correction Submitted'); -- 50002

INSERT INTO flow_state (id, name, flow, description, color, view_description)
VALUES (50009, 'UPSTREAM_REJECTED', 5000, 'Reject Application', '#ff0000', 'Application Rejected'); -- 50010

INSERT INTO flow_state (id, name, flow, description, color, view_description)
VALUES (50010, 'UPSTREAM_APPLICATION_REOPENED', 5000, 'Reopen Application', '#f99a2f',
        'Application Reopened'); -- 50003, 50007, 50009

INSERT INTO flow_state (id, name, flow, description, color, view_description)
VALUES (50004, 'UPSTREAM_APPROVED_AND_FORWARDED_TO_GM_INTERNATIONAL', 5000, 'Approve & Forward to GM (International)',
        '#f99a2f', 'Forwarded to GM (International)'); -- 50005

INSERT INTO flow_state (id, name, flow, description, color, view_description)
VALUES (50005, 'UPSTREAM_APPROVED_VENDOR_AND_FORWARDED_TO_DGM_CORE_UPSTREAM', 5000,
        'Approve Vendor & Forward to DGM (Core & Upstream)', '#f99a2f',
        'Approved Vendor and forwarded to DGM (Core & Upstream)'); -- 50006

INSERT INTO flow_state (id, name, flow, description, color, view_description)
VALUES (50006, 'UPSTREAM_CONNECTION_COMPLETE', 5000, 'Complete Connection', '#008000', 'Application Process Completed');


-- flow state transition
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50000, 50002, 50003, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50001, 50002, 50007, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50002, 50002, 50009, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50003, 50003, 50004, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50004, 50007, 50008, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50005, 50008, 50002, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50006, 50009, 50010, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50007, 50010, 50003, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50008, 50010, 50007, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50009, 50010, 50009, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50010, 50004, 50005, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50011, 50005, 50006, null);


-- flow state transition role
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50000, 50000, 500001);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50001, 50001, 500001);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50002, 50002, 500001);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50003, 50003, 500002);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50004, 50004, 500001);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50005, 50005, 500000);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50006, 50006, 500001);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50007, 50007, 500001);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50008, 50008, 500001);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50009, 50009, 500001);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50010, 50010, 500003);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role)
VALUES (50011, 50011, 500000);


-- 31 March 2019
UPDATE flow_state_transition_role t
SET t.`role` = 500000
WHERE t.`id` = 50004;



INSERT INTO `adrole` (`rlRoleID`)
VALUES ('500000');
UPDATE `adrole`
SET `rlRoleName`='DGM (Core & Upstream)',
    `rlRoleDesc`='DGM (Core & Upstream)',
    `rlMaxClientPerDay`='0'
WHERE `rlRoleID` = 500000;
INSERT INTO `adrole` (`rlRoleID`, `rlRoleName`, `rlRoleDesc`, `rlMaxClientPerDay`)
VALUES ('500001', 'GM (Data and Internet)', 'GM (Data and Internet)', '0');
INSERT INTO `adrole` (`rlRoleID`, `rlRoleName`, `rlRoleDesc`, `rlMaxClientPerDay`)
VALUES ('500002', 'CGM (Overseas)', 'CGM (Overseas)', '0');
INSERT INTO `adrole` (`rlRoleID`, `rlRoleName`, `rlRoleDesc`, `rlMaxClientPerDay`)
VALUES ('500003', 'GM (International)', 'GM (International)', '0');



INSERT INTO `aduser` (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`, `usStatus`, `usFullName`,
                      `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`, `usAddedBy`,
                      `usAddTime`, `usLastModifiedBy`, `usLastModificationTime`, `usExchange`, `usProfilePicturePath`)
VALUES ('500000', 'dgm_upstream', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', '500000', '', '0', 'Maruful Haque Hamidi',
        'Deputy General Manager', 'Uttara-12', '', '', '', '4127', '1484454823461', '4127', '1495361672567', '0',
        '-127/-10127/5493c199-d4f9-4dc3-b89d-75b50b4d6d4b.jpg');

INSERT INTO `aduser` (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`, `usStatus`, `usFullName`,
                      `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`, `usAddedBy`,
                      `usAddTime`, `usLastModifiedBy`, `usLastModificationTime`, `usExchange`, `usProfilePicturePath`)
VALUES ('500001', 'gm_data', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', '500001', '', '0', 'Maruful Haque Hamidi',
        'Deputy General Manager', 'Uttara-12', '', '', '', '4127', '1484454823461', '4127', '1495361672567', '0',
        '-127/-10127/5493c199-d4f9-4dc3-b89d-75b50b4d6d4b.jpg');

INSERT INTO `aduser` (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`, `usStatus`, `usFullName`,
                      `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`, `usAddedBy`,
                      `usAddTime`, `usLastModifiedBy`, `usLastModificationTime`, `usExchange`, `usProfilePicturePath`)
VALUES ('500002', 'cgm_overseas', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', '500002', '', '0', 'Maruful Haque Hamidi',
        'Deputy General Manager', 'Uttara-12', '', '', '', '4127', '1484454823461', '4127', '1495361672567', '0',
        '-127/-10127/5493c199-d4f9-4dc3-b89d-75b50b4d6d4b.jpg');

INSERT INTO `aduser` (`usUserID`, `usUserName`, `usPassword`, `usRoleID`, `usMailAddr`, `usStatus`, `usFullName`,
                      `usDesignation`, `usAddress`, `usPhoneNo`, `usAdditionalInfo`, `usSecurityToken`, `usAddedBy`,
                      `usAddTime`, `usLastModifiedBy`, `usLastModificationTime`, `usExchange`, `usProfilePicturePath`)
VALUES ('500003', 'gm_international', 'qvTGHdzF6KLavt4PO0gs2a6pQ00=', '500003', '', '0', 'Maruful Haque Hamidi',
        'Deputy General Manager', 'Uttara-12', '', '', '', '4127', '1484454823461', '4127', '1495361672567', '0',
        '-127/-10127/5493c199-d4f9-4dc3-b89d-75b50b4d6d4b.jpg');


-- 24/03/19


CREATE TABLE `upstream_inventory`
(
    `id`        BIGINT      NOT NULL,
    `item_type` VARCHAR(50) NOT NULL,
    `item_name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
)
    COLLATE = 'latin1_swedish_ci';

INSERT INTO `vbSequencer` (`table_name`, `next_id`)
VALUES ('upstream_inventory', '1000');



INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('1', 'TYPE_OF_BW', 'IP Transite');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('2', 'TYPE_OF_BW', 'Peering');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('3', 'TYPE_OF_BW', 'IPLC');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('4', 'MEDIA', 'SMW4');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('5', 'MEDIA', 'SMW5');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('6', 'MEDIA', 'ITC');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('7', 'BTCL_SERVICE_LOCATION', 'Moghbazar');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('8', 'BTCL_SERVICE_LOCATION', 'Chittagong');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('9', 'BTCL_SERVICE_LOCATION', 'Jessore');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)
VALUES ('10', 'PROVIDER_LOCATION', 'Singapore');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)VALUES ('11', 'PROVIDER_LOCATION', 'Palwal');



INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)VALUES ('100', 'PROVIDER', 'Singtel');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)VALUES ('101', 'PROVIDER', 'Palwal');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)VALUES ('102', 'PROVIDER', 'xyz');
INSERT INTO `upstream_inventory` (`id`, `item_type`, `item_name`)VALUES ('103', 'PROVIDER', 'abc');


CREATE TABLE `upstream_application`
(
    `application_id`           BIGINT       NOT NULL,
    `type_of_bw_id`            BIGINT       NOT NULL,
    `bw_capacity`              DOUBLE       NOT NULL,
    `media_id`                 BIGINT       NOT NULL,
    `btcl_service_location_id` BIGINT       NOT NULL,
    `provider_location_id`     BIGINT       NOT NULL,
    `selected_provider_id`     BIGINT       NULL,
    `bw_price`                 DOUBLE       NOT NULL,
    `otc`                      DOUBLE       NULL,
    `mrc`                      DOUBLE       NULL,
    `contract_duration`        DOUBLE       NOT NULL,
    `srf_date`                 BIGINT       NULL,
    `circuit_info_link`        VARCHAR(50)  NOT NULL DEFAULT '""',
    `application_date`         BIGINT       NOT NULL,
    `state`                    BIGINT       NULL,
    `application_status`       VARCHAR(250) NOT NULL DEFAULT '""',
    PRIMARY KEY (`application_id`)
)
    COLLATE = 'latin1_swedish_ci';


INSERT INTO vbSequencer (table_name, next_id, table_LastModificationTime)
VALUES ('upstream_application', 1, 1552280186722);


ALTER TABLE `upstream_application`
    CHANGE COLUMN `circuit_info_link` `circuit_info_link` VARCHAR(50) NULL DEFAULT '""' AFTER `srf_date`;



ALTER TABLE `upstream_application`
    ALTER `contract_duration` DROP DEFAULT;
ALTER TABLE `upstream_application`
    CHANGE COLUMN `contract_duration` `contract_duration` DOUBLE NULL AFTER `mrc`,
    CHANGE COLUMN `application_status` `application_status` VARCHAR(50) NULL DEFAULT '""' AFTER `state`;



ALTER TABLE `upstream_application`
    CHANGE COLUMN `bw_price` `bw_price` DOUBLE NOT NULL DEFAULT '0' AFTER `selected_provider_id`;


CREATE TABLE `upstream_vendor`
(
    `id`              BIGINT      NOT NULL,
    `vendor_name`     VARCHAR(50) NOT NULL,
    `vendor_location` BIGINT      NULL,
    PRIMARY KEY (`id`)
)
    COLLATE = 'latin1_swedish_ci'
;

ALTER TABLE `upstream_vendor`
    CHANGE COLUMN `vendor_location` `vendor_location_id` BIGINT(20) NULL DEFAULT NULL AFTER `vendor_name`;


ALTER TABLE `upstream_application`
    ADD COLUMN `application_type` VARCHAR(50) NOT NULL AFTER `application_id`;


CREATE TABLE `upstream_circuit_info`
(
    `id`             BIGINT      NOT NULL,
    `info_type`      VARCHAR(50) NULL,
    `application_id` BIGINT      NOT NULL,
    `circuit_id`     BIGINT      NULL,
    `location`       VARCHAR(50) NULL,
    `device_name`    VARCHAR(50) NULL,
    `shelf_number`   BIGINT      NULL,
    `card_number`    BIGINT      NULL,
    `port_number`    BIGINT      NULL,
    PRIMARY KEY (`id`)
)
    COLLATE = 'latin1_swedish_ci'
;

INSERT INTO `vbSequencer` (`table_name`, `next_id`)
VALUES ('upstream_circuit_info', '1');


CREATE TABLE `upstream_contract`
(
    `id`                       BIGINT      NOT NULL,
    `contract_history_id`      BIGINT      NOT NULL,
    `application_id`           BIGINT      NOT NULL,
    `application_type`         VARCHAR(50) NULL,
    `type_of_bw_id`            BIGINT      NOT NULL,
    `bw_capacity`              DOUBLE      NOT NULL,
    `media_id`                 BIGINT      NOT NULL,
    `btcl_service_location_id` BIGINT      NOT NULL,
    `provider_location_id`     BIGINT      NOT NULL,
    `selected_provider_id`     BIGINT      NULL,
    `bw_price`                 DOUBLE      NOT NULL DEFAULT '0',
    `otc`                      DOUBLE      NOT NULL DEFAULT '0',
    `mrc`                      DOUBLE      NOT NULL DEFAULT '0',
    `double`                   DOUBLE      NOT NULL DEFAULT '0',
    `srf_date`                 BIGINT      NULL,
    `circuit_info_link`        VARCHAR(50) NULL,
    `active_from`              BIGINT      NOT NULL DEFAULT '0',
    `active_to`                BIGINT      NOT NULL DEFAULT '0',
    `valid_from`               BIGINT      NOT NULL DEFAULT '0',
    `valid_to`                 BIGINT      NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
)
    COLLATE = 'latin1_swedish_ci'
;
alter table upstream_contract
    add contract_duration double null;
INSERT INTO `vbSequencer` (`table_name`, `next_id`)
VALUES ('upstream_contract', '2000');

alter table upstream_contract
    add contract_name varchar(255) null;

ALTER TABLE `upstream_contract`
    DROP COLUMN `double`;

alter table upstream_application
    add contract_id bigint null;

ALTER TABLE `at_uploaded_file`
    DROP COLUMN `stateId`;

alter table upstream_circuit_info
    add contract_id bigint null;

alter table upstream_circuit_info
    drop column application_id;

-- add reject state
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50012, 50003, 50009, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50013, 50004, 50009, null);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`)
VALUES (50012, 50012, 500002);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`)
VALUES (50013, 50013, 500003);



DELETE
FROM `flow_state_transition`
WHERE `id` = 50005;
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50015, 50008, 50003, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50016, 50008, 50007, null);
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`)
VALUES (50017, 50008, 50009, null);



INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`)
VALUES (50015, 50015, 500001);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`)
VALUES (50016, 50016, 500001);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`)
VALUES (50017, 50017, 500001);


INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`)
VALUES (50001, -1, 'Upstream', 11);
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`)
VALUES (50002, 50001, 'New Request', 11);
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`)
VALUES (50003, 50001, 'Application List', 11);
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`)
VALUES (50004, 50001, 'Contract List', 11);
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`)
VALUES (50005, 50001, 'Contract Bandwidth Change', 11);
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`)
VALUES (50006, 50001, 'Contract Close', 11);
INSERT INTO  `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`)
VALUES (50007, 50001, 'Contract Extension', 11);

INSERT INTO  `at_global_config` (`gcID`, `gcName`, `gcValue`) VALUES (11, 'Upstream', 1)