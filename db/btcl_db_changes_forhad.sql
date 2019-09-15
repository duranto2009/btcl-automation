CREATE TABLE at_lli_bandwidth_configuration
(
    id int PRIMARY KEY AUTO_INCREMENT,
    bandwidth double,
    duration long
);
-- dummy_data
INSERT INTO at_lli_bandwidth_configuration (id, bandwidth, duration) VALUES (1, 500, '864000000');
INSERT INTO at_lli_bandwidth_configuration (id, bandwidth, duration) VALUES (2, 1000, '1728000000');
INSERT INTO at_lli_bandwidth_configuration (id, bandwidth, duration) VALUES (3, 1500, '2592000000');
INSERT INTO at_lli_bandwidth_configuration (id, bandwidth, duration) VALUES (4, 2000, '864000000');
INSERT INTO at_lli_bandwidth_configuration (id, bandwidth, duration) VALUES (5, 2500, '1728000000');
INSERT INTO at_lli_bandwidth_configuration (id, bandwidth, duration) VALUES (6, 3000, '2592000000');


-- 20/12/2018
INSERT INTO btcl_2018_25_nov.at_state (stID, stName, stDurationInMillis, stRootRequestTypeID, stLastModificationTime, stActivationStatus, stIsVisibleToSystem, stIsDeleted) VALUES (45003, 'Application Verified', 604800000, 45001, 0, 2, 1, 0);

INSERT INTO btcl_2018_25_nov.at_state (stID, stName, stDurationInMillis, stRootRequestTypeID, stLastModificationTime, stActivationStatus, stIsVisibleToSystem, stIsDeleted) VALUES (45004, 'Application Correction Requested', 604800000, 45001, 0, 2, 1, 0);

INSERT INTO btcl_2018_25_nov.at_state (stID, stName, stDurationInMillis, stRootRequestTypeID, stLastModificationTime, stActivationStatus, stIsVisibleToSystem, stIsDeleted) VALUES (-45003, 'Application Rejected', 604800000, 45001, 0, 0, 1, 0);


INSERT INTO btcl_2018_25_nov.at_state (stID, stName, stDurationInMillis, stRootRequestTypeID, stLastModificationTime, stActivationStatus, stIsVisibleToSystem, stIsDeleted) VALUES (-45002, 'Application Cancelled', 604800000, 45001, 0, 0, 1, 0);

-- 21 - 01 - 2019
-- colocation cost config in server

UPDATE `colocation_cost_config` t SET t.`quantity_id` = 1 WHERE t.`id` = 108
UPDATE `colocation_cost_config` t SET t.`quantity_id` = 1 WHERE t.`id` = 109
UPDATE `colocation_cost_config` t SET t.`quantity_id` = 1 WHERE t.`id` = 110
UPDATE `colocation_cost_config` t SET t.`quantity_id` = 1 WHERE t.`id` = 104