ALTER TABLE documents ADD COLUMN owner_id UUID;

UPDATE documents SET owner_id = '07b74ae5-1db4-4375-8c5b-6aa62f421d73';

ALTER TABLE documents ALTER COLUMN owner_id SET NOT NULL;

ALTER TABLE documents
    ADD CONSTRAINT fk_documents_owner
    FOREIGN KEY (owner_id) REFERENCES users(id);
