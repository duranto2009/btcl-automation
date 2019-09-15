DROP TABLE IF EXISTS flow_state_transition_role;
DROP TABLE IF EXISTS flow_state_transition;
DROP TABLE IF EXISTS flow_state;
DROP TABLE IF EXISTS flow;
DROP TABLE IF EXISTS component;
DROP TABLE IF EXISTS module;


CREATE TABLE module (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) DEFAULT NULL
);

INSERT INTO module (name) VALUES ('LLI');


CREATE TABLE component (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) DEFAULT NULL,
    module INT DEFAULT NULL,
    FOREIGN KEY (module) REFERENCES module (id) ON UPDATE CASCADE ON DELETE SET NULL
);

INSERT INTO component (name, module) VALUES ('Connection Application', (SELECT id FROM module WHERE name = 'LLI'));


CREATE TABLE flow (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) DEFAULT NULL,
    component INT DEFAULT NULL,
    FOREIGN KEY (component) REFERENCES component (id) ON UPDATE CASCADE ON DELETE SET NULL    
);

INSERT INTO flow (name, "module") VALUES ('New Connection Application Flow', (SELECT id FROM component WHERE name = 'Connection Application'));


CREATE TABLE flow_state (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) DEFAULT NULL,
    flow INT DEFAULT NULL,
    FOREIGN KEY (flow) REFERENCES flow (id) ON UPDATE CASCADE ON DELETE SET NULL    
);

INSERT INTO flow_state (name, flow) VALUES ('SUBMITTED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('PRECHECK_WIP', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('PRECHECK_DONE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow')); 
INSERT INTO flow_state (name, flow) VALUES ('PROCESS_STARTED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('IFR_WIP', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('IFR_NEGATIVE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('REJECTED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('REQUESTED_TO_CENTRAL', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('REQUESTED_FOR_RESOURCE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('IFR2_WIP', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('IFR2_NEGATIVE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('IFR2_POSITIVE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('EFR2_WIP', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('EFR2_OUTSOURCED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('EFR2_DONE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('RESPONDED_TO_CENTRAL', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('REQUESTED_RESOURCE_AVAILABLE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('IFR_POSITIVE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('IFR_DONE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('EFR_WIP', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('EFR_OUTSOURCED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('EFR_DONE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('DEMAND_NOTE_PUBLISHED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('PAYMENT_DONE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('PAYMENT_VERIFIED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('WORK_ORDERED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('WORK_DONE', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('TESTED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));
INSERT INTO flow_state (name, flow) VALUES ('ADVICE_NOTE_PUBLISHED', (SELECT id FROM flow WHERE name = 'New Connection Application Flow'));


CREATE TABLE flow_state_transition (
    id INT AUTO_INCREMENT PRIMARY KEY,
    source INT DEFAULT NULL,
    destination INT DEFAULT NULL,
    comment VARCHAR(255) DEFAULT NULL,
    FOREIGN KEY (source) REFERENCES flow_state (id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (destination) REFERENCES flow_state (id) ON UPDATE CASCADE ON DELETE SET NULL
);

INSERT INTO flow_state_transition (source, destination) VALUES ((SELECT id FROM flow_state WHERE name = 'SUBMITTED'), (SELECT id FROM flow_state WHERE name = 'PRECHECK_WIP'));
INSERT INTO flow_state_transition (source, destination) VALUES ((SELECT id FROM flow_state WHERE name = 'PROCESS_STARTED'), (SELECT id FROM flow_state WHERE name = 'IFR_WIP'));
INSERT INTO flow_state_transition (source, destination) VALUES ((SELECT id FROM flow_state WHERE name = 'EFR_DONE'), (SELECT id FROM flow_state WHERE name = 'DEMAND_NOTE_PUBLISHED'));
INSERT INTO flow_state_transition (source, destination) VALUES ((SELECT id FROM flow_state WHERE name = 'EFR_DONE'), (SELECT id FROM flow_state WHERE name = 'EFR_WIP'));


CREATE TABLE flow_state_transition_role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    flow_state_transition INT DEFAULT NULL,
    role BIGINT DEFAULT NULL,
    FOREIGN KEY (flow_state_transition) REFERENCES flow_state_transition (id) ON UPDATE CASCADE ON DELETE SET NULL, 
    FOREIGN KEY (role) REFERENCES  adrole(rlRoleID) ON UPDATE CASCADE ON DELETE SET NULL
);

INSERT INTO flow_state_transition_role (flow_state_transition, role) VALUES (
    (SELECT id FROM flow_state_transition WHERE source = (SELECT id FROM flow_state WHERE name = 'SUBMITTED') AND destination = (SELECT id FROM flow_state WHERE name = 'PRECHECK_WIP')), 
    (SELECT rlRoleID FROM adrole WHERE rlRoleName = 'DGM'));

INSERT INTO flow_state_transition_role (flow_state_transition, role) VALUES (
    (SELECT id FROM flow_state_transition WHERE source = (SELECT id FROM flow_state WHERE name = 'PROCESS_STARTED') AND destination = (SELECT id FROM flow_state WHERE name = 'IFR_WIP')),
    (SELECT rlRoleID FROM adrole WHERE rlRoleName = 'DGM'));


ALTER TABLE flow_state ADD COLUMN description VARCHAR(1023) DEFAULT NULL;

ALTER TABLE flow_state ADD COLUMN color VARCHAR(255) DEFAULT NULL;

ALTER TABLE flow_state ADD COLUMN view_description VARCHAR(2047) DEFAULT NULL;




INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('module', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('component', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('flow', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('flow_state', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('flow_state_transition', 1, 0);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime) VALUES ('flow_state_transition_role', 1, 0);


-- start : new long term client contract --

INSERT INTO component (name, module) VALUES ('Client Contract', (SELECT id from module where name = 'LLI'));

INSERT INTO flow (name, "module") VALUES ('New Long Term', (SELECT id from component where name = 'Client Contract'));

INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('NEW_LONG_TERM_SUBMIT', (SELECT id from flow where name = 'New Long Term'), 'Long Term Submitted', '#f99a2f', 'Long Term Submitted');
INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('NEW_LONG_TERM_COMPLETE', (SELECT id from flow where name = 'New Long Term'), 'Complete Long Term', '#008000', 'Long Term Completed');
INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('LONG_TERM_REJECTED', (SELECT id from flow where name = 'New Long Term'), 'Reject Long Term', '#800000', 'Long Term Rejected');
INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('LONG_TERM_REOPENED', (SELECT id from flow where name = 'New Long Term'), 'Reopen Long Term', '#f99a2f', 'Long Term Reopened');

INSERT INTO flow_state_transition (source, destination) VALUES ((SELECT id from flow_state where name = 'NEW_LONG_TERM_SUBMIT'), (SELECT id from flow_state where name = 'LONG_TERM_REJECTED'));
INSERT INTO flow_state_transition (source, destination) VALUES ((SELECT id from flow_state where name = 'NEW_LONG_TERM_SUBMIT'), (SELECT id from flow_state where name = 'NEW_LONG_TERM_COMPLETE'));
INSERT INTO flow_state_transition (source, destination) VALUES ((SELECT id from flow_state where name = 'LONG_TERM_REJECTED'), (SELECT id from flow_state where name = 'LONG_TERM_REOPENED'));
INSERT INTO flow_state_transition (source, destination) VALUES ((SELECT id from flow_state where name = 'LONG_TERM_REOPENED'), (SELECT id from flow_state where name = 'LONG_TERM_REJECTED'));
INSERT INTO flow_state_transition (source, destination) VALUES ((SELECT id from flow_state where name = 'LONG_TERM_REOPENED'), (SELECT id from flow_state where name = 'NEW_LONG_TERM_COMPLETE'));

INSERT INTO flow_state_transition_role (flow_state_transition, role) VALUES ((SELECT id from flow_state_transition where source = (SELECT id from flow_state where name = 'NEW_LONG_TERM_SUBMIT') and destination = (SELECT id from flow_state where name = 'LONG_TERM_REJECTED')), (SELECT rlRoleID from adrole where rlRoleName = 'CDGM'));
INSERT INTO flow_state_transition_role (flow_state_transition, role) VALUES ((SELECT id from flow_state_transition where source = (SELECT id from flow_state where name = 'NEW_LONG_TERM_SUBMIT') and destination = (SELECT id from flow_state where name = 'NEW_LONG_TERM_COMPLETE')), (SELECT rlRoleID from adrole where rlRoleName = 'CDGM'));
INSERT INTO flow_state_transition_role (flow_state_transition, role) VALUES ((SELECT id from flow_state_transition where source = (SELECT id from flow_state where name = 'LONG_TERM_REJECTED') and destination = (SELECT id from flow_state where name = 'LONG_TERM_REOPENED')), (SELECT rlRoleID from adrole where rlRoleName = 'CDGM'));
INSERT INTO flow_state_transition_role (flow_state_transition, role) VALUES ((SELECT id from flow_state_transition where source = (SELECT id from flow_state where name = 'LONG_TERM_REOPENED') and destination = (SELECT id from flow_state where name = 'LONG_TERM_REJECTED')), (SELECT rlRoleID from adrole where rlRoleName = 'CDGM'));
INSERT INTO flow_state_transition_role (flow_state_transition, role) VALUES ((SELECT id from flow_state_transition where source = (SELECT id from flow_state where name = 'LONG_TERM_REOPENED') and destination = (SELECT id from flow_state where name = 'NEW_LONG_TERM_COMPLETE')), (SELECT rlRoleID from adrole where rlRoleName = 'CDGM'));

ALTER TABLE at_lli_connection_revise_client ADD COLUMN bandwidth float DEFAULT 0;

--. end : new long term client contract --

-- start: break long term contract

-- alter

ALTER TABLE at_lli_connection_revise_client ADD COLUMN referenceContract bigint(20) DEFAULT 0;


-- states

INSERT INTO flow (name, "module") VALUES ('Break Long Term', (SELECT id FROM component WHERE name = 'Client Contract'));

INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('BREAK_LONG_TERM_SUBMIT', (SELECT id FROM flow WHERE name = 'Break Long Term'), 'Break Long Term Submitted', '#f99a2f', 'Break Long Term Submitted');

INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('BREAK_LONG_TERM_APPROVED', (SELECT id FROM flow WHERE name = 'Break Long Term'), 'Break Long Term Approved', '#f99a2f', 'Approved and Demand Note Issued');

INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('BREAK_LONG_TERM_REJECTED', (SELECT id FROM flow WHERE name = 'Break Long Term'), 'Reject Long Term', '#800000', 'Long Term Rejected');

INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('BREAK_LONG_TERM_REOPENED', (SELECT id FROM flow WHERE name = 'Break Long Term'), 'Reopen Long Term', '#f99a2f', 'Long Term Reopened');

INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('LONG_TERM_PAYMENT_DONE', (SELECT id FROM flow WHERE name = 'Break Long Term'), 'Payment done against issued Demand Note', '#f99a2f', 'Demand Note Paid');

INSERT INTO flow_state (name, flow, description, color, view_description) VALUES ('LONG_TERM_PAYMENT_VERIFIED', (SELECT id FROM flow WHERE name = 'Break Long Term'), 'Payment Verified', '#008000', 'Payment Verified');

insert into flow_state (name, flow, description, color, view_description) values ('BREAK_LONG_TERM_COMPLETE', (select id from flow where name = 'Break Long Term'), 'Complete Break Long Term', '#008000', 'Break Long Term Completed');


-- transitions

insert into flow_state_transition (source, destination) values ((select id from flow_state where name = 'BREAK_LONG_TERM_SUBMIT'), (select id from flow_state where name = 'BREAK_LONG_TERM_REJECTED'));

insert into flow_state_transition (source, destination) values ((select id from flow_state where name = 'BREAK_LONG_TERM_REJECTED'), (select id from flow_state where name = 'BREAK_LONG_TERM_REOPENED'));

insert into flow_state_transition (source, destination) values ((select id from flow_state where name = 'BREAK_LONG_TERM_REOPENED'), (select id from flow_state where name = 'BREAK_LONG_TERM_REJECTED'));

insert into flow_state_transition (source, destination) values ((select id from flow_state where name = 'BREAK_LONG_TERM_SUBMIT'), (select id from flow_state where name = 'BREAK_LONG_TERM_APPROVED'));

insert into flow_state_transition (source, destination) values ((select id from flow_state where name = 'BREAK_LONG_TERM_REOPENED'), (select id from flow_state where name = 'BREAK_LONG_TERM_APPROVED'));

insert into flow_state_transition (source, destination) values ((select id from flow_state where name = 'BREAK_LONG_TERM_APPROVED'), (select id from flow_state where name = 'LONG_TERM_PAYMENT_DONE'));

insert into flow_state_transition (source, destination) values ((select id from flow_state where name = 'LONG_TERM_PAYMENT_DONE'), (select id from flow_state where name = 'LONG_TERM_PAYMENT_VERIFIED'));

insert into flow_state_transition (source, destination) values ((select id from flow_state where name = 'LONG_TERM_PAYMENT_VERIFIED'), (select id from flow_state where name = 'BREAK_LONG_TERM_COMPLETE'));


-- role

insert into flow_state_transition_role (flow_state_transition, role) VALUES ((select id from flow_state_transition where source = (select id from flow_state where name = 'BREAK_LONG_TERM_SUBMIT') and destination = (select id from flow_state where name = 'BREAK_LONG_TERM_REJECTED')), (select rlRoleID from adrole where rlRoleName = 'CDGM'));

insert into flow_state_transition_role (flow_state_transition, role) VALUES ((select id from flow_state_transition where source = (select id from flow_state where name = 'BREAK_LONG_TERM_SUBMIT') and destination = (select id from flow_state where name = 'BREAK_LONG_TERM_APPROVED')), (select rlRoleID from adrole where rlRoleName = 'CDGM'));

insert into flow_state_transition_role (flow_state_transition, role) VALUES ((select id from flow_state_transition where source = (select id from flow_state where name = 'BREAK_LONG_TERM_REJECTED') and destination = (select id from flow_state where name = 'BREAK_LONG_TERM_REOPENED')), (select rlRoleID from adrole where rlRoleName = 'CDGM'));

insert into flow_state_transition_role (flow_state_transition, role) VALUES ((select id from flow_state_transition where source = (select id from flow_state where name = 'BREAK_LONG_TERM_REOPENED') and destination = (select id from flow_state where name = 'BREAK_LONG_TERM_REJECTED')), (select rlRoleID from adrole where rlRoleName = 'CDGM'));

insert into flow_state_transition_role (flow_state_transition, role) VALUES ((select id from flow_state_transition where source = (select id from flow_state where name = 'BREAK_LONG_TERM_REOPENED') and destination = (select id from flow_state where name = 'BREAK_LONG_TERM_APPROVED')), (select rlRoleID from adrole where rlRoleName = 'CDGM'));

insert into flow_state_transition_role (flow_state_transition, role) VALUES ((select id from flow_state_transition where source = (select id from flow_state where name = 'BREAK_LONG_TERM_APPROVED') and destination = (select id from flow_state where name = 'LONG_TERM_PAYMENT_DONE')), -1);

insert into flow_state_transition_role (flow_state_transition, role) VALUES ((select id from flow_state_transition where source = (select id from flow_state where name = 'LONG_TERM_PAYMENT_DONE') and destination = (select id from flow_state where name = 'LONG_TERM_PAYMENT_VERIFIED')), (select rlRoleID from adrole where rlRoleName = 'Revenue'));

insert into flow_state_transition_role (flow_state_transition, role) VALUES ((select id from flow_state_transition where source = (select id from flow_state where name = 'LONG_TERM_PAYMENT_VERIFIED') and destination = (select id from flow_state where name = 'BREAK_LONG_TERM_COMPLETE')), (select rlRoleID from adrole where rlRoleName = 'CDGM'));

--. end: break long term contract