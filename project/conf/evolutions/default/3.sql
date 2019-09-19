# --- !Ups

delete from user;

insert into user (type,email,role,name,password) values ( 'a','admin@whiskey.com','admin','Alice Admin', 'password');

