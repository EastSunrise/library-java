drop table if exists lib_book_author;
drop table if exists lib_book;
drop table if exists lib_author;
drop table if exists lib_category;

create table lib_category
(
    idx       varchar(16) primary key,
    title     varchar(128)     not null,
    super_idx varchar(16)      not null,
    is_leaf   tinyint unsigned not null,
    constraint fk_super_idx foreign key (super_idx) references lib_category (idx)
);

create table lib_author
(
    id            bigint unsigned primary key auto_increment,
    author_name   varchar(32)                        not null,
    original_name varchar(32),
    region        char(2)                            not null,
    link          varchar(64),
    gmt_modified  datetime default current_timestamp not null on update current_timestamp,
    constraint uk_author_name unique index (author_name)
);

create table lib_book
(
    isbn           bigint unsigned primary key,
    title          varchar(64)                        not null,
    original_title varchar(64),
    cover          varchar(64),
    press          varchar(16)                        not null,
    publish_date   date                               not null,
    category       varchar(16)                        not null,
    description    varchar(128),
    content        varchar(512),
    link           varchar(64),
    collect_status tinyint unsigned                   not null,
    read_status    tinyint unsigned                   not null,
    gmt_modified   datetime default current_timestamp not null on update current_timestamp,
    constraint fk_book_idx foreign key (category) references lib_category (idx)
);

create table lib_book_author
(
    id        bigint unsigned primary key auto_increment,
    book_isbn bigint unsigned  not null,
    author_id bigint unsigned  not null,
    work_type tinyint unsigned not null,
    constraint fk_author_book_id foreign key (book_isbn) references lib_book (isbn),
    constraint fk_book_author_id foreign key (author_id) references lib_author (id),
    constraint uk_book_author unique index (book_isbn, author_id)
);

