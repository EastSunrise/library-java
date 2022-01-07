drop table if exists video_resource_link;
drop table if exists video_resource;
drop table if exists video_celebrity_relation;
drop table if exists video_season;
drop table if exists video_series;
drop table if exists video_movie;
drop table if exists video_celebrity;
drop table if exists video_id_relation;

create table video_id_relation
(
    douban_id bigint unsigned primary key,
    imdb_id   varchar(10) not null,
    constraint uk_video_imdb_id unique index (imdb_id)
);

create table video_celebrity
(
    id            bigint unsigned primary key auto_increment,
    douban_id     bigint unsigned,
    imdb_id       char(9)                            not null,
    zh_name       varchar(32)                        not null,
    original_name varchar(32)                        not null,
    en_name       varchar(32),
    gender        tinyint unsigned                   not null,
    birthday      date,
    region        char(2)                            not null,
    gmt_modified  datetime default current_timestamp not null on update current_timestamp,
    constraint uk_celebrity_douban_id unique index (douban_id),
    constraint uk_celebrity_imdb_id unique index (imdb_id)
);

create table video_movie
(
    id             bigint unsigned primary key auto_increment,
    douban_id      bigint unsigned                    not null,
    imdb_id        varchar(10)                        not null,
    zh_title       varchar(32)                        not null,
    original_title varchar(128)                       not null,
    en_title       varchar(128),
    other_titles   varchar(256),
    genres         varchar(32)                        not null,
    regions        varchar(64)                        not null,
    languages      varchar(128)                       not null,
    release_date   date,
    runtimes       varchar(16),
    description    varchar(512),
    gmt_modified   datetime default current_timestamp not null on update current_timestamp,
    constraint uk_movie_douban_id unique index (douban_id),
    constraint uk_movie_imdb_id unique index (imdb_id)
) auto_increment = 1000000;

create table video_series
(
    id             bigint unsigned primary key auto_increment,
    imdb_id        varchar(10)                        not null,
    zh_title       varchar(32),
    original_title varchar(64),
    en_title       varchar(128)                       not null,
    other_titles   varchar(256),
    gmt_modified   datetime default current_timestamp not null on update current_timestamp,
    constraint uk_series_imdb_id unique index (imdb_id)
) auto_increment = 5000000;

create table video_season
(
    id             bigint unsigned primary key auto_increment,
    series_id      bigint unsigned                    not null,
    current_season tinyint unsigned                   not null,
    episodes_count tinyint unsigned                   not null,
    douban_id      bigint unsigned                    not null,
    zh_title       varchar(32)                        not null,
    original_title varchar(128)                       not null,
    other_titles   varchar(256),
    genres         varchar(32)                        not null,
    regions        varchar(64)                        not null,
    languages      varchar(128)                       not null,
    release_date   date,
    description    varchar(512),
    gmt_modified   datetime default current_timestamp not null on update current_timestamp,
    constraint uk_season_douban_id unique index (douban_id),
    constraint fk_season_series_id foreign key (series_id) references video_series (id),
    constraint uk_season_series_index unique index (series_id, current_season)
) auto_increment = 6000000;

create table video_celebrity_relation
(
    id           bigint unsigned primary key auto_increment,
    video_id     bigint unsigned                    not null,
    celebrity_id bigint unsigned                    not null,
    status       tinyint unsigned                   not null,
    gmt_modified datetime default current_timestamp not null on update current_timestamp,
    constraint fk_video_celebrity_id foreign key (celebrity_id) references video_celebrity (id),
    constraint uk_video_celebrity_relation unique index (video_id, celebrity_id, status)
);

create table video_resource
(
    id              bigint unsigned primary key auto_increment,
    video_id        bigint unsigned,
    source_site     varchar(32)                        not null,
    source_link     varchar(64)                        not null,
    title           varchar(64)                        not null,
    publish_time    datetime,
    resource_status tinyint unsigned                   not null,
    cover           varchar(64),
    video_year      smallint unsigned,
    douban_id       bigint unsigned,
    imdb_id         varchar(10),
    gmt_modified    datetime default current_timestamp not null on update current_timestamp,
    constraint uk_video_resource_source_link unique index (source_link)
);

create table video_resource_link
(
    id           bigint unsigned primary key auto_increment,
    resource_id  bigint unsigned                    not null,
    title        varchar(64),
    link_type    tinyint unsigned                   not null,
    url          varchar(512)                       not null,
    password     char(4),
    filename     varchar(512),
    filesize     int unsigned,
    gmt_modified datetime default current_timestamp not null on update current_timestamp,
    constraint fk_video_resource_id foreign key (resource_id) references video_resource (id)
);