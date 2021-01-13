drop table authorities if exists;
drop table users if exists;
drop table user_authorities if exists;
drop table products if exists;
drop table messages if exists;
drop table orders if exists;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;

create table authorities
(
    id   UUID not null,
    name varchar(255),
    primary key (id)
);
create table users
(
    id           UUID not null,
    username     varchar(255) not null,
    password     varchar(255),
    date_created timestamp,
    name         varchar(255) not null,
    email        varchar(255) not null,
    mobile       varchar(255) not null,
    enabled      bit(1)       not null,
    primary key (id)
);
create table user_authorities
(
    user_id      UUID not null,
    authority_id UUID not null,
    primary key (user_id, authority_id)
);
create table products
(
    id             UUID         not null,
    code           varchar(255) not null,
    name           varchar(255) not null,
    summary        clob         not null,
    description    clob         not null,
    image          varchar(255),
    price          float        not null,
    in_stock       bit(1)       not null,
    time_to_stock  integer      not null,
    rating         integer      not null,
    available      bit(1)       not null,
    primary key (id)
);
create table messages
(
    id        UUID          not null,
    user_id   UUID          not null,
    text      clob          default null,
    sent_date datetime      default NOW(),
    read_date datetime      default null,
    read      bit(1)        not null,
    primary key (id)
);
create table orders
(
    id              UUID            not null,
    user_id         UUID            not null,
    order_num       varchar(255)    not null,
    order_date      datetime        default NOW(),
    amount          float           not null,
    shipped         bit(1)          not null,
    shipped_date    datetime        default null,
    primary key (id)
);

alter table users
    add constraint UKuser_username unique (username);
alter table user_authorities
    add constraint FKuser_authority_authority_id foreign key (authority_id) references authorities on delete cascade;
alter table user_authorities
    add constraint FKuser_authority_user_id foreign key (user_id) references users on delete cascade;
alter table messages
    add constraint FKmessage_user_id foreign key (user_id) references users (id) on delete cascade;
alter table orders
    add constraint FKorders_user_id foreign key (user_id) references users (id) on delete cascade;
