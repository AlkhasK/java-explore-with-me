create table if not exists users
(
    user_id   bigint generated by default as identity
        constraint users_pk
            primary key,
    email     varchar(50) not null
        constraint users_unique_email
            unique,
    user_name varchar(50) not null
);

create table if not exists categories
(
    category_id   bigint generated by default as identity
        constraint categories_pk
            primary key,
    category_name varchar not null
        constraint categories_unique_name
            unique
);

create table if not exists events
(
    event_id           bigint generated by default as identity
        constraint events_pk
            primary key,
    annotation         varchar(2000) not null,
    category_id        bigint        not null
        constraint events_categories_fk
            references categories,
    created_on         timestamp,
    description        varchar(7000) not null,
    event_date         timestamp     not null,
    initiator_id       bigint        not null
        constraint events_users_fk
            references users
            on delete cascade,
    paid               boolean       not null,
    participant_limit  integer       not null,
    published_on       timestamp,
    request_moderation boolean       not null,
    state              varchar(10)   not null,
    title              varchar(120)  not null,
    lat                real          not null,
    lon                real          not null
);

create table if not exists participation_requests
(
    request_id bigint generated by default as identity
        constraint participation_requests_pk
            primary key,
    created    timestamp  not null,
    user_id    bigint     not null
        constraint participation_requests_users_null_fk
            references users
            on delete cascade,
    event_id   bigint     not null
        constraint participation_requests_events_null_fk
            references events
            on delete cascade,
    status     varchar(9) not null,
    constraint participation_requests_event_user_unique
        unique (event_id, user_id)
);

create table if not exists compilation
(
    compilation_id    bigint generated by default as identity
        constraint compilation_pk
            primary key,
    compilation_title varchar not null
        constraint compilation_title_unique
            unique,
    pinned            boolean not null
);

create table if not exists compilation_event
(
    compilation_id bigint not null
        constraint compilation_event_compilation_compilation_id_fk
            references compilation
            on delete cascade,
    event_id       bigint not null
        constraint compilation_event_events_event_id_fk
            references events
            on delete cascade,
    constraint compilation_event_pk
        primary key (compilation_id, event_id)
);

create table if not exists comments
(
    comment_id bigint generated by default as identity
        constraint comments_pk
            primary key,
    author_id  bigint      not null
        constraint comments_users_null_fk
            references users
            on delete cascade,
    event_id   bigint      not null
        constraint comments_events_null_fk
            references events
            on delete cascade,
    text       varchar     not null,
    status     varchar(10) not null,
    created_on timestamp   not null
);