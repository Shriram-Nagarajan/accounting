CREATE SCHEMA if not exists `authdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;

DROP table if exists `authdb`.`properties`;
CREATE TABLE `authdb`.`properties` (
    prop_key VARCHAR(50) PRIMARY KEY,
    prop_value VARCHAR(200)
)  ENGINE=INNODB DEFAULT CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_UNICODE_CI;


create schema if not exists userdb charset utf8mb4 collate utf8mb4_unicode_ci;
DROP table if exists userdb.users;
CREATE TABLE userdb.users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    login_id VARCHAR(100) UNIQUE KEY,
    `name` VARCHAR(100),
    `password` VARCHAR(100),
    email_id VARCHAR(400)
)  CHARSET=UTF8MB4 , COLLATE = UTF8MB4_UNICODE_CI;

drop table if exists `userdb`.`forgot_password_token`;
CREATE TABLE `userdb`.`forgot_password_token` (
  `token_id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `authentication_id` varchar(350) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `authentication_type` enum('email','mobile') COLLATE utf8mb4_unicode_ci NOT NULL,
  `token_status` enum('sent','used','expired') COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `expiry_timestamp` timestamp NOT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

drop table if exists `userdb`.`registration_token`;
CREATE TABLE `userdb`.`registration_token` (
  `token_id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `authentication_id` varchar(350) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `authentication_type` enum('email','mobile') COLLATE utf8mb4_unicode_ci NOT NULL,
  `token_status` enum('sent','verified','expired','registered') COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `expiry_timestamp` timestamp NOT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
