ALTER TABLE at_lli_fixed_cost_config ADD COLUMN fixed_ip_count INT DEFAULT 0;
UPDATE at_lli_fixed_cost_config SET fixed_ip_count = 4;

ALTER TABLE at_lli_fixed_cost_config ADD COLUMN fixed_ip_charge DOUBLE DEFAULT 0.0;
UPDATE at_lli_fixed_cost_config SET fixed_ip_charge = 800.0;

ALTER TABLE at_lli_fixed_cost_config ADD COLUMN variable_ip_charge DOUBLE DEFAULT 0.0;
UPDATE at_lli_fixed_cost_config SET variable_ip_charge = 200.0;
