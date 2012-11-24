# --- Sample dataset

# --- !Ups

insert into wedding (id, name, date, venue, owner) values (1, 'My first wedding', '2012-04-15', 'Country Club', 'Jane Smith');

insert into user (email, name, password) values ('jane.smith@gmail.com', 'Jane Smith', 'password');

# --- !Downs

delete from wedding;
delete from user;
