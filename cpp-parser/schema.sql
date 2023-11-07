-- Schema for news parser
DROP TABLE IF EXISTS `sources`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `publications`;
DROP TABLE IF EXISTS `open_graph_data`;
DROP TABLE IF EXISTS `users_favorites`;
DROP TABLE IF EXISTS `categories`;
DROP TABLE IF EXISTS `publications_categories`;
DROP TABLE IF EXISTS `category_allowed_keywords`;
DROP TABLE IF EXISTS `category_restricted_keywords`;
DROP TABLE IF EXISTS `source_restricted_keywords`;

CREATE TABLE `sources` (
	`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`url` VARCHAR(200) NOT NULL,
	`source_type` VARCHAR(10) NOT NULL,
	`parse_depth` INTEGER NOT NULL DEFAULT 2,
	`created` TIMESTAMP NOT NULL DEFAULT now(),
	CHECK (`source_type` IN ('Telegram', 'Web', 'RSS')),
	UNIQUE (`url`)
);

INSERT INTO `sources` VALUES
	(DEFAULT, 'https://knife.media/category/news/', 'Web', DEFAULT, DEFAULT),
	(DEFAULT, 'https://www.reddit.com/r/Popular_Science_Ru/', 'Web', DEFAULT, DEFAULT),
	(DEFAULT, 'https://www.dailymail.co.uk/articles.rss', 'RSS', DEFAULT, DEFAULT),
	(DEFAULT, 'bbcrussian', 'Telegram', DEFAULT, DEFAULT);

CREATE TABLE `users` (
	`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(30) NOT NULL,
	`password_hash` TEXT NOT NULL,
	`created` TIMESTAMP NOT NULL DEFAULT now(),
	UNIQUE(`name`)
);

CREATE TABLE `publications` (
	`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`url` VARCHAR(200) NOT NULL,
	`copies_count` INTEGER NOT NULL DEFAULT 1,
	`hash` VARCHAR(200) NOT NULL,
	`created` TIMESTAMP NOT NULL DEFAULT now(),
	`caption` VARCHAR(200) NOT NULL,
	`text` TEXT NOT NULL,
	UNIQUE(`url`),
	UNIQUE(`hash`)
);

CREATE TABLE `open_graph_data` (
	`publication_id` BIGINT NOT NULL REFERENCES `publications`(`id`),
	`property` VARCHAR(200) NOT NULL,
	`content` VARCHAR(200) NOT NULL
);

CREATE TABLE `users_favorites` (
	`user_id` BIGINT NOT NULL REFERENCES `users`(`id`),
	`publication_id` BIGINT NOT NULL REFERENCES `publications`(`id`),
	UNIQUE(`user_id`, `publication_id`)
);

CREATE TABLE `categories` (
	`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(100) NOT NULL,
	UNIQUE(`name`)
);

CREATE TABLE `publications_categories` (
	`publication_id` BIGINT NOT NULL REFERENCES `publications`(`id`),
	`category_id` BIGINT NOT NULL REFERENCES `categories`(`id`),
	UNIQUE(`category_id`, `publication_id`)
);

CREATE TABLE `category_allowed_keywords` (
	`category_id` BIGINT NOT NULL REFERENCES `categories`(`id`),
	`keyword` VARCHAR(30) NOT NULL,
	UNIQUE(`category_id`, `keyword`)
);

CREATE TABLE `category_restricted_keywords` (
	`category_id` BIGINT NOT NULL REFERENCES `categories`(`id`),
	`keyword` VARCHAR(30) NOT NULL,
	UNIQUE(`category_id`, `keyword`)
);

CREATE TABLE `source_restricted_keywords` (
	`source_id` BIGINT NOT NULL REFERENCES `sources`(`id`),
	`keyword` VARCHAR(30) NOT NULL,
	UNIQUE(`source_id`, `keyword`)
);
