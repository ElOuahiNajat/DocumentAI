create table documents
(
    id          uuid primary key default uuid_generate_v4(),
    title       varchar(255)                            not null,
    author      varchar(255),
    description text,
    created_at  timestamp        default localtimestamp not null,
    updated_at  timestamp        default localtimestamp not null,
    file_type   varchar(100)                            not null,
    file_size   int                                     not null,
    file_path   varchar(255)                            not null
);