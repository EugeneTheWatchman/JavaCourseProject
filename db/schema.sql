-- Здесь располагаются DDL-команды для создания схемы БД
-- Рекомендуется подробно описывать, для чего необходима каждая таблица
-- DLL-команды следует приводить в отформатированном виде, чтобы их было удобно читать

-- таблица пользователей
CREATE TABLE users (
    id             	integer     	auto_increment primary key,
    login     	    varchar(30) 	NOT NULL unique,
    is_admin	   	boolean       	NOT NULL
);

-- таблица тестов
CREATE TABLE tests (
	id					integer			auto_increment primary key,
	description 		varchar(100),
	text				varchar(30) 	NOT NULL unique
);

-- таблица с вариантами тестов
CREATE TABLE variants (
	id			integer			auto_increment primary key,
	test_id		integer			NOT NULL,
	text		varchar(30)		NOT NULL,
	is_right	boolean			NOT NULL,
	constraint uk_variants_tesIid_text unique(text, test_id),
	constraint fk_variants_test_id foreign key (test_id) references tests (id)
);

-- таблица с результатами похождения тестов у пользователей
CREATE TABLE users_tests (
	id			integer		auto_increment primary key,
	user_id		integer		NOT NULL,
	test_id		integer		NOT NULL,
	is_passed	boolean		NOT NULL,
	pass_date	timestamp	NOT NULL,
	constraint uk_users_tests_user_test unique(user_id, test_id, pass_date),
    constraint fk_users_tests_user foreign key (user_id) references users (id),
    constraint fk_users_tests_test foreign key (test_id) references tests (id)
);
