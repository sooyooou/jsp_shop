create table member(
	id varchar(50) not null primary key,
	passwd varchar(16) not null,
	name varchar(10) not null,
	reg_date datetime not null,
	address varchar(100) not null,
	tel varchar(20) not null
);

show tables;
desc member;

create table manager(
	managerId varchar(50) not null primary key,
	managerPasswd varchar(16) not null
);
insert into manager(managerId,managerPasswd) value ("aaa","123");
select * from manager;
create table book(
	book_id int not null primary key auto_increment,
	book_kind varchar(3) not null,
	book_title varchar(100) not null,
	book_price int not null,
	book_count smallint not null,
	author varchar(40) not null,
	publish_com varchar(30) not null,
	publish_date varchar(15) not null,
	book_image varchar(16) default 'nothing.jpg',
	book_content text not null,
	discount_rate tinyint default 10,
	reg_date datetime not null
);

select * from book;

create table bank(
	account varchar(30) not null,
	bank varchar(10) not null,
	name varchar(10) not null
);

create table cart(
	cart_id int not null primary key auto_increment,
	buyer varchar(50) not null,
	book_id int not null,
	book_title varchar(100) not null,
	buy_price int not null,
	buy_count tinyint not null,
	book_image varchar(16) default 'nothing.jpg'
);

create table buy(
	buy_id bigint not null,
	buyer varchar(50) not null,
	book_id varchar(12) not null,
	book_title varchar(100) not null,
	buy_price int not null,
	buy_count tinyint not null,
	book_image varchar(16) default 'nothing.jpg',
	buy_date datetime not null,
	account varchar(50) not null,
	deliveryName varchar(10) not null,
	deliveryTel varchar(20) not null,
	deliveryAddress varchar(100) not null,
	sanction varchar(10) default '상품 준비중'
);