--
-- Файл сгенерирован с помощью SQLiteStudio v3.2.1 в Вт сен 8 22:20:38 2020
--
-- Использованная кодировка текста: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Таблица: android_metadata
DROP TABLE IF EXISTS android_metadata;
CREATE TABLE android_metadata (locale TEXT);
INSERT INTO android_metadata (locale) VALUES ('ru_RU');

-- Таблица: my_accounts
DROP TABLE IF EXISTS my_accounts;
CREATE TABLE `my_accounts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `summoner_id` INTEGER NOT NULL, FOREIGN KEY(`summoner_id`) REFERENCES `summoners`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION );

-- Таблица: friends
DROP TABLE IF EXISTS friends;
CREATE TABLE `friends` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `my_account_id` INTEGER NOT NULL, `summoner_id` INTEGER NOT NULL, FOREIGN KEY(`my_account_id`) REFERENCES `my_accounts`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`summoner_id`) REFERENCES `summoners`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION );

-- Таблица: regions
DROP TABLE IF EXISTS regions;
CREATE TABLE `regions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tag` TEXT NOT NULL);
INSERT INTO regions (id, tag) VALUES (1, 'BR');
INSERT INTO regions (id, tag) VALUES (2, 'EUNE');
INSERT INTO regions (id, tag) VALUES (3, 'EUW');
INSERT INTO regions (id, tag) VALUES (4, 'JP');
INSERT INTO regions (id, tag) VALUES (5, 'KR');
INSERT INTO regions (id, tag) VALUES (6, 'LAN');
INSERT INTO regions (id, tag) VALUES (7, 'LAS');
INSERT INTO regions (id, tag) VALUES (8, 'NA');
INSERT INTO regions (id, tag) VALUES (9, 'OCE');
INSERT INTO regions (id, tag) VALUES (10, 'RU');
INSERT INTO regions (id, tag) VALUES (11, 'TR');

-- Таблица: room_master_table
DROP TABLE IF EXISTS room_master_table;
CREATE TABLE room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT);
INSERT INTO room_master_table (id, identity_hash) VALUES (42, '9d1d1b355479fedd9c8dbd70a638905c');

-- Таблица: state_current_accounts
DROP TABLE IF EXISTS state_current_accounts;
CREATE TABLE `state_current_accounts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `region_id` INTEGER NOT NULL, `account_id` INTEGER NOT NULL, FOREIGN KEY(`region_id`) REFERENCES `regions`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`account_id`) REFERENCES `my_accounts`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION );

-- Таблица: state_global
DROP TABLE IF EXISTS state_global;
CREATE TABLE `state_global` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `current_account_id` INTEGER, FOREIGN KEY(`current_account_id`) REFERENCES `state_current_accounts`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION );
INSERT INTO state_global (id, current_account_id) VALUES (1, NULL);

-- Таблица: summoners
DROP TABLE IF EXISTS summoners;
CREATE TABLE `summoners` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `puuid` TEXT NOT NULL, `region_id` INTEGER NOT NULL, FOREIGN KEY(`region_id`) REFERENCES `regions`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION );

-- Индекс: index_my_accounts_summoner_id
DROP INDEX IF EXISTS index_my_accounts_summoner_id;
CREATE UNIQUE INDEX `index_my_accounts_summoner_id` ON `my_accounts` (`summoner_id`);

-- Индекс: index_friends_my_account_id
DROP INDEX IF EXISTS index_friends_my_account_id;
CREATE INDEX `index_friends_my_account_id` ON `friends` (`my_account_id`);

-- Индекс: index_friends_summoner_id
DROP INDEX IF EXISTS index_friends_summoner_id;
CREATE INDEX `index_friends_summoner_id` ON `friends` (`summoner_id`);

-- Индекс: index_regions_tag
DROP INDEX IF EXISTS index_regions_tag;
CREATE INDEX `index_regions_tag` ON `regions` (`tag`);

-- Индекс: index_state_current_accounts_account_id
DROP INDEX IF EXISTS index_state_current_accounts_account_id;
CREATE UNIQUE INDEX `index_state_current_accounts_account_id` ON `state_current_accounts` (`account_id`);

-- Индекс: index_state_current_accounts_region_id
DROP INDEX IF EXISTS index_state_current_accounts_region_id;
CREATE UNIQUE INDEX `index_state_current_accounts_region_id` ON `state_current_accounts` (`region_id`);

-- Индекс: index_state_global_current_account_id
DROP INDEX IF EXISTS index_state_global_current_account_id;
CREATE INDEX `index_state_global_current_account_id` ON `state_global` (`current_account_id`);

-- Индекс: index_summoners_region_id
DROP INDEX IF EXISTS index_summoners_region_id;
CREATE INDEX `index_summoners_region_id` ON `summoners` (`region_id`);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
