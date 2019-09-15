-- 3/2/19

CREATE TABLE `vpn_probable_td_client` (
	`id` BIGINT(20) NOT NULL,
	`clientID` BIGINT(20) NULL DEFAULT NULL,
	`tdDate` BIGINT(20) NULL DEFAULT NULL,
	PRIMARY KEY (`id`) USING BTREE
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('vpn_probable_td_client', 1, 0);


CREATE TABLE `vpn_client_td` (
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

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('vpn_client_td', 1, 0);


-- td flow design 
	
	-- Flow State

INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7500, 'CLIENT_TD_SUBMITTED', 4, 'Submitted For Temporary Disconnect', '#f99a2f', 'Temporary Disconnection Submitted');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7501, 'TD_AN', 4, 'Advice Note Generate', '#f99a2f', 'Advice Note Generated');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7502, 'TD_DONE', 4, 'Disconnect Client', '#f99a2f', 'Client Disconnected');
	
	-- flow state transition

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (7500, 7500, 7501, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (7501, 7501, 7502, 'NULL');

	-- flow state transition role

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (7500, 7500, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (7501, 7501, 16021);

-- 3/4/19

-- Reconnect flow design

-- Flow State

INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7503, 'CLIENT_RECONNECTION_SUBMITTED', 5, 'Submitted for Reconnection', '#f99a2f', 'Submitted for Reconnection');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7504, 'GENERATE_DEMAND_NOTE', 5, 'Generate Demand Note', '#f99a2f', 'Demand Note Generated');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7505, 'GIVE_PAYMENT', 5, 'Give Payment', '#f99a2f', 'Payment Verification Needed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7506, 'VERIFY_PAYMENT', 5, 'Verify Payment', '#008000', 'Payment Verified');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7507, 'GENERATE_AN', 5, 'Generate Advice Note', '#f99a2f', 'Advice Note Generated');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7508, 'COMPLETE_RECONNECTION', 5, 'Reconnect Client', '#008000', 'Client Reconnected');

-- flow state transition

INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (7503, 7503, 7504, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (7504, 7504, 7505, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (7505, 7505, 7506, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (7506, 7506, 7507, 'NULL');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (7507, 7507, 7508, 'NULL');

-- flow state transition role

INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (7503, 7503, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (7504, 7504, -1);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (7505, 7505, 6020);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (7506, 7506, 22021);
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (7507, 7507, 16021);


alter table vpn_application_link modify link_state varchar(50) not null;


-- TD Flow change

INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7509, 'TD_REJECTED', 4, 'Reject Application', '#f99a2f', 'Application Rejected');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (7502, 7500, 7509, 'NULL');
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (7502, 7502, 22021);

-- reconnect flow change

INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7510, 'TD_REJECTED', 4, 'Reject Application', '#f99a2f', 'Application Rejected');
INSERT INTO `flow_state_transition` (`id`, `source`, `destination`, `comment`) VALUES (7508, 7503, 7510, 'NULL');
INSERT INTO `flow_state_transition_role` (`id`, `flow_state_transition`, `role`) VALUES (7508, 7508, 22021);

ALTER TABLE application add second_client BIGINT DEFAULT 0 NULL after client_id;
