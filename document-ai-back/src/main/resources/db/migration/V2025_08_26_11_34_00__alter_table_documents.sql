ALTER TABLE documents ADD COLUMN owner_id UUID;

UPDATE documents SET owner_id = '04e9ce1f-c15b-4a25-992b-96b03adaa2a0';

ALTER TABLE documents ALTER COLUMN owner_id SET NOT NULL;

ALTER TABLE documents
    ADD CONSTRAINT fk_documents_owner
    FOREIGN KEY (owner_id) REFERENCES users(id);
