
alter table if exists comment
    drop constraint if exists FKm1rmnfcvq5mk26li4lit88pc5;

alter table if exists comment
    drop constraint if exists FKgcgdcgly6u49hf4g8y2di3g4p;

alter table if exists grades
    drop constraint if exists FKqk8dk15w3n4wbldhaw6ubjbhn;

alter table if exists grades
    drop constraint if exists FKdeoq5b6kue0xeuivi2bn2cwl8;

alter table if exists notification
    drop constraint if exists FKevidnapp9upsd44aq70k5kcxi;

alter table if exists organization
    drop constraint if exists FK320p6saknnk5smgkcrs2d7qe8;

alter table if exists product
    drop constraint if exists FKps2e0q9jpd0i9kj83je4rsmf1;

alter table if exists product
    drop constraint if exists FKd7o25pueh2dsexxvtu2hia4i3;

alter table if exists purchase_history
    drop constraint if exists FKf1t3bdc1864c5onk6pt79pxny;

alter table if exists purchase_history
    drop constraint if exists FK2k6fiqxdm9py1knq62aob64co;

alter table if exists tabl
    drop constraint if exists FKkynreo5yqe98gh87ah6hg3d6l;

alter table if exists tag
    drop constraint if exists FKdal0uofqrt3xxhyig2jcd0dmi;

alter table if exists token
    drop constraint if exists FKssf1kt08wvjrqg5x50facgn4g;

alter table if exists user_role
    drop constraint if exists FKfpm8swft53ulq2hl11yplpr5;

alter table if exists validation_form
    drop constraint if exists FKgnsnsar4915hrn01rcom8moun;

drop table if exists comment cascade;

drop table if exists discount cascade;

drop table if exists grades cascade;

drop table if exists notification cascade;

drop table if exists organization cascade;

drop table if exists product cascade;

drop table if exists purchase_history cascade;

drop table if exists tabl cascade;

drop table if exists tag cascade;

drop table if exists token cascade;

drop table if exists user_role cascade;

drop table if exists usr cascade;

drop table if exists validation_form cascade;

drop sequence if exists token_seq;

create sequence token_seq start with 1 increment by 50;

create table comment (
                         id bigserial not null,
                         message varchar(255),
                         product_id bigint,
                         user_id bigint,
                         primary key (id)
);

create table discount (
                          id bigserial not null,
                          discount float(53),
                          discount_interval bigint,
                          primary key (id)
);

create table grades (
                        id bigserial not null,
                        grade_value float(53),
                        product_id bigint,
                        user_id bigint,
                        primary key (id)
);

create table notification (
                              id bigserial not null,
                              date_of_creation timestamp(6),
                              header varchar(255),
                              text varchar(255),
                              user_id bigint,
                              primary key (id)
);

create table organization (
                              id bigserial not null,
                              description varchar(255),
                              logo_path varchar(255),
                              name varchar(255),
                              status varchar(255),
                              user_id bigint,
                              primary key (id)
);

create table product (
                         id bigserial not null,
                         price float(53),
                         quantity bigint,
                         title varchar(255),
                         discount_id bigint,
                         organization_id bigint,
                         primary key (id)
);

create table purchase_history (
                                  id bigserial not null,
                                  date timestamp(6),
                                  product_id bigint not null,
                                  user_id bigint not null,
                                  primary key (id)
);

create table tabl (
                      id bigserial not null,
                      characteristic varchar(255),
                      table_value varchar(255),
                      product_id bigint not null,
                      primary key (id)
);

create table tag (
                     id bigserial not null,
                     tag varchar(255),
                     product_id bigint,
                     primary key (id)
);

create table token (
                       id integer not null,
                       expired boolean not null,
                       revoked boolean not null,
                       token varchar(255),
                       token_type varchar(255),
                       user_id bigint,
                       primary key (id)
);

create table user_role (
                           user_id bigint not null,
                           roles varchar(255)
);

create table usr (
                     id bigserial not null,
                     balance float(53),
                     email varchar(255),
                     password varchar(255),
                     username varchar(255),
                     primary key (id)
);

create table validation_form (
                                 id bigserial not null,
                                 approved boolean,
                                 message varchar(255),
                                 organization_id bigint,
                                 primary key (id)
);

alter table if exists token
    add constraint UK_pddrhgwxnms2aceeku9s2ewy5 unique (token);

alter table if exists comment
    add constraint FKm1rmnfcvq5mk26li4lit88pc5
        foreign key (product_id)
            references product;

alter table if exists comment
    add constraint FKgcgdcgly6u49hf4g8y2di3g4p
        foreign key (user_id)
            references usr;

alter table if exists grades
    add constraint FKqk8dk15w3n4wbldhaw6ubjbhn
        foreign key (product_id)
            references product;

alter table if exists grades
    add constraint FKdeoq5b6kue0xeuivi2bn2cwl8
        foreign key (user_id)
            references usr;

alter table if exists notification
    add constraint FKevidnapp9upsd44aq70k5kcxi
        foreign key (user_id)
            references usr;

alter table if exists organization
    add constraint FK320p6saknnk5smgkcrs2d7qe8
        foreign key (user_id)
            references usr;

alter table if exists product
    add constraint FKps2e0q9jpd0i9kj83je4rsmf1
        foreign key (discount_id)
            references discount;

alter table if exists product
    add constraint FKd7o25pueh2dsexxvtu2hia4i3
        foreign key (organization_id)
            references organization;

alter table if exists purchase_history
    add constraint FKf1t3bdc1864c5onk6pt79pxny
        foreign key (product_id)
            references product;

alter table if exists purchase_history
    add constraint FK2k6fiqxdm9py1knq62aob64co
        foreign key (user_id)
            references usr;

alter table if exists tabl
    add constraint FKkynreo5yqe98gh87ah6hg3d6l
        foreign key (product_id)
            references product;

alter table if exists tag
    add constraint FKdal0uofqrt3xxhyig2jcd0dmi
        foreign key (product_id)
            references product;

alter table if exists token
    add constraint FKssf1kt08wvjrqg5x50facgn4g
        foreign key (user_id)
            references usr;

alter table if exists user_role
    add constraint FKfpm8swft53ulq2hl11yplpr5
        foreign key (user_id)
            references usr;

alter table if exists validation_form
    add constraint FKgnsnsar4915hrn01rcom8moun
        foreign key (organization_id)
            references organization;