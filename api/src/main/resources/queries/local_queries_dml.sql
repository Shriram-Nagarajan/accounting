INSERT INTO `accounts`.`categories` (`category_name`,`is_default`) VALUES ('Service',true);
INSERT INTO `accounts`.`categories` (`category_name`,`is_default`) VALUES ('Medical',true);
INSERT INTO `accounts`.`categories` (`category_name`,`is_default`) VALUES ('EMI',true);
INSERT INTO `accounts`.`categories` (`category_name`,`is_default`) VALUES ('Food order',true);
INSERT INTO `accounts`.`categories` (`category_name`,`is_default`) VALUES ('Grocery',true);
INSERT INTO `accounts`.`categories` (`category_name`,`is_default`) VALUES ('Misc',true);
INSERT INTO `accounts`.`categories` (`category_name`,`is_default`) VALUES ('Utility bills',true);
INSERT INTO `accounts`.`categories` (`category_name`,`is_default`) VALUES ('Deposits',true);
INSERT INTO `accounts`.`categories` (`category_name`,`is_default`) VALUES ('Travel',true);

INSERT INTO `accounts`.`account_details` (`account_number`, `name`) VALUES ('4895', 'test');

INSERT INTO `accounts`.`user_account_mapping` (`user_id`, `account_id`) VALUES ('1', '1');
INSERT INTO `accounts`.`user_account_mapping` (`user_id`, `account_id`) VALUES ('2', '1');

