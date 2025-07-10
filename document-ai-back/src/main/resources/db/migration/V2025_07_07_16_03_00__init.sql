CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

create table users
(
    id    uuid primary key default uuid_generate_v4(),
    email varchar(255) not null unique
)