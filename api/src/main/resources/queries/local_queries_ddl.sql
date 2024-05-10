create schema if not exists users charset utf8mb4 collate utf8mb4_unicode_ci;
DROP table if exists users.app_users;
CREATE TABLE users.app_users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(100) UNIQUE KEY,
    `password` VARCHAR(100),
    email_id VARCHAR(400)
)  CHARSET=UTF8MB4 , COLLATE = UTF8MB4_UNICODE_CI;