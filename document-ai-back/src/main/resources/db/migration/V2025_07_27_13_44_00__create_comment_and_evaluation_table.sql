create table comments (
    id          uuid primary key default uuid_generate_v4(),
    content     text not null,
    created_at  timestamp default localtimestamp not null,
    document_id uuid not null references documents(id)
);

create table evaluations (
    id          uuid primary key default uuid_generate_v4(),
    note        TINYINT not null,
    created_at  timestamp default localtimestamp not null,
    document_id uuid not null references documents(id)
);