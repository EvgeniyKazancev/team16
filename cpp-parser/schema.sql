-- Schema for news parser
DROP TABLE IF EXISTS `users_favorites`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `publications_text`;
DROP TABLE IF EXISTS `publications_data`;
DROP TABLE IF EXISTS `publications_categories`;
DROP TABLE IF EXISTS `publications`;
DROP TABLE IF EXISTS `category_allowed_keywords`;
DROP TABLE IF EXISTS `category_restricted_keywords`;
DROP TABLE IF EXISTS `categories`;
DROP TABLE IF EXISTS `source_restricted_keywords`;
DROP TABLE IF EXISTS `sources`;

CREATE TABLE `sources` (
	`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`url` VARCHAR(200) NOT NULL,
	`source_type` VARCHAR(10) NOT NULL,
	`parse_depth` INTEGER DEFAULT 2, -- Maximum 4!
	`created` TIMESTAMP NOT NULL DEFAULT now(),
	`name` VARCHAR(50) NOT NULL,
	CHECK (`source_type` IN ('Telegram', 'Web', 'RSS')),
	CHECK (`parse_depth` >= 1 AND `parse_depth` <= 4),
	UNIQUE (`url`)
);

INSERT INTO `sources` VALUES
	(DEFAULT, 'knifemedia', 'Telegram', NULL, DEFAULT, 'Knife Media'),
	(DEFAULT, 'https://www.reddit.com/r/Popular_Science_Ru/', 'Web', DEFAULT, DEFAULT, 'Reddit Popular Science Ru'),
	(DEFAULT, 'https://www.dailymail.co.uk/articles.rss', 'RSS', NULL, DEFAULT, 'Daily Mail'),
	(DEFAULT, 'bbcrussian', 'Telegram', NULL, DEFAULT, 'BBC Russian'),
	(DEFAULT, 'nplus1', 'Telegram', NULL, DEFAULT, 'N+1'),
	(DEFAULT, 'nsmag', 'Telegram', NULL, DEFAULT, 'Naked Science'),
	(DEFAULT, 'https://newatlas.com/index.rss', 'RSS', NULL, DEFAULT, 'New Atlas'),
	(DEFAULT, 'https://www.theverge.com/rss/index.xml', 'RSS', NULL, DEFAULT, 'The Verge'),
	(DEFAULT, 'meduza_news', 'Telegram', NULL, DEFAULT, 'Медуза');


CREATE TABLE `users` (
	`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`email` VARCHAR(30) NOT NULL,
	`first_name` VARCHAR(30) NOT NULL,
	`last_name` VARCHAR(30) NOT NULL,
	`patronym` VARCHAR(30),
	`password_hash` TEXT NOT NULL,
	`created` TIMESTAMP NOT NULL DEFAULT now(),
	UNIQUE(`email`)
);

INSERT INTO `users` VALUES (DEFAULT, 'lordgprs@yandex.ru', 'Максим', 'Вельгач', 'Сергеевич', MD5('12345'), DEFAULT);

CREATE TABLE `publications` (
	`id` BIGINT NOT NULL PRIMARY KEY,
	`source_id` BIGINT NOT NULL,
	`url` VARCHAR(500) NOT NULL,
	`copies_count` INTEGER NOT NULL DEFAULT 1,
	`created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	UNIQUE(`url`),
	FOREIGN KEY (`source_id`)
		REFERENCES `sources`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE `publications_text` (
	`publication_id` BIGINT NOT NULL,
	`is_header` BOOLEAN NOT NULL DEFAULT FALSE,
	`text` TEXT NOT NULL,
	FOREIGN KEY (`publication_id`)
		REFERENCES `publications`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE `publications_data` (
	`publication_id` BIGINT NOT NULL,
	`property` VARCHAR(200) NOT NULL,
	`content` VARCHAR(500) NOT NULL,
	FOREIGN KEY (`publication_id`)
		REFERENCES `publications`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE `users_favorites` (
	`user_id` BIGINT NOT NULL,
	`publication_id` BIGINT NOT NULL,
	UNIQUE(`user_id`, `publication_id`),
	FOREIGN KEY (`user_id`)
		REFERENCES `users`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (`publication_id`)
		REFERENCES `publications`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE `categories` (
	`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(100) NOT NULL,
	UNIQUE(`name`)
);

CREATE TABLE `publications_categories` (
	`publication_id` BIGINT NOT NULL,
	`category_id` BIGINT NOT NULL,
	UNIQUE(`category_id`, `publication_id`),
	FOREIGN KEY (`publication_id`)
		REFERENCES `publications`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY (`category_id`)
		REFERENCES `categories`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE `category_allowed_keywords` (
	`category_id` BIGINT NOT NULL,
	`keyword` VARCHAR(30) NOT NULL,
	UNIQUE(`category_id`, `keyword`),
	FOREIGN KEY (`category_id`)
		REFERENCES `categories`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE `category_restricted_keywords` (
	`category_id` BIGINT NOT NULL,
	`keyword` VARCHAR(30) NOT NULL,
	UNIQUE(`category_id`, `keyword`),
	FOREIGN KEY (`category_id`)
		REFERENCES `categories`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);

CREATE TABLE `source_restricted_keywords` (
	`source_id` BIGINT NOT NULL,
	`keyword` VARCHAR(30) NOT NULL,
	UNIQUE(`source_id`, `keyword`),
	FOREIGN KEY (`source_id`)
		REFERENCES `sources`(`id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE
);
