create table if not exists hits
(
    hit_id    bigint generated by default as identity
        constraint hits_pk
            primary key,
    app       varchar(50) not null,
    uri       varchar(50) not null,
    ip        varchar(39) not null,
    timestamp timestamp   not null
);