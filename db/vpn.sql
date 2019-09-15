drop table if exists application;
create table application
(
	application_id bigint not null
		primary key,
	client_id bigint not null,
	application_type varchar(45) not null,
	application_state varchar(100) not null,
	user_id bigint null,
	module_id int not null,
	submission_date bigint not null,
	suggested_date bigint not null,
	created_date bigint not null,
	last_modification_time bigint not null,
	is_service_started bit not null,
	class_name varchar(100) not null
);
drop table if exists efr;
create table efr
(
	id int auto_increment
		primary key,
	application_id int null,
	module_id int null,
	office_id int null,
	pop_id int null,
	parent_efr_id int null,
	bandwidth int null,
	source_type int null,
	source varchar(255) null,
	destination_type int null,
	destination varchar(255) null,
	is_replied bit default b'0' null,
	quotation_deadline bigint null,
	proposed_loop_distance int null,
	vendor_id int null,
	vendor_type int null,
	is_selected bit default b'0' null,
	is_completed bit default b'0' null,
	work_deadline bigint null,
	ofc_type int null,
	is_forwarded bit null,
	actual_loop_distance int default '0' null,
	loop_distance_is_approved int default '0' null,
	is_ignored bit default b'0' null,
	is_work_ordered bit default b'0' null
);
drop table if exists local_loop;
create table local_loop
(
	id bigint not null
		primary key,
	office_id bigint not null,
	application_id bigint not null,
	district_id bigint not null,
	pop_id bigint not null,
	port_id bigint not null,
	router_or_switch_id bigint not null,
	vlan_id bigint not null,
	ofc_type varchar(45) not null,
	port_type varchar(45) not null,
	btcl_distance int not null,
	oc_distance int not null,
	vendor_id bigint not null,
	loop_provider varchar(45) not null,
	ifr_feasibility bit default b'0' null,
	is_completed bit default b'0' null,
	zone_id int default '0' not null,
	is_distributed bit default b'0' null,
	adjusted_btcl_distance int null,
	adjusted_oc_distance int null,
	adjustment_approved_by bigint null,
	is_adjusted bit default b'0' null
)
;
drop table if exists office;
create table office
(
	id bigint not null
		primary key,
	name varchar(45) not null,
	address varchar(100) not null,
	created_date bigint not null,
	client_id bigint not null,
	module_id int not null
)
;
drop table if exists vpn_application;
create table vpn_application
(
	vpn_application_id bigint not null
		primary key,
	parent_application_id bigint not null,
	network_type varchar(45) not null,
	network_id bigint not null,
	layer_type varchar(45) not null,
	demand_note_id bigint null,
	skip_payment bit not null,
	is_demand_note_aggregated bit not null
)
;
drop table if exists vpn_application_link;
create table vpn_application_link
(
	id bigint not null
		primary key,
	link_name varchar(45) null,
	link_bandwidth int not null,
	local_office_id bigint not null,
	remote_office_id bigint not null,
	link_state varchar(45) not null,
	local_zone_id int not null,
	remote_zone_id int not null,
	vpn_application_id bigint not null,
	demand_note_id bigint null,
	skip_payment bit not null,
	ifr_feasibility bit not null,
	local_office_terminal_device_provider varchar(45) not null,
	remote_office_terminal_device_provider varchar(45) not null,
	is_forwarded bit not null,
	local_office_loop_id bigint null,
	remote_office_loop_id bigint null,
	is_service_started bit default b'0' null
)
;
drop table if exists vpn_dn;
create table vpn_dn
(
	id bigint not null
		primary key,
	parent_bill_id bigint not null,
	security_charge double not null,
	registration_charge double not null,
	bandwidth_charge double not null,
	local_loop_charge double not null,
	degradation_charge double not null,
	reconnect_charge double not null,
	closing_charge double not null,
	shifting_charge double not null,
	ownership_change_charge double not null,
	other_charge double not null,
	advance double not null,
	application_group varchar(20) not null
)
;
drop table if exists vpn_network;
create table vpn_network
(
	id bigint not null
		primary key,
	name varchar(45) not null,
	client_id bigint not null,
	network_type varchar(45) not null,
	status varchar(45) not null,
	active_from bigint not null,
	active_to bigint not null,
	valid_from bigint not null,
	valid_to bigint not null,
	discount_rate double not null,
	history_id bigint not null
)
;
drop table if exists vpn_network_link;
create table vpn_network_link
(
	id bigint not null
		primary key,
	history_id bigint not null,
	clientId bigint not null,
	incident_type varchar(45) not null,
	link_name varchar(45) null,
	link_status varchar(45) not null,
	active_from bigint not null,
	active_to bigint not null,
	valid_from bigint not null,
	valid_to bigint not null,
	link_bandwidth int not null,
	local_office_id bigint not null,
	remote_office_id bigint not null,
	local_zone_id bigint not null,
	remote_zone_id bigint not null,
	local_office_terminal_device_provider varchar(45) not null,
	remote_office_terminal_device_provider varchar(45) not null,
	network_history_id bigint not null,
	application_id bigint null
);
drop table if exists local_loop_consumer_mapping;
create table local_loop_consumer_mapping
(
	id bigint not null
		primary key,
	local_loop_id bigint null,
	consumer_id bigint null,
	consumer_type varchar(255) null,
	consumer_module_id bigint null,
	from_date bigint default '0' null,
	to_date bigint default '0' null,
	is_active bit default b'0' null,
	bill_applicable bit default b'0' null
)
;

INSERT IGNORE INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('application', 1, 0);
INSERT IGNORE INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('efr', 1, DEFAULT);
INSERT IGNORE INTO vbSequencer (table_name, next_id, table_LastModificationTime)
VALUES ('local_loop', 19001, 1551158194658);
INSERT IGNORE INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('local_loop_consumer_mapping', 1001, DEFAULT);
INSERT IGNORE INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('office', 1, DEFAULT);
INSERT IGNORE INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('vpn_application', 1001, DEFAULT);
INSERT IGNORE INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('vpn_application_link', 1, DEFAULT);
INSERT IGNORE INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('vpn_network', 1, DEFAULT);
INSERT IGNORE INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('vpn_network_link', 1, DEFAULT);
INSERT IGNORE INTO vbSequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('vpn_dn', 1, DEFAULT);





INSERT INTO module (`id`, `name`) VALUES (20, 'VPN');
INSERT INTO component (`id`, `name`, `module`)
VALUES (20, 'Connection Application', 20);
INSERT INTO flow (`id`, `name`, `component`) VALUES (2001, 'New Connection Flow', 20);


delete from flow_state where id >=7000;
delete from flow_state_transition where id >=7000;
delete from flow_state_transition_role where id >=7000;
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7001, 'SUBMITTED', 2001, 'Submit Application', '#f99a2f', 'Application Submitted');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7002, 'PRECHECK_DONE', 2001, 'Submit Correction', '#008000', 'Client Correction Submitted');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7003, 'REJECTED', 2001, 'Reject Application', '#ff0000', 'Application Rejected');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7004, 'REQUESTED_TO_CENTRAL', 2001, 'Forward to CDGM(Broadbrand-1)', '#f99a2f', 'Application Forwarded by LDGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7005, 'APPROVE_AND_FORWRAD', 2001, 'Approve and forward to CDGM', '#f99a2f', 'Job Completed and Forwarded');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7006, 'RESPONDED_TO_CENTRAL', 2001, 'Forward To Central DGM', '#f99a2f', 'Forwarded From Local DGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7007, 'GIVE_WORK_ORDER', 2001, 'Give Work Order', '#f99a2f', 'Work Order Given');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7008, 'JOB_DONE', 2001, 'Complete Work Order', '#f99a2f', 'Work Order Completed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7009, 'FORWARD_FOR_WORK_ORDER', 2001, 'Forward LDGM for Work Order', '#f99a2f', 'Forwarded from CDGM for work order');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7010, 'EFR_WIP', 2001, 'EFR RESPONSE', '#008000', 'External FR Responded');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7011, 'EFR_DONE', 2001, 'EFR Done', '#008000', 'External FR Completed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7012, 'PAYMENT_DONE', 2001, 'Give Payment ', '#f99a2f', 'Payment Verification Needed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7013, 'PAYMENT_VERIFIED', 2001, 'Verify Payment', '#008000', 'Payment Verified');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7014, 'WORK_DONE', 2001, 'Job Done', '#008000', 'Work Order Completed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7015, 'TESTING_PENDING', 2001, 'Complete Testing ', '#f99a2f', 'Testing Completed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7016, 'ADVICE_NOTE_PUBLISH', 2001, 'Publish Advice Note', '#f99a2f', 'Advice note published');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7017, 'IFR_SUBMITTED', 2001, ' Internal FR Request', '#f99a2f', 'Internal FR requested');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7018, 'IFR_RESPONDED', 2001, 'Response Internal FR ', '#008000', 'Internal FR Responded');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7019, 'EFR_REQUESTED', 2001, 'Request for External FR', '#f99a2f', 'External FR requested');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7020, 'DEMAND_NOTE_GENERATED', 2001, 'Generate Demand Note', '#f99a2f', 'Demand note generated');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7021, 'REQUEST_FOR_ACCOUNT_CC', 2001, 'Request Account For CC', '#f99a2f', 'Requested for Account CC');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7022, 'REQUESTED_CLIENT_FOR_CORRECTION', 2001, 'Request to client for correction', '#f99a2f', 'Requested correction for submitted app');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7023, 'ACCOUNT_CC_POSITIVE', 2001, 'Account Payment Condition Positive', '#008000', 'Positive response from revenue');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7024, 'ACCOUNT_CC_NEGETIVE', 2001, 'Account Payment Condition Negetive', '#ff0000', ' Negetive Response From Revenue');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7025, 'VPN_GENERATE_WORK_ORDER', 2001, 'Generate work order', '#f99a2f', 'Work Order Generated');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7026, 'TESTING_DONE', 2001, 'Complete Setup', '#008000', 'Setup Completed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7027, 'CONNECTION_COMPLETE', 2001, 'Complete Connection', '#008000', 'Connection Completed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7028, 'APPLICATION_REOPEN', 2001, 'Reopen Application', '#f99a2f', 'Application Reopened');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7029, 'FORWARD_TO_MUX_TEAM', 2001, 'forward to mux team', '#f99a2f', 'Application forwarded from server room');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7030, 'MUX_CONFIGURE_DONE', 2001, 'Complete Mux configuration', '#008000', 'Mux configuration Done');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7031, 'IFR_2_REQUESTED', 2001, 'Request For Internal FR', '#f99a2f', 'Internal FR Requested');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7032, 'IFR_2_RESPONDED', 2001, 'Response IFR', '#008000', 'IFR Responded');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7033, 'EFR_2_REQUESTED', 2001, 'Request external FR', '#f99a2f', 'External FR Requested');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7034, 'EFR_2_RESPONDED', 2001, 'Response EFR', '#008000', 'EFR Responded');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7035, 'REQUESTED_TO_LOCAL', 2001, 'Forward To Nearby Local DGM', '#f99a2f', 'Application Forwarded from Central DGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7036, 'MOVE_BACK_TO_ORIGINAL', 2001, 'Retransfer to original Local DGM', '#f99a2f', 'Applicatio Retransfarred');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7037, 'IFR_2_INCOMPLETE', 2001, 'Give Negeive IFR to Central DGM', '#ff0000', 'Negetive IFR Report From Local DGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7038, 'WITHOUT_LOOP_SUBMITTED', 2001, 'Application Submitted', '#f99a2f', 'Application Submitted');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7039, 'WITHOUT_LOOP_IFR_REQUEST', 2001, 'Request Internal FR', '#f99a2f', 'Internal FR Requested');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7040, 'WITHOUT_LOOP_REQUESTED_CLIENT_FOR_CORRECTION', 2001, 'Request for Correction', '#f99a2f', 'Requested correction for submitted app');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7041, 'WITHOUT_LOOP_REJECTED', 2001, 'Reject Application', '#ff0000', 'Application Rejected');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7042, 'WITHOUT_LOOP_REQUEST_FOR_ACCOUNT_CC', 2001, 'Request Account For CC', '#f99a2f', 'Requested for Account CC');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7043, 'WITHOUT_LOOP_IFR_RESPONSE', 2001, 'Response Internal FR', '#008000', 'Internal FR Responded');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7044, 'WITHOUT_LOOP_CLIENT_CORRECTION_SUBMITTED', 2001, 'Submit Correction', '#008000', 'Client Correction Submitted');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7045, 'WITHOUT_LOOP_ACCOUNT_CC_POSITIVE', 2001, 'Account Payment Condition Positive', '#008000', 'Positive response from revenue');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7046, 'WITHOUT_LOOP_ACCOUNT_CC_NEGETIVE', 2001, 'Account Payment Condition Negetive', '#ff0000', 'Negetive response from revenue');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7047, 'WITHOUT_LOOP_DEMAND_NOTE_GENERATED', 2001, 'Generate Demand Note', '#f99a2f', 'Demand note generated');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7048, 'WITHOUT_LOOP_REQUESTED_TO_CENTRAL', 2001, 'Forward to CDGM(Broadbrand-1)', '#f99a2f', 'Application Forwarded by LDGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7049, 'WITHOUT_LOOP_REQUESTED_TO_LOCAL', 2001, 'Forward To Nearby Local DGM', '#f99a2f', 'Application Forwarded from Central DGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7050, 'WITHOUT_LOOP_IFR_2_REQUESTED', 2001, 'Request For Internal FR', '#f99a2f', 'Internal FR Requested');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7051, 'WITHOUT_LOOP_IFR_2_RESPONDED', 2001, 'Response IFR', '#008000', 'IFR Responded');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7052, 'WITHOUT_LOOP_IFR_2_INCOMPLETE', 2001, 'Give Negeive IFR to Central DGM', '#ff0000', 'Negetive IFR Report From Local DGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7053, 'WITHOUT_LOOP_RESPONDED_TO_CENTRAL', 2001, 'Forward To Central DGM', '#008000', 'Forwarded From Local DGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7054, 'WITHOUT_LOOP_MOVE_BACK_TO_ORIGINAL', 2001, 'Retransfer to original Local DGM', '#f99a2f', 'Applicatio Retransfarred');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7055, 'WITHOUT_LOOP_PAYMENT_DONE', 2001, 'give Payment', '#f99a2f', 'Payment Verification Needed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7056, 'WITHOUT_LOOP_PAYMENT_VERIFIED', 2001, 'Verify Payment', '#008000', 'Payment Verified');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7057, 'WITHOUT_LOOP_ADVICE_NOTE_PUBLISH', 2001, 'Publish Advice Note', '#f99a2f', 'Advice note published');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7058, 'WITHOUT_LOOP_TESTING_COMPLETE', 2001, 'Complete Testing', '#008000', 'Testing Completed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7059, 'WITHOUT_LOOP_FORWARD_TO_MUX_TEAM', 2001, 'forward to mux team', '#f99a2f', 'Application forwarded from server room');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7060, 'WITHOUT_LOOP_CONNCETION_COMPLETE', 2001, 'Complete Connection', '#f99a2f', 'Application Process Completed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7061, 'WITHOUT_LOOP_APPLICATION_REOPEN', 2001, 'Reopen Application', '#f99a2f', 'application reopened');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7062, 'WITHOUT_LOOP_MUX_DONE', 2001, 'Mux Configure Done', '#008000', 'Mux Configure Done');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7063, 'WITHOUT_LOOP_RESPONDED_TO_CENTRAL', 2001, 'Forward To Central DGM', '#f99a2f', 'Application Forwarded by LDGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7064, 'COMPLETE_TESTING_AND_CREATE_CONNECTION', 2001, 'Complete Application Process', '#008000', 'Application Process Completed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7065, 'DEMAND_NOTE_SKIP', 2001, 'Demand note generate and skip payment', '#f99a2f', 'Payment Skipped');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7066, 'WITHOUT_LOOP_DEMAND_NOTE_SKIP', 2001, 'Demand note generate and skip payment', '#f99a2f', 'Payment Skipped');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7067, 'FORWARD_LDGM_FOR_LOOP', 2001, 'Forward To LDGM to Complete Local Loop', '#f99a2f', 'Application Forwarded For Local Loop Completion');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7068, 'FORWARD_LDGM_EFR_REQUEST_FOR_LOOP', 2001, 'Request For External FR', '#f99a2f', 'External FR requested');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7069, 'FORWARD_LDGM_RESPONSE_EXTERNAL_FR', 2001, 'Response External FR', '#008000', 'External FR Responded');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7070, 'FORWARD_LDGM_SELECT_EXTERNAL_FR', 2001, 'Forward Back to CDGM', '#f99a2f', 'Application move back from Local DGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7071, 'SELECT_EXTERNAL_FR', 2001, 'Select External FR', '#f99a2f', 'External FR Selected');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7072, 'WORK_ORDER_APPROVE', 2001, 'Approve Work Done', '#008000', 'Work Done Approved');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7073, 'APPROVE_SR_COLLABORATION', 2001, 'Approve Collaboration With Vendor', '#f99a2f', 'Job completed and approved By server Room');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7074, 'REJECT_SR_COLLABORATION', 2001, 'Reject and  Send Back To Vendor', '#f99a2f', 'Rejected By server Room');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7075, 'REJECT_LOOP_DISTANCE', 2001, 'Reject and  Send Back To Vendor', '#f99a2f', 'Rejected Loop Distance by CDGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7076, 'APPLY_LOOP_DISTANCE_APPROVAL', 2001, 'Apply for loop distance approval', '#f99a2f', 'Submitted for Loop Distance Approval');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7077, 'APPROVE_FORWARDED_SR_COLLABORATION', 2001, 'Approve Collaboration With Vendor', '#f99a2f', 'Job completed and approved By server Room');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7078, 'REJECT_FORWARDED_SR_COLLABORATION', 2001, 'Reject and  Send Back To Vendor', '#f99a2f', 'Rejected By server Room');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7079, 'LDGM_REJECT_LOOP_DISTANCE', 2001, 'Reject and  Send Back To Vendor', '#f99a2f', 'Rejected Loop Distance by CDGM');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7080, 'FORWARD_APPLY_LOOP_DISTANCE_APPROVAL', 2001, 'Apply for loop distance approval', '#f99a2f', 'Submitted for Loop Distance Approval');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7500, 'CLIENT_TD_SUBMITTED', 4, 'Submitted For Temporary Disconnect', '#f99a2f', 'Temporary Disconnection Submitted');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7501, 'TD_AN', 4, 'Advice Note Generate', '#f99a2f', 'Advice Note Generated');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7502, 'TD_DONE', 4, 'Disconnect Client', '#f99a2f', 'Client Disconnected');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7503, 'CLIENT_RECONNECTION_SUBMITTED', 5, 'Submitted for Reconnection', '#f99a2f', 'Submitted for Reconnection');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7504, 'GENERATE_DEMAND_NOTE', 5, 'Generate Demand Note', '#f99a2f', 'Demand Note Generated');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7505, 'GIVE_PAYMENT', 5, 'Give Payment', '#f99a2f', 'Payment Verification Needed');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7506, 'VERIFY_PAYMENT', 5, 'Verify Payment', '#008000', 'Payment Verified');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7507, 'GENERATE_AN', 5, 'Generate Advice Note', '#f99a2f', 'Advice Note Generated');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7508, 'COMPLETE_RECONNECTION', 5, 'Reconnect Client', '#008000', 'Client Reconnected');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7509, 'TD_REJECTED', 4, 'Reject Application', '#f99a2f', 'Application Rejected');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7510, 'TD_REJECTED', 4, 'Reject Application', '#f99a2f', 'Application Rejected');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7601, 'SUBMITTED', 2002, 'Submit Application', '#f99a2f', 'Application Submitted');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7602, 'REQUEST_CLIENT_FOR_CORRECTION', 2002, 'Request to client for correction', '#f99a2f', 'Requested correction for submitted app');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7603, 'REQUEST_FOR_ACCOUNT_CC', 2002, 'Request Account For CC', '#f99a2f', 'Requested for Account CC');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7604, 'REJECTED', 2002, 'Reject Application', '#ff0000', 'Application Rejected');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7605, 'ADVICE_NOTE_PUBLISH', 2002, 'Publish Advice Note', '#f99a2f', 'Advice note published');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7606, 'CLIENT_CORRECTION_SUBMITTED', 2002, 'Submit Correction', '#008000', 'Client Correction Submitted');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7607, 'ACCOUNT_CC_POSITIVE', 2002, 'Account Payment Condition Positive', '#008000', 'Positive response from revenue');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7608, 'ACCOUNT_CC_NEGATIVE', 2002, 'Account Payment Condition Negative', '#ff0000', ' Negative Response From Revenue');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7609, 'APPLICATION_REOPEN', 2002, 'Reopen Application', '#f99a2f', 'Application Reopened');
INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7610, 'TESTING_DONE', 2002, 'Complete Setup', '#008000', 'Setup Completed');

INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7001, 7001, 7022, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7002, 7001, 7021, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7003, 7001, 7003, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7004, 7001, 7017, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7005, 7022, 7002, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7006, 7002, 7021, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7007, 7002, 7003, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7008, 7002, 7017, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7009, 7021, 7023, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7010, 7021, 7024, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7011, 7023, 7003, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7012, 7023, 7017, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7013, 7024, 7021, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7014, 7024, 7003, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7015, 7024, 7017, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7016, 7003, 7028, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7017, 7028, 7022, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7018, 7028, 7021, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7019, 7028, 7017, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7020, 7017, 7018, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7021, 7018, 7019, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7022, 7019, 7010, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7024, 7071, 7020, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7025, 7071, 7003, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7026, 7071, 7065, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7027, 7020, 7012, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7028, 7012, 7013, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7029, 7065, 7007, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7030, 7065, 7009, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7031, 7018, 7067, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7032, 7067, 7068, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7033, 7068, 7069, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7034, 7069, 7070, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7035, 7070, 7020, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7036, 7070, 7065, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7037, 7070, 7003, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7038, 7013, 7007, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7039, 7013, 7009, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7040, 7007, 7008, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7041, 7009, 7025, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7042, 7025, 7014, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7043, 7014, 7077, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7044, 7008, 7073, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7045, 7005, 7016, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7046, 7016, 7026, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7047, 7038, 7039, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7048, 7038, 7040, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7049, 7038, 7041, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7050, 7038, 7042, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7051, 7040, 7044, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7052, 7044, 7039, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7053, 7044, 7041, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7054, 7044, 7042, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7055, 7041, 7061, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7056, 7061, 7039, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7057, 7061, 7040, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7058, 7061, 7042, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7059, 7042, 7045, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7060, 7042, 7046, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7061, 7045, 7039, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7062, 7045, 7040, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7063, 7045, 7041, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7064, 7046, 7039, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7065, 7046, 7040, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7066, 7046, 7041, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7067, 7046, 7042, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7068, 7039, 7043, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7069, 7043, 7047, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7070, 7043, 7066, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7071, 7047, 7055, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7072, 7055, 7056, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7074, 7066, 7057, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7075, 7056, 7057, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7076, 7010, 7071, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7077, 7026, 7027, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7078, 7026, 7026, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7079, 7057, 7026, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7080, 7058, 7060, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7081, 7058, 7058, '');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7082, 7072, 7016, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7083, 7043, 7041, 'withOUT efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7084, 7073, 7072, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7085, 7008, 7074, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7086, 7074, 7008, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7087, 7073, 7075, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7088, 7075, 7076, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7089, 7076, 7075, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7090, 7076, 7072, 'with efr and work order flow ');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7091, 7014, 7078, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7092, 7078, 7014, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7093, 7077, 7072, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7094, 7077, 7079, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7095, 7079, 7080, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7096, 7080, 7072, null);
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7500, 7500, 7501, 'NULL');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7501, 7501, 7502, 'NULL');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7502, 7500, 7509, 'NULL');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7503, 7503, 7504, 'NULL');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7504, 7504, 7505, 'NULL');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7505, 7505, 7506, 'NULL');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7506, 7506, 7507, 'NULL');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7507, 7507, 7508, 'NULL');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7508, 7503, 7510, 'NULL');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7601, 7601, 7602, 'downgrade no policy flow: Submit=>Client Correction');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7602, 7601, 7603, 'downgrade no policy flow: Submit=>Account CC');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7603, 7601, 7604, 'downgrade no policy flow: Submit=>Reject');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7604, 7601, 7605, 'downgrade no policy flow: Submit=>AN Publish');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7605, 7602, 7606, 'downgrade no policy flow: Client Correction=>Correction Submit');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7606, 7603, 7607, 'downgrade no policy flow: Account CC=> Positive CC');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7607, 7603, 7608, 'downgrade no policy flow: Account CC=>Negative CC');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7608, 7604, 7609, 'downgrade no policy flow: Reject=>Reopen');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7609, 7605, 7610, 'downgrade no policy flow: AN Publish=>Testing Done');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7610, 7606, 7604, 'downgrade no policy flow: Correction Submit=>Reject');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7611, 7606, 7605, 'downgrade no policy flow: Correction Submit=>AN Publish');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7612, 7607, 7604, 'downgrade no policy flow: Positive CC=>Reject');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7613, 7607, 7605, 'downgrade no policy flow: Positive CC=>AN Publish');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7614, 7608, 7603, 'downgrade no policy flow: Negative CC=>Account CC');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7615, 7608, 7604, 'downgrade no policy flow: Negative CC=>Reject');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7616, 7608, 7605, 'downgrade no policy flow: Negative CC=>AN Publish');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7617, 7609, 7602, 'downgrade no policy flow: Reopen=>Client Correction');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7618, 7609, 7603, 'downgrade no policy flow: Reopen=>Account CC');
INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7619, null, null, null);

INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7001, 7001, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7002, 7002, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7003, 7003, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7004, 7004, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7005, 7005, -1);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7006, 7006, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7007, 7007, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7008, 7008, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7009, 7009, 6020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7010, 7010, 6020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7011, 7011, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7012, 7012, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7013, 7013, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7014, 7014, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7015, 7015, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7016, 7016, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7017, 7017, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7018, 7018, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7019, 7019, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7020, 7020, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7021, 7021, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7022, 7022, 16020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7024, 7024, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7025, 7025, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7026, 7026, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7027, 7027, -1);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7028, 7028, 6020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7029, 7029, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7030, 7030, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7031, 7031, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7032, 7032, 22020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7033, 7033, 16020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7034, 7034, 22020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7035, 7035, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7036, 7036, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7037, 7037, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7038, 7038, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7039, 7039, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7040, 7040, 16020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7041, 7041, 22020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7042, 7042, 16020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7043, 7043, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7044, 7044, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7045, 7045, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7046, 7046, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7047, 7047, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7048, 7048, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7049, 7049, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7050, 7050, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7051, 7051, -1);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7052, 7052, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7053, 7053, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7054, 7054, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7055, 7055, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7056, 7056, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7057, 7057, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7058, 7058, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7059, 7059, 6020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7060, 7060, 6020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7061, 7061, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7062, 7062, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7063, 7063, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7064, 7064, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7065, 7065, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7066, 7066, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7067, 7067, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7068, 7068, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7069, 7069, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7070, 7070, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7071, 7071, -1);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7072, 7072, 6020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7074, 7074, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7075, 7075, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7076, 7076, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7077, 7077, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7078, 7078, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7079, 7079, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7080, 7080, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7081, 7081, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7082, 7082, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7083, 7083, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7084, 7084, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7085, 7085, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7086, 7086, 16020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7087, 7087, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7088, 7088, 16020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7089, 7089, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7090, 7090, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7091, 7091, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7092, 7092, 16020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7093, 7093, 22020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7094, 7094, 22020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7095, 7095, 16020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7096, 7096, 22020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7500, 7500, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7501, 7501, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7502, 7502, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7503, 7503, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7504, 7504, -1);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7505, 7505, 6020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7506, 7506, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7507, 7507, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7508, 7508, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7601, 7601, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7602, 7602, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7603, 7603, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7604, 7604, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7605, 7605, -1);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7606, 7606, 6020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7607, 7607, 6020);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7608, 7608, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7609, 7609, 16021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7610, 7610, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7611, 7611, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7612, 7612, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7613, 7613, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7614, 7614, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7615, 7615, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7616, 7616, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7617, 7617, 22021);
INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7618, 7618, 22021);



-- Raihan Vai space

-- 11/2/2019


INSERT INTO at_universal_table(`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'registrationChargePerLink', '10000');
-- 13/2/2019
INSERT INTO at_universal_table(`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'vatPercentage', '15');

-- 17/02/2019

-- 02/03/2019
ALTER TABLE `vpn_application_link`
ADD COLUMN `advice_note_id` bigint(20) NULL DEFAULT NULL AFTER `vpn_application_id`;
-- 03/03/2019
ALTER TABLE `efr`
ADD COLUMN `wo_number` bigint(20) NULL DEFAULT NULL AFTER `is_work_ordered`;
-- 05/03/2019
ALTER TABLE `vpn_dn`
ADD COLUMN `otc_local_loop_BTCL` double(20, 0) NOT NULL DEFAULT 0 AFTER `application_group`;


alter table efr
  add contact varchar(25) null
;

alter table efr
  add is_collaborated_server_room bit default b'0' null
;

alter table efr
  add is_collaboration_approved_server_room bit default b'0' null
;

-- 06/03/2019
-- Downgrade Flow
INSERT INTO `flow`(`id`, `name`, `component`) VALUES (2002, 'Downgrade Flow', 20);

-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7601, 'SUBMITTED', 2002, 'Submit Application', '#f99a2f', 'Application Submitted');
-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7602, 'REQUEST_CLIENT_FOR_CORRECTION', 2002, 'Request to client for correction', '#f99a2f', 'Requested correction for submitted app');
-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7603, 'REQUEST_FOR_ACCOUNT_CC', 2002, 'Request Account For CC', '#f99a2f', 'Requested for Account CC');
-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7604, 'REJECTED', 2002, 'Reject Application', '#ff0000', 'Application Rejected');
-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7605, 'ADVICE_NOTE_PUBLISH', 2002, 'Publish Advice Note', '#f99a2f', 'Advice note published');
-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7606, 'CLIENT_CORRECTION_SUBMITTED', 2002, 'Submit Correction', '#008000', 'Client Correction Submitted');
-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7607, 'ACCOUNT_CC_POSITIVE', 2002, 'Account Payment Condition Positive', '#008000', 'Positive response from revenue');
-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7608, 'ACCOUNT_CC_NEGATIVE', 2002, 'Account Payment Condition Negative', '#ff0000', ' Negative Response From Revenue');
-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7609, 'APPLICATION_REOPEN', 2002, 'Reopen Application', '#f99a2f', 'Application Reopened');
-- INSERT INTO flow_state (id, name, flow, description, color, view_description) VALUES (7610, 'TESTING_DONE', 2002, 'Complete Setup', '#008000', 'Setup Completed');
--
--
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7601, 7601, 7602, 'downgrade no policy flow: Submit=>Client Correction');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7602, 7601, 7603, 'downgrade no policy flow: Submit=>Account CC');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7603, 7601, 7604, 'downgrade no policy flow: Submit=>Reject');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7604, 7601, 7605, 'downgrade no policy flow: Submit=>AN Publish');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7605, 7602, 7606, 'downgrade no policy flow: Client Correction=>Correction Submit');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7606, 7603, 7607, 'downgrade no policy flow: Account CC=> Positive CC');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7607, 7603, 7608, 'downgrade no policy flow: Account CC=>Negative CC');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7608, 7604, 7609, 'downgrade no policy flow: Reject=>Reopen');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7609, 7605, 7610, 'downgrade no policy flow: AN Publish=>Testing Done');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7610, 7606, 7604, 'downgrade no policy flow: Correction Submit=>Reject');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7611, 7606, 7605, 'downgrade no policy flow: Correction Submit=>AN Publish');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7612, 7607, 7604, 'downgrade no policy flow: Positive CC=>Reject');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7613, 7607, 7605, 'downgrade no policy flow: Positive CC=>AN Publish');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7614, 7608, 7603, 'downgrade no policy flow: Negative CC=>Account CC');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7615, 7608, 7604, 'downgrade no policy flow: Negative CC=>Reject');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7616, 7608, 7605, 'downgrade no policy flow: Negative CC=>AN Publish');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7617, 7609, 7602, 'downgrade no policy flow: Reopen=>Client Correction');
-- INSERT INTO flow_state_transition (id, source, destination, comment) VALUES (7618, 7609, 7603, 'downgrade no policy flow: Reopen=>Account CC');

-- CDGM ROLE 22021
-- Client ROLE -1
-- Server Room Role 16021
-- Revenue ROLE 6020
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7601, 7601, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7602, 7602, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7603, 7603, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7604, 7604, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7605, 7605, -1);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7606, 7606, 6020);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7607, 7607, 6020);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7608, 7608, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7609, 7609, 16021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7610, 7610, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7611, 7611, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7612, 7612, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7613, 7613, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7614, 7614, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7615, 7615, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7616, 7616, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7617, 7617, 22021);
-- INSERT INTO flow_state_transition_role (id, flow_state_transition, role) VALUES (7618, 7618, 22021);


-- 7/03/2019
ALTER TABLE `vpn_application_link`
MODIFY COLUMN `link_state` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL AFTER `remote_office_id`;

-- 10/03/2018
ALTER TABLE `vpn_dn`
MODIFY COLUMN `application_group` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL AFTER `advance`;

-- 11/03/2019
INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'reconnectChargePerLink', '3000');

INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'registrationChargeSpecialCaseLinkCount', '100');
INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'registrationChargePerLinkSpecialCase', '3000');
INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'bwShiftingOTC', '1000');
INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'addressShiftingOTC', '2000');
INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'localLoopShiftingOTC', '3000');
INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'registrationChargePerLinkSpecialCase', '3000');
-- 13/03/2019
INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('LLIOTC', 'premiumBWMargin_Mbps', '300');
INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('LLIOTC', 'tempConnectionMaximumLimit_Days', '30');
INSERT INTO `at_universal_table`(`tableName`, `columnName`, `value`) VALUES ('LLIOTC', 'tempConnectionMinimumLimit_Days', '15');

UPDATE `at_universal_table` SET `value` = '30000' WHERE `tableName` = 'VPNOTC' AND `columnName` = 'maximumOwnershipChangeCharge';
UPDATE `at_universal_table` SET `value` = '2000' WHERE `tableName` = 'VPNOTC' AND `columnName` = 'minimumOwnershipChangeCharge';



-- 1/04/2019
ALTER TABLE `vpn_application_link`
ADD COLUMN `demand_note_official_letter_id` bigint(20) NULL DEFAULT NULL AFTER `network_link_id`;














-- Touhid Space
-- 05 February, 2019

UPDATE `at_global_config` SET `gcValue`='1' WHERE  `gcID`=6;



INSERT INTO `at_reg_type_vs_module` (`id`, `moduleId`, `registrantTypeId`, `isDeleted`, `lastModificationTime`) VALUES
	(10000, 6, 1, 0, 1549341112363),
	(10001, 6, 2, 0, 1549341112519),
	(10002, 6, 3, 0, 1549341112574),
	(10003, 6, 4, 0, 1549341112634),
	(10004, 6, 5, 0, 1549341112700);


INSERT INTO `at_reg_category_vs_type` (`id`, `regTypeInAModuleId`, `registrantCategoryId`, `tariffCatId`, `isDeleted`, `lastModificationTime`) VALUES
	(19000, 10000, 1, 0, 0, 1549341304869),
	(19001, 10000, 17, 0, 0, 1549341304970),
	(19002, 10000, 2, 0, 0, 1549341305037),
	(19003, 10000, 18, 0, 0, 1549341305088),
	(19004, 10000, 5, 0, 0, 1549341305120),
	(19005, 10000, 6, 0, 0, 1549341305181),
	(19006, 10000, 8, 0, 0, 1549341305209),
	(20000, 10001, 5, 0, 0, 1549341409994),
	(20001, 10001, 6, 0, 0, 1549341410103),
	(20002, 10001, 8, 0, 0, 1549341410163),
	(21000, 10002, 16, 0, 0, 1549341632742),
	(21001, 10002, 1, 0, 0, 1549341632931),
	(21002, 10002, 17, 0, 0, 1549341632989),
	(21003, 10002, 2, 0, 0, 1549341633055),
	(21004, 10002, 18, 0, 0, 1549341633087),
	(21005, 10002, 5, 0, 0, 1549341633121),
	(21006, 10002, 8, 0, 0, 1549341633151),
	(21007, 10002, 12, 0, 0, 1549341633179),
	(21008, 10002, 13, 0, 0, 1549341633209),
	(21009, 10002, 14, 0, 0, 1549341633245),
	(21010, 10002, 15, 0, 0, 1549341633278),
	(22000, 10003, 9, 0, 0, 1549341684527),
	(22001, 10003, 10, 0, 0, 1549341684713),
	(22002, 10003, 11, 0, 0, 1549341684755),
	(23000, 10004, 7, 0, 0, 1549341747735);


INSERT INTO `at_req_doc_vs_category` (`id`, `regCategoryInATypeId`, `documentId`, `isMandatory`, `isDeleted`, `lastModificationTime`) VALUES
	(19000, 19000, 105, 1, 0, 1549342726260),
	(19001, 19000, 104, 1, 0, 1549342726354),
	(19002, 19000, 110, 1, 0, 1549342726394),
	(19003, 19000, 101, 1, 0, 1549342726471),
	(19004, 19000, 103, 1, 0, 1549342726512),
	(19005, 19000, 114, 1, 0, 1549342726538),
	(19006, 19000, 115, 1, 0, 1549342726617),
	(19007, 19000, 112, 1, 0, 1549342726708),
	(19008, 19000, 116, 0, 0, 1549342726743),
	(19009, 19000, 113, 0, 0, 1549342726760),
	(19010, 19002, 105, 1, 0, 1549342726793),
	(19011, 19002, 104, 1, 0, 1549342726823),
	(19012, 19002, 110, 1, 0, 1549342726843),
	(19013, 19002, 101, 1, 0, 1549342726868),
	(19014, 19002, 103, 1, 0, 1549342726894),
	(19015, 19002, 114, 1, 0, 1549342726921),
	(19016, 19002, 115, 1, 0, 1549342726944),
	(19017, 19002, 112, 1, 0, 1549342726969),
	(19018, 19002, 116, 0, 0, 1549342726994),
	(19019, 19002, 113, 0, 0, 1549342727020),
	(19020, 19001, 105, 1, 0, 1549342727044),
	(19021, 19001, 104, 1, 0, 1549342727070),
	(19022, 19001, 110, 1, 0, 1549342727093),
	(19023, 19001, 101, 1, 0, 1549342727134),
	(19024, 19001, 103, 1, 0, 1549342727152),
	(19025, 19001, 114, 1, 0, 1549342727177),
	(19026, 19001, 115, 1, 0, 1549342727201),
	(19027, 19001, 112, 1, 0, 1549342727229),
	(19028, 19001, 116, 0, 0, 1549342727293),
	(19029, 19001, 113, 0, 0, 1549342727321),
	(19030, 19003, 105, 1, 0, 1549342727344),
	(19031, 19003, 104, 1, 0, 1549342727371),
	(19032, 19003, 110, 1, 0, 1549342727394),
	(19033, 19003, 101, 1, 0, 1549342727640),
	(19034, 19003, 103, 1, 0, 1549342727687),
	(19035, 19003, 114, 1, 0, 1549342727709),
	(19036, 19003, 115, 1, 0, 1549342727736),
	(19037, 19003, 112, 1, 0, 1549342727759),
	(19038, 19003, 116, 0, 0, 1549342727789),
	(19039, 19003, 113, 0, 0, 1549342727809),
	(19040, 19004, 105, 1, 0, 1549342727835),
	(19041, 19005, 105, 1, 0, 1549342727860),
	(19042, 19006, 105, 1, 0, 1549342727886),
	(19043, 19006, 101, 0, 0, 1549342727959),
	(19044, 20000, 105, 1, 0, 1549342728009),
	(19045, 20001, 105, 1, 0, 1549342728085),
	(19046, 21000, 105, 1, 0, 1549342728111),
	(19047, 21000, 104, 1, 0, 1549342728135),
	(19048, 21000, 110, 1, 0, 1549342728159),
	(19049, 21000, 101, 1, 0, 1549342728184),
	(19050, 21000, 103, 1, 0, 1549342728209),
	(19051, 21000, 112, 1, 0, 1549342728234),
	(19052, 21000, 114, 1, 0, 1549342728260),
	(19053, 21000, 115, 1, 0, 1549342728285),
	(19054, 21000, 116, 0, 0, 1549342728344),
	(19055, 21001, 105, 1, 0, 1549342728394),
	(19056, 21001, 104, 1, 0, 1549342728551),
	(19057, 21001, 110, 1, 0, 1549342728743),
	(19058, 21001, 101, 1, 0, 1549342728793),
	(19059, 21001, 103, 1, 0, 1549342728851),
	(19060, 21001, 112, 1, 0, 1549342728909),
	(19061, 21001, 113, 1, 0, 1549342728935),
	(19062, 21001, 114, 1, 0, 1549342728960),
	(19063, 21001, 115, 1, 0, 1549342728985),
	(19064, 21001, 116, 0, 0, 1549342729010),
	(19065, 21003, 105, 1, 0, 1549342729034),
	(19066, 21003, 104, 1, 0, 1549342729093),
	(19067, 21003, 110, 1, 0, 1549342729118),
	(19068, 21003, 101, 1, 0, 1549342729142),
	(19069, 21003, 103, 1, 0, 1549342729168),
	(19070, 21003, 112, 1, 0, 1549342729234),
	(19071, 21003, 113, 1, 0, 1549342729259),
	(19072, 21003, 114, 1, 0, 1549342729327),
	(19073, 21003, 115, 1, 0, 1549342729368),
	(19074, 21003, 116, 0, 0, 1549342729392),
	(19075, 21002, 105, 1, 0, 1549342729493),
	(19076, 21002, 104, 1, 0, 1549342729543),
	(19077, 21002, 110, 1, 0, 1549342729568),
	(19078, 21002, 101, 1, 0, 1549342729593),
	(19079, 21002, 103, 1, 0, 1549342729619),
	(19080, 21002, 112, 1, 0, 1549342729643),
	(19081, 21002, 113, 1, 0, 1549342729668),
	(19082, 21002, 114, 1, 0, 1549342729692),
	(19083, 21002, 115, 1, 0, 1549342729719),
	(19084, 21002, 116, 0, 0, 1549342729743),
	(19085, 21004, 105, 1, 0, 1549342729793),
	(19086, 21004, 104, 1, 0, 1549342729810),
	(19087, 21004, 110, 1, 0, 1549342729835),
	(19088, 21004, 101, 1, 0, 1549342729859),
	(19089, 21004, 103, 1, 0, 1549342729884),
	(19090, 21004, 112, 1, 0, 1549342729909),
	(19091, 21004, 113, 1, 0, 1549342729934),
	(19092, 21004, 114, 1, 0, 1549342729984),
	(19093, 21004, 115, 1, 0, 1549342730002),
	(19094, 21004, 116, 0, 0, 1549342730051),
	(19095, 21005, 105, 1, 0, 1549342730068),
	(19096, 21005, 101, 0, 0, 1549342730092),
	(19097, 21006, 105, 1, 0, 1549342730118),
	(19098, 21006, 104, 1, 0, 1549342730143),
	(19099, 21006, 110, 1, 0, 1549342730168),
	(19100, 21006, 101, 1, 0, 1549342730193),
	(19101, 21006, 103, 1, 0, 1549342730218),
	(19102, 21006, 112, 1, 0, 1549342730243),
	(19103, 21006, 114, 1, 0, 1549342730268),
	(19104, 21006, 115, 1, 0, 1549342730318),
	(19105, 21006, 116, 0, 0, 1549342730342),
	(19106, 21007, 105, 1, 0, 1549342730368),
	(19107, 21007, 104, 1, 0, 1549342730393),
	(19108, 21007, 110, 1, 0, 1549342730472),
	(19109, 21007, 101, 1, 0, 1549342730702),
	(19110, 21007, 103, 1, 0, 1549342730750),
	(19111, 21007, 112, 1, 0, 1549342730809),
	(19112, 21007, 113, 1, 0, 1549342730825),
	(19113, 21007, 114, 1, 0, 1549342730851),
	(19114, 21007, 115, 1, 0, 1549342730885),
	(19115, 21007, 116, 0, 0, 1549342730917),
	(19116, 21008, 105, 1, 0, 1549342730951),
	(19117, 21008, 104, 1, 0, 1549342730976),
	(19118, 21008, 110, 1, 0, 1549342731001),
	(19119, 21008, 101, 1, 0, 1549342731026),
	(19120, 21008, 103, 1, 0, 1549342731050),
	(19121, 21008, 112, 1, 0, 1549342731076),
	(19122, 21008, 113, 0, 0, 1549342731100),
	(19123, 21008, 114, 1, 0, 1549342731126),
	(19124, 21008, 115, 1, 0, 1549342731151),
	(19125, 21008, 116, 0, 0, 1549342731177),
	(19126, 21009, 105, 1, 0, 1549342731201),
	(19127, 21009, 104, 1, 0, 1549342731227),
	(19128, 21009, 110, 1, 0, 1549342731251),
	(19129, 21009, 101, 1, 0, 1549342731276),
	(19130, 21009, 103, 1, 0, 1549342731301),
	(19131, 21009, 112, 1, 0, 1549342731326),
	(19132, 21009, 114, 1, 0, 1549342731365),
	(19133, 21009, 115, 1, 0, 1549342731411),
	(19134, 21009, 116, 0, 0, 1549342731542),
	(19135, 21010, 105, 1, 0, 1549342731751),
	(19136, 21010, 104, 1, 0, 1549342731833),
	(19137, 21010, 110, 1, 0, 1549342731850),
	(19138, 21010, 101, 1, 0, 1549342731910),
	(19139, 21010, 103, 1, 0, 1549342731976),
	(19140, 21010, 112, 1, 0, 1549342732009),
	(19141, 21010, 114, 1, 0, 1549342732043),
	(19142, 21010, 115, 1, 0, 1549342732109),
	(19143, 21010, 116, 0, 0, 1549342732134),
	(19144, 22000, 105, 1, 0, 1549342732160),
	(19145, 22000, 101, 0, 0, 1549342732217),
	(19146, 22001, 105, 1, 0, 1549342732267),
	(19147, 22001, 101, 0, 0, 1549342732317),
	(19148, 22002, 105, 1, 0, 1549342732334),
	(19149, 22002, 101, 0, 0, 1549342732359),
	(19150, 23000, 105, 1, 0, 1549342732385);


-- 24 Feb

ALTER TABLE `vpn_network_link`
	ADD COLUMN `clientId` BIGINT(20) NOT NULL AFTER `history_id`;

-- 4 March

alter table vpn_application_link modify link_name varchar(255) null;


alter table efr
  add wo_number bigint null
;


-- Jami
-- 3/5/19
ALTER TABLE application add second_client BIGINT DEFAULT 0 NULL after client_id;
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES (6056, 6008, 'Owner Change', 6);
-- 3/6/19
INSERT INTO `at_universal_table` (`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'minimumOwnershipChangeCharge', '3000');
INSERT INTO `at_universal_table` (`tableName`, `columnName`, `value`) VALUES ('VPNOTC', 'maximumOwnershipChangeCharge', '50000');

CREATE TABLE IF NOT EXISTS `vpn_on_process_link` (
		`id` BIGINT(20) NOT NULL,
		`application` BIGINT(20) NOT NULL,
		`link` BIGINT(20) NOT NULL,
		PRIMARY KEY (`id`) USING BTREE
)
	COLLATE='utf8_general_ci'
	ENGINE=InnoDB
;

INSERT IGNORE INTO `vbSequencer` (table_name, next_id, table_LastModificationTime) VALUES ('vpn_on_process_link', 1, 0);




-- 27 Feb

CREATE TABLE `vpn_monthly_bill_client` (
	`id` BIGINT NOT NULL,
	`clientId` BIGINT NOT NULL,
	`mbpsBreakDownContent` VARCHAR(255) NULL,
	`billingRangeBreakDownContent` VARCHAR(255) NULL,
	`createdDate` BIGINT NOT NULL,
	`month` INT NOT NULL,
	`year` INT NOT NULL,
	`isDeleted` TINYINT NULL DEFAULT '0',
	`grandTotal` DOUBLE NOT NULL DEFAULT '0',
	`discountRate` DOUBLE NOT NULL DEFAULT '0',
	`discount` DOUBLE NOT NULL DEFAULT '0',
	`totalPayable` DOUBLE NOT NULL DEFAULT '0',
	`vatRate` DOUBLE NOT NULL DEFAULT '0',
	`vat` DOUBLE NOT NULL DEFAULT '0',
	`netPayable` DOUBLE NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
;



CREATE TABLE `vpn_monthly_bill_link` (
	`id` BIGINT NOT NULL,
	`clientId` BIGINT NOT NULL,
	`monthlyBillByClientId` BIGINT NOT NULL,
	`linkId` BIGINT NOT NULL,
	`linkStatus` VARCHAR(20) NULL,
	`linkName` VARCHAR(50) NULL,
	`linkType` INT NULL DEFAULT '0',
	`linkAddress` VARCHAR(50) NULL,
	`linkBandwidth` DOUBLE NOT NULL,
	`mbpsRate` DOUBLE NULL,
	`mbpsCost` DOUBLE NULL,
	`localEndLoopCost` DOUBLE NULL,
	`remoteEndLoopCost` DOUBLE NULL,
	`createdDate` BIGINT NOT NULL,
	`grandCost` BIGINT NOT NULL DEFAULT '0',
	`discountRate` BIGINT NOT NULL DEFAULT '0',
	`discount` BIGINT NOT NULL DEFAULT '0',
	`vatRate` BIGINT NOT NULL DEFAULT '0',
	`vat` BIGINT NOT NULL DEFAULT '0',
	`totalCost` BIGINT NOT NULL DEFAULT '0',
	`localLoopBreakDownsContentLocalEnd` VARCHAR(255) NULL,
	`localLoopBreakDownsContentRemoteEnd` VARCHAR(255) NULL,
	`remark` VARCHAR(20) NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
;


CREATE TABLE `vpn_monthly_usage_client` (
	`id` BIGINT NOT NULL,
	`clientId` BIGINT NOT NULL,
	`mbpsBreakDownContent` VARCHAR(255) NULL,
	`billingRangeBreakDownContent` VARCHAR(255) NULL,
	`createdDate` BIGINT NOT NULL,
	`month` INT NOT NULL,
	`year` INT NOT NULL,
	`isDeleted` TINYINT NOT NULL DEFAULT '0',
	`grandTotal` DOUBLE NOT NULL DEFAULT '0',
	`discountRate` DOUBLE NOT NULL DEFAULT '0',
	`discount` DOUBLE NOT NULL DEFAULT '0',
	`totalPayable` DOUBLE NOT NULL DEFAULT '0',
	`vatRate` DOUBLE NOT NULL DEFAULT '0',
	`vat` DOUBLE NOT NULL DEFAULT '0',
	`netPayable` DOUBLE NOT NULL DEFAULT '0',
	`description` VARCHAR(50) NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
;

CREATE TABLE `vpn_monthly_usage_link` (
	`id` BIGINT NOT NULL,
	`clientId` BIGINT NOT NULL,
	`monthlyUsageByClientId` BIGINT NOT NULL,
	`linkId` BIGINT NOT NULL,
	`linkName` VARCHAR(50) NULL,
	`linkType` INT NULL,
	`linkAddress` VARCHAR(50) NULL,
	`mbpsCost` DOUBLE NOT NULL DEFAULT '0',
	`localEndLoopCost` DOUBLE NOT NULL DEFAULT '0',
	`remoteEndLoopCost` DOUBLE NOT NULL DEFAULT '0',
	`totalLoopCost` DOUBLE NOT NULL DEFAULT '0',
	`createdDate` BIGINT NOT NULL DEFAULT '0',
	`grandCost` DOUBLE NOT NULL DEFAULT '0',
	`discountRate` DOUBLE NOT NULL DEFAULT '0',
	`discount` DOUBLE NOT NULL DEFAULT '0',
	`vatRate` DOUBLE NOT NULL DEFAULT '0',
	`vat` DOUBLE NOT NULL DEFAULT '0',
	`totalCost` DOUBLE NOT NULL DEFAULT '0',
	`localLoopBreakDownsContentLocalEnd` VARCHAR(255) NULL,
	`localLoopBreakDownsContentRemoteEnd` VARCHAR(255) NULL,
	`linkBandwidthBreakDownsContent` VARCHAR(255) NULL,
	`remark` VARCHAR(50) NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
;


INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('vpn_monthly_bill_client', '50000');
INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('vpn_monthly_bill_link', '60000');
INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('vpn_monthly_usage_client', '70000');
INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('vpn_monthly_usage_link', '80000');




ALTER TABLE `vpn_monthly_bill_link`
	CHANGE COLUMN `mbpsRate` `mbpsRate` DOUBLE NULL DEFAULT '0' AFTER `linkBandwidth`,
	CHANGE COLUMN `mbpsCost` `mbpsCost` DOUBLE NULL DEFAULT '0' AFTER `mbpsRate`,
	CHANGE COLUMN `localEndLoopCost` `localEndLoopCost` DOUBLE NULL DEFAULT '0' AFTER `mbpsCost`,
	ADD COLUMN `totalLoopCost` DOUBLE NULL DEFAULT NULL AFTER `remoteEndLoopCost`;


ALTER TABLE `vpn_monthly_bill_link`
	CHANGE COLUMN `remoteEndLoopCost` `remoteEndLoopCost` DOUBLE NULL DEFAULT '0' AFTER `localEndLoopCost`,
	CHANGE COLUMN `totalLoopCost` `totalLoopCost` DOUBLE NULL DEFAULT '0' AFTER `remoteEndLoopCost`;


-- 04-03-19

CREATE TABLE `vpn_monthly_bill_summary_client` (
	`id` BIGINT NOT NULL,
	`clientId` BIGINT NOT NULL,
	`mbpsBreakDownContent` VARCHAR(50) NULL,
	`billingRangeBreakDownContent` VARCHAR(50) NULL,
	`createdDate` VARCHAR(50) NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
;


CREATE TABLE `vpn_monthly_bill_summary_item` (
	`id` BIGINT NOT NULL,
	`monthlyBillSummaryByClientId` BIGINT NOT NULL,
	`type` INT NULL,
	`grandCost` DOUBLE NOT NULL DEFAULT '0',
	`discount` DOUBLE NOT NULL DEFAULT '0',
	`totalCost` DOUBLE NOT NULL DEFAULT '0',
	`vatRate` DOUBLE NOT NULL DEFAULT '0',
	`vat` DOUBLE NOT NULL DEFAULT '0',
	`netCost` DOUBLE NOT NULL DEFAULT '0',
	`remark` VARCHAR(50) NULL DEFAULT '0',
	`createdDate` BIGINT NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
;

CREATE TABLE `vpn_dn_adjustment` (
	`id` BIGINT NOT NULL,
	`clientId` BIGINT NOT NULL,
	`billId` BIGINT NOT NULL,
	`linkType` VARCHAR(20) NULL,
	`createdDate` BIGINT NULL,
	`lastModifiedDate` BIGINT NOT NULL,
	`activeFrom` BIGINT NOT NULL,
	`bwCharge` DOUBLE NOT NULL DEFAULT '0',
	`bwDiscount` DOUBLE NOT NULL DEFAULT '0',
	`loopCharge` DOUBLE NOT NULL DEFAULT '0',
	`totalDue` DOUBLE NOT NULL DEFAULT '0',
	`vatRate` DOUBLE NOT NULL DEFAULT '0',
	`vat` DOUBLE NOT NULL DEFAULT '0',
	`status` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
;



INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('vpn_dn_adjustment', '1');
INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('vpn_monthly_bill_summary_item', '10000');
INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('vpn_monthly_bill_summary_client', '20000');



ALTER TABLE `vpn_monthly_bill_summary_client`
	ADD COLUMN `bill_id` BIGINT(20) NOT NULL AFTER `id`;


-- 06-03-19


CREATE TABLE `vpn_monthly_outsource_bill` (
	`id` BIGINT NOT NULL,
	`vendorId` BIGINT NOT NULL,
	`createdDate` BIGINT NULL,
	`lastModifiedDate` BIGINT NULL,
	`loopLengthSingle` DOUBLE NOT NULL DEFAULT '0.0',
	`loopLengthDouble` DOUBLE NOT NULL DEFAULT '0.0',
	`loopLength` DOUBLE NOT NULL DEFAULT '0.0',
	`totalDue` DOUBLE NOT NULL DEFAULT '0.0',
	`totalPayable` DOUBLE NOT NULL DEFAULT '0.0',
	`year` INT NOT NULL DEFAULT '0.0',
	`month` INT NOT NULL DEFAULT '0.0',
	`status` VARCHAR(50) NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
;


CREATE TABLE `vpn_monthly_outsource_bill_link` (
	`id` BIGINT NOT NULL,
	`outsourceBillId` BIGINT NOT NULL,
	`linkId` BIGINT NOT NULL,
	`loopLengthSingle` DOUBLE NOT NULL DEFAULT '0.0',
	`loopLengthDouble` DOUBLE NOT NULL DEFAULT '0.0',
	`btclLength` DOUBLE NOT NULL DEFAULT '0.0',
	`vendorLength` DOUBLE NOT NULL DEFAULT '0.0',
	`totalDue` DOUBLE NOT NULL DEFAULT '0.0',
	`totalPayable` DOUBLE NOT NULL DEFAULT '0.0',
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci';


INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('vpn_monthly_outsource_bill', '1000');
INSERT INTO `vbSequencer` (`table_name`, `next_id`) VALUES ('vpn_monthly_outsource_bill_link', '10000');


-- Monthly Bill Menu VPN

INSERT INTO admenu (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6100', '6001', 'Monthly Bill', '6');
INSERT INTO admenu (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6101', '6100', 'Search', '6');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6105', '6001', 'Monthly Usage', '6');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6106', '6105', 'Search', '6');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6110', '6001', 'Monthly Bill Summary', '6');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6111', '6110', 'Check', '6');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6113', '6110', 'Generate', '6');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6112', '6110', 'Search', '6');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6120', '6001', 'Outsource Bill', '6');
INSERT INTO `admenu` (`mnMenuID`, `mnParentMenuID`, `mnMenuName`, `mnModuleTypeID`) VALUES ('6121', '6120', 'Search', '6');

-- Common Comment Module
create table comment
(
	id int null,
	moduleId int null,
	userId long null,
	stateId int null,
	applicationId long null,
	entityId long null,
	sequenceId long null,
	comment text null,
	submissionDate long null,
	constraint comment_pk
		primary key (id)
);


INSERT INTO `vbSequencer` (`table_name`, `next_id`, `table_LastModificationTime`) VALUES ('comment', 1, 1551950044561);

alter table comment drop column applicationId;



ALTER TABLE `vpn_network_link`
	ADD COLUMN `link_type` VARCHAR(45) NULL AFTER `link_status`,
	ADD COLUMN `link_distance` BIGINT NOT NULL DEFAULT '0' AFTER `link_type`;


ALTER TABLE `vpn_monthly_bill_link`
	ADD COLUMN `link_distance` BIGINT NOT NULL DEFAULT '0' AFTER `linkType`;


ALTER TABLE `vpn_monthly_usage_link`
	ADD COLUMN `link_distance` BIGINT NOT NULL DEFAULT '0' AFTER `linkType`;
alter table local_loop add vlan_type int null;



-- 7-7-2019

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (7023, 7018, 7003, null);

-- 10-7-19

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (7023, 7023, 22021);



INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`) VALUES (7098, 7080, 7079, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`) VALUES (7098, 7098, 22020);

-- 13-7-18

DELETE FROM flow_state_transition_role WHERE `id` = 7016;

DELETE FROM flow_state_transition WHERE `id` = 7016;


DELETE FROM flow_state_transition_role WHERE `id` = 7055;


DELETE FROM flow_state_transition WHERE `id` = 7055;



