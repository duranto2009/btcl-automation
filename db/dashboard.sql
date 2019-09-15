-- 12/03/2019
CREATE TABLE `dashboard_template` (
  `id` int(11) NOT NULL,
  `template_name` varchar(50) NOT NULL,
  `template_description` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `dashboard_template_role` (
  `id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `dashboard_template_id` int(11) NOT NULL,
  `is_active` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `dashboard_visualization_unit` (
  `id` int(11) NOT NULL,
  `visualization_name` varchar(50) NOT NULL,
  `file_path` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `dashboard_template_visualization` (
  `id` int(11) NOT NULL,
  `template_id` int(11) NOT NULL,
  `visualization_id` int(11) NOT NULL,
  `row_start` int(11) NOT NULL,
  `row_end` int(11) NOT NULL,
  `col_start` int(11) NOT NULL,
  `col_end` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `dashboard_template`(`id`, `template_name`, `dashboard_template_description`) VALUES (1, 'Admin Template', 'For Admin (LLI/VPN/CoLocation/NIX)');
INSERT INTO `dashboard_template`(`id`, `template_name`, `dashboard_template_description`) VALUES (2, 'Client Template', 'For Client');
INSERT INTO `role_dashboard_template`(`id`, `role_id`, `dashboard_template_id`) VALUES (1, 22021, 1);
INSERT INTO `role_dashboard_template`(`id`, `role_id`, `dashboard_template_id`) VALUES (2, 1, 1);

INSERT INTO `vbSequencer`(`table_name`, `next_id`) VALUES ('dashboard_template_visualization', 1);
INSERT INTO `vbSequencer`(`table_name`, `next_id`) VALUES ('dashboard_visualization_unit', 1);