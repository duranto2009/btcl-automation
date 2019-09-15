-- 20 jan 2019
drop table if exists nix_monthly_bill_connection;
create table nix_monthly_bill_connection
(
	id bigint not null
		primary key,
	clientId bigint not null,
	connectionId bigint not null,
	monthlyBillByClientId bigint not null,
	type int(8) not null,
	status int(8) not null,
	name varchar(255) null,
	remark varchar(255) null,
	portCount int default '0' null,
	portType int default '0' null,
	portRate double default '0' null,
	portCost double default '0' null,
	loopRate double default '0' null,
	totalCost double default '0' null,
	createdDate bigint null,
	grandCost double default '0' null,
	discountRate double default '0' null,
	discount double default '0' null,
	vatRate double default '0' null,
	vat double default '0' null,
	localLoopBreakDownsContent varchar(1024) null,
	loopCost double default '0' null
)
;


drop table if exists nix_monthly_bill_client;
create table nix_monthly_bill_client
(
  id bigint not null
    primary key,
  clientId bigint not null,
  createdDate bigint null,
  month int not null,
  year int not null,
  isDeleted int(2) default '0' null,
  grandTotal double default '0' null,
  discountRate double default '0' null,
  discount double default '0' null,
  totalPayable double default '0' null,
  vatRate double default '0' null,
  vat double default '0' null,
  netPayable double default '0' null
);
drop table if exists nix_monthly_bill_summary_item;
create table nix_monthly_bill_summary_item
(
  id bigint not null
    primary key,
  monthlyBillSummaryByClientId bigint not null,
  type int not null,
  createdDate bigint null,
  grandCost double default '0' null,
  discount double default '0' null,
  totalCost double default '0' null,
  vatRate double default '0' null,
  vat double default '0' null,
  netCost double default '0' null,
  remark varchar(255) null
);
drop table if exists nix_monthly_bill_summary_client;
create table nix_monthly_bill_summary_client
(
	bill_id bigint not null,
	id bigint not null
		primary key,
	clientId bigint not null,
	createdDate bigint null
);


drop table if exists nix_monthly_usage_connection;
create table nix_monthly_usage_connection
(
	id bigint not null
		primary key,
	clientId bigint not null,
	connectionId bigint not null,
	monthlyUsageByClientId bigint not null,
	type int(8) not null,
	name varchar(255) null,
	address varchar(255) null,
	localLoopBreakDownsContent varchar(1024) null,
	feesContent varchar(255) null,
	remark varchar(255) null,
	portCost double default '0' null,
	loopCost double default '0' null,
	totalCost double default '0' null,
	createdDate bigint null,
	grandCost double default '0' null,
	discountRate double default '0' null,
	discount double default '0' null,
	vatRate double default '0' null,
	vat double default '0' null,
	portBreakDownsContent varchar(1024) null
)
;


drop table if exists nix_monthly_usage_client;
create table nix_monthly_usage_client
(
	id bigint not null
		primary key,
	clientId bigint not null,
	createdDate bigint null,
	month int null,
	year int null,
	grandTotal double default '0' null comment 'grand total',
	lateFee double default '0' null,
	totalPayable double default '0' null,
	discountPercentage double default '0' null,
	discount double default '0' null,
	vatRate double default '0' null,
	vat double default '0' null,
	netPayable double default '0' null,
	description varchar(255) null,
	isDeleted int(2) null
)
;

INSERT INTO vbSequencer (table_name, next_id, table_LastModificationTime)
VALUES ('nix_monthly_bill_client', 6010, 1548134124582);
INSERT INTO vbSequencer (table_name, next_id, table_LastModificationTime)
VALUES ('nix_monthly_bill_connection', 6010, 1548134124582);
INSERT INTO vbSequencer (table_name, next_id, table_LastModificationTime)
VALUES ('nix_monthly_usage_client', 1, 0);
INSERT INTO vbSequencer (table_name, next_id, table_LastModificationTime)
VALUES ('nix_monthly_usage_connection', 1, 0);
INSERT INTO vbSequencer (table_name, next_id, table_LastModificationTime)
VALUES ('nix_monthly_bill_summary_client', 1, 0);
INSERT INTO vbSequencer (table_name, next_id, table_LastModificationTime)
VALUES ('nix_monthly_bill_summary_item', 1, 0);


create table if not exists nix_monthly_outsource_bill
(
	id bigint not null
		primary key,
	vendorId bigint not null,
	loopLengthSingle double default '0' null,
	loopLengthDouble double default '0' null,
	loopLength double default '0' null,
	totalDue double default '0' null,
	totalPayable double default '0' null,
	month int(8) not null,
	year int(8) not null,
	status varchar(30) not null,
	createdDate bigint null,
	lastModifiedDate bigint null
)
;


create table if not exists nix_monthly_outsource_bill_connection
(
	id bigint not null
		primary key,
	outsourceBillId bigint not null,
	connectionId bigint not null,
	loopLengthSingle double default '0' null,
	loopLengthDouble double default '0' null,
	btclLength double default '0' null,
	vendorLength double default '0' null,
	totalDue double default '0' null,
	totalPayable double default '0' null
);

INSERT INTO vbsequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('nix_monthly_outsource_bill_connection', 1, DEFAULT);
INSERT INTO vbsequencer (`table_name`, `next_id`, `table_LastModificationTime`)
VALUES ('nix_monthly_outsource_bill', 1, DEFAULT);


-- 7-17-19

INSERT INTO flow_state_transition (`id`, `source`, `destination`, `comment`)
VALUES (5058, 5030, 5029, null);

INSERT INTO flow_state_transition_role (`id`, `flow_state_transition`, `role`)
VALUES (5058, 5058, 22020);








