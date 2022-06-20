drop table authorities if exists cascade;
drop table users if exists cascade;
drop table user_authorities if exists cascade;
drop table verifications if exists cascade;
drop table products if exists cascade;
drop table messages if exists cascade;
drop table orders if exists cascade;
drop table reviews if exists cascade;
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
    first_name   varchar(255) not null,
    last_name    varchar(255) not null,
    email        varchar(255) not null,
    phone        varchar(255) not null,
    address      varchar(255) default null,
    city         varchar(255) default null,
    state        varchar(255) default null,
    zip          varchar(255) default null,
    country      varchar(255) default null,
    enabled      bit(1)       not null,
    primary key (id)
);
create table user_authorities
(
    user_id      UUID not null,
    authority_id UUID not null,
    primary key (user_id, authority_id)
);
create table verifications
(
    phone       varchar(255)    not null,
    request_id  varchar(255)    not null,
    expiry_date datetime        default null,
    primary key (phone)
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
    on_sale        bit(1)       default 0 not null,
    sale_price     float        default 0.0 not null,
    in_stock       bit(1)       default 1 not null,
    time_to_stock  integer      default 0 not null,
    rating         integer      default 1 not null,
    available      bit(1)       default 1 not null,
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
    cart            clob            default null,
    shipped         bit(1)          not null,
    shipped_date    datetime        default null,
    primary key (id)
);
create table reviews
(
    id              UUID            not null,
    product_id      UUID            not null,
    user_id         UUID            not null,
    review_date     datetime        default NOW(),
    comment         clob            default null,
    rating          integer         default 1 not null,
    visible         bit(1)          not null,
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
alter table reviews
    add constraint FKproducts_product_id foreign key (product_id) references products (id) on delete cascade;
alter table reviews
    add constraint FKproducts_user_id foreign key (user_id) references users (id) on delete cascade;
