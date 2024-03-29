# --- First database schema

# --- !Ups

set ignorecase true;

create table account (
  id                bigint identity not null,
  email             varchar(255) not null,
  password          varchar(255) not null,
  name              varchar(255) not null,
  permission        varchar(255) not null
);

create table wedding (
  id                bigint identity not null,
  name              varchar(255) not null,
  date              timestamp not null,
  venue             varchar(255) not null,
  coordinator_id    bigint not null,
  foreign key(coordinator_id) references account(id))
;

create table event (
  id                bigint identity not null,
  name              varchar(255) not null,
  timeline_order    int not null,
  coordinator_id    bigint not null,
  wedding_id        bigint not null,
  foreign key(coordinator_id) references account(id) on delete cascade,
  foreign key(wedding_id) references wedding(id) on delete cascade)
;

create table vendor (
  id                bigint identity not null,
  name              varchar(255) not null,
  role              varchar(255) not null,
  phone             varchar(12) not null,
  coordinator_id    bigint not null,
  foreign key(coordinator_id) references account(id))
;

create table events_vendors (
  event_id          bigint not null,
  vendor_id         bigint not null,
  coordinator_id    bigint not null,
  foreign key(event_id) references event(id) on delete cascade,
  foreign key(vendor_id) references vendor(id) on delete cascade,
  foreign key(coordinator_id) references account(id) on delete cascade)
;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists wedding;
drop table if exists event;
drop table if exists vendor;
drop table if exists events_vendors;
drop table if exists account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists wedding_seq;

