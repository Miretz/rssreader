    alter table FEEDS 
        drop 
        foreign key FK_cpltyr0vwqmg200xxh9o340rm;

    alter table MESSAGES 
        drop 
        foreign key FK_cwud5s66kxqonvxhbiv8ld23q;

    drop table if exists ACCOUNTS;

    drop table if exists FEEDS;

    drop table if exists MESSAGES;

    create table ACCOUNTS (
        ACCOUNT_ID integer not null auto_increment,
        PASSWORD varchar(255),
        USERNAME varchar(255),
        primary key (ACCOUNT_ID)
    );

    create table FEEDS (
        FEED_ID integer not null auto_increment,
        NAME varchar(255),
        URL varchar(255),
        ACCOUNT_ID integer,
        primary key (FEED_ID)
    );

    create table MESSAGES (
        MESSAGE_ID integer not null auto_increment,
        AUTHOR varchar(255),
        IS_FAVOURITE boolean,
        GUID varchar(255),
        PUBLISH_DATE datetime,
        IS_READ boolean,
        SUMMARY TEXT,
        TITLE varchar(255),
        URL varchar(255),
        FEED_ID integer,
        primary key (MESSAGE_ID)
    );

    alter table FEEDS 
        add index FK_cpltyr0vwqmg200xxh9o340rm (ACCOUNT_ID), 
        add constraint FK_cpltyr0vwqmg200xxh9o340rm 
        foreign key (ACCOUNT_ID) 
        references ACCOUNTS (ACCOUNT_ID);

    alter table MESSAGES 
        add constraint UK_713ei1tpqa9px3ng6msv4fly4 unique (GUID);

    alter table MESSAGES 
        add index FK_cwud5s66kxqonvxhbiv8ld23q (FEED_ID), 
        add constraint FK_cwud5s66kxqonvxhbiv8ld23q 
        foreign key (FEED_ID) 
        references FEEDS (FEED_ID);