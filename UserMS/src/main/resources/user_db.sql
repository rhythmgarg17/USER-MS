drop schema user_db;

create schema user_db;

use user_db;

create table buyer(
buyer_id varchar(10),
name varchar(20),
email varchar(50),
password varchar(20),
phone_no varchar(10),
is_privileged boolean,
reward_points int,
is_active boolean,
primary key (buyer_id));

insert into buyer values("B101","Tanji","tanji@infosys.com","Tanji@123","1234567890",false,0,true);
insert into buyer values("B102","Naruto","naruto@infosys.com","Naruto@123","0987654321",true,0,true);

create table seller(
seller_id varchar(10),
name varchar(20),
email varchar(50),
password varchar(20),
phone_no varchar(10),
is_active boolean,
primary key (seller_id));

insert into seller values("S101","Tanji","tanji@infosys.com","Tanji@123","1234567890",true);
insert into seller values("S102","Naruto","naruto@infosys.com","Naruto@123","0987654321",true);

create table cart(
buyer_id varchar(10),
product_id varchar(20),
quantity int,
primary key (buyer_id, product_id));

insert into cart values("B101","P102",5);
insert into cart values("B102","P105",6);
insert into cart values("B102","P104",2);

create table wishlist(
buyer_id varchar(10),
product_id varchar(20),
primary key (buyer_id, product_id));

insert into wishlist values("B101","P102");
insert into wishlist values("B102","P105");
insert into wishlist values("B102","P104");


select * from buyer;
select * from seller;
select * from cart;
select * from wishlist;