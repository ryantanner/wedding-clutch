# --- Sample dataset

# --- !Ups

insert into user (email, name, password) values ('jane.smith@gmail.com', 'Jane Smith', 'password');
insert into user (email, name, password) values ('mary.kate@gmail.com', 'Mary Kate', 'password');
insert into user (email, name, password) values ('ellen.joy@gmail.com', 'Ellen Joy', 'password');
insert into user (email, name, password) values ('cassie.constanzo@gmail.com', 'Cassie Costanzo', 'password');

insert into user_admins (user_id) values (1);

insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 1', '2012-04-15', 'Country Club', 1);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 2', '2012-04-16', 'Country Club', 1);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 3', '2012-04-17', 'Country Club', 1);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 4', '2012-04-18', 'Country Club', 1);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 5', '2012-04-19', 'Country Club', 1);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 6', '2012-04-11', 'Country Club', 1);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 7', '2012-04-12', 'Country Club', 1);


insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 8', '2012-04-15', 'Country Club', 2);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 9', '2012-04-16', 'Country Club', 2);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 10', '2012-04-17', 'Country Club', 2);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 11', '2012-04-18', 'Country Club', 2);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 12', '2012-04-19', 'Country Club', 2);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 13', '2012-04-25', 'Country Club', 2);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 14', '2012-04-27', 'Country Club', 2);



insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 15', '2012-04-15', 'Country Club', 3);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 16', '2012-04-16', 'Country Club', 3);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 17', '2012-04-17', 'Country Club', 3);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 18', '2012-04-18', 'Country Club', 3);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 19', '2012-04-19', 'Country Club', 3);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 20', '2012-04-25', 'Country Club', 3);
insert into wedding (name, date, venue, coordinator_id) values ('Test Wedding 21', '2012-04-27', 'Country Club', 3);

# --- !Downs

delete from wedding;
delete from user_admins;
delete from user;

