CREATE TABLE feedbacks (
    id          uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    content     text NULL,
    note        smallint NULL,
    created_at  timestamp DEFAULT localtimestamp NOT NULL,
    document_id uuid NOT NULL REFERENCES documents(id)
);

INSERT INTO feedbacks (content, note, created_at, document_id)
SELECT content, NULL, created_at, document_id
FROM comments;

INSERT INTO feedbacks (content, note, created_at, document_id)
SELECT NULL, note, created_at, document_id
FROM evaluations;

DROP TABLE comments;
DROP TABLE evaluations;
