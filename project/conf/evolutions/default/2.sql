# --- Sample dataset
 
# --- !Ups
 
delete from item_on_sale;
delete from category;
 
insert into category (id,name) values ( 1,'Irish Whiskey' );
insert into category (id,name) values ( 2,'Scottish Whiskey' );
insert into category (id,name) values ( 3,'Russian Whiskey' );
insert into category (id,name) values ( 4,'Japan Whiskey' );
insert into category (id,name) values ( 5,'Serbian Whiskey' );
insert into category (id,name) values ( 6,'American Whiskey' );
 
insert into item_on_sale (id,name,description,stock,price) values ( 1,'Proper 12','Proper Irish whiskey from D12',1, 100.00 );
insert into item_on_sale (id,name,description,stock,price) values ( 2,'Homemade Scottish Whiskey','Scottish brew',1,3.00 );

insert into CATEGORY_ITEM_ON_SALE(category_id,item_on_sale_id) values (1,1);
insert into CATEGORY_ITEM_ON_SALE(category_id,item_on_sale_id) values (2,2);
