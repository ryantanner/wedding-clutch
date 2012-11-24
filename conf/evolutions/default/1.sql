# --- First database schema

# --- !Ups

set ignorecase true;

create table wedding (
  id                bigint auto_increment not null,
  name              varchar(255) not null,
  date              timestamp not null,
  venue             varchar(255) not null,
  owner_id          bigint not null
  constraint pk_wedding primary key (id),
  foreign key (owner_id) references user(id))
;

create table user (
  id                bigint auto_increment not null,
  email             varchar(255) not null,
  name              varchar(255) not null,
  password          varchar(255) not null
  constraint pk_user primary key (id)
);

create table event (
  id                bigint auto_increment not null,
  name              varchar(255) not null,
  order             int not null,
  wedding_id        bigint not null,
  constraint pk_event primary key (id),
  foreign key (wedding_id) references wedding(id))
;

create table events_vendors (
  event_id          bigint not null,
  vendor_id         bigint not null,
  foreign key (event_id) references event(id),
  foreign key (vendor_id) references vendor(id))
;



create sequence wedding_seq start with 1000;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists wedding;
drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists wedding_seq;

