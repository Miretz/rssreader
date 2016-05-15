alter table FEEDS drop foreign key FK87sxa2e6taq2hv5qp3crjuixd
alter table MESSAGES drop foreign key FK3y42tm6keu0go6qh18lmqhwph
drop table if exists ACCOUNTS
drop table if exists FEEDS
drop table if exists hibernate_sequence
drop table if exists MESSAGES
create table ACCOUNTS (ACCOUNT_ID integer not null, PASSWORD varchar(255), USERNAME varchar(255), primary key (ACCOUNT_ID))
create table FEEDS (FEED_ID integer not null, NAME varchar(255), URL varchar(255), ACCOUNT_ID integer, primary key (FEED_ID))
create table hibernate_sequence (next_val bigint)
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table MESSAGES (MESSAGE_ID integer not null, AUTHOR varchar(255), IS_FAVOURITE bit, GUID varchar(255), PUBLISH_DATE datetime, IS_READ bit, SUMMARY TEXT, TITLE varchar(255), URL varchar(255), FEED_ID integer, primary key (MESSAGE_ID))
alter table MESSAGES add constraint UK713ei1tpqa9px3ng6msv4fly4 unique (GUID)
alter table FEEDS add constraint FK87sxa2e6taq2hv5qp3crjuixd foreign key (ACCOUNT_ID) references ACCOUNTS (ACCOUNT_ID)
alter table MESSAGES add constraint FK3y42tm6keu0go6qh18lmqhwph foreign key (FEED_ID) references FEEDS (FEED_ID)
