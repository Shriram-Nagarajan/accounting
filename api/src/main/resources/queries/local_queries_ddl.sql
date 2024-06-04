create schema if not exists users charset utf8mb4 collate utf8mb4_unicode_ci;
DROP table if exists users.app_users;
CREATE TABLE users.app_users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(100) UNIQUE KEY,
    `password` VARCHAR(100),
    email_id VARCHAR(400)
)  CHARSET=UTF8MB4 , COLLATE = UTF8MB4_UNICODE_CI;


create schema if not exists accounts charset utf8mb4 collate utf8mb4_unicode_ci;
drop table if exists accounts.`account_details`;
CREATE TABLE IF NOT EXISTS accounts.`account_details` (
    `account_id` BIGINT primary key auto_increment,
    `account_number` VARCHAR(50) NOT NULL,
    `name` varchar(100) NOT NULL,
    INDEX `idx_account_number` (`account_number`)
);

drop table if exists accounts.`transactions`;
CREATE TABLE IF NOT EXISTS accounts.`transactions` (
    `transaction_id` BIGINT NOT NULL AUTO_INCREMENT,
    `transaction_ref_num` VARCHAR(200),
    `account_id` bigint DEFAULT NULL,
    `transaction_date` DATE,
    `description` VARCHAR(300) DEFAULT NULL,
    `credit_txn` BOOL NOT NULL,
    `amount` DOUBLE NOT NULL,
    `reversal_txn` BOOL NOT NULL,
    PRIMARY KEY (`transaction_id`),
    INDEX `idx_transaction_ref_num` (`transaction_ref_num`),
    INDEX `idx_transaction_date` (`transaction_date`)
	CONSTRAINT `fk_account_id` FOREIGN KEY (`account_id`) REFERENCES `account_details` (`account_id`)
)  DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_UNICODE_CI;

DROP table if exists accounts.`categories`;
CREATE TABLE IF NOT EXISTS accounts.`categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) unique NOT NULL,
  PRIMARY KEY (`category_id`),
  KEY `idx_category_name` (`category_name`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


drop table if exists accounts.`expense_details`;
CREATE TABLE IF NOT EXISTS accounts.`expense_details` (
    `transaction_id` BIGINT NOT NULL,
    `category_id` INT,
    PRIMARY KEY (`transaction_id`),
    CONSTRAINT `transaction_id` FOREIGN KEY (`transaction_id`)
        REFERENCES transactions (transaction_id) ON DELETE CASCADE
) DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_UNICODE_CI;

drop table if exists accounts.SPRING_SESSION;
CREATE TABLE if not exists accounts.SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    PRIMARY KEY (PRIMARY_ID)
);

drop table if exists accounts.SPRING_SESSION_ATTRIBUTES;
CREATE TABLE if not exists accounts.SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BLOB NOT NULL,
    PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);