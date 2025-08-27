ALTER TABLE documents ADD COLUMN owner_id UUID;

UPDATE documents SET owner_id = '2347c1c0-4568-4fdf-8fff-d9281b0f7068';

ALTER TABLE documents ALTER COLUMN owner_id SET NOT NULL;

ALTER TABLE documents
    ADD CONSTRAINT fk_documents_owner
    FOREIGN KEY (owner_id) REFERENCES users(id);
