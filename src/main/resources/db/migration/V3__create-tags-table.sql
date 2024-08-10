CREATE TABLE tags (
    note_id UUID,
    tag VARCHAR(30) NOT NULL,
    PRIMARY KEY (note_id, tag),
    FOREIGN KEY (note_id) REFERENCES notes(id)
);