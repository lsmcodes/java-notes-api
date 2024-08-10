CREATE TABLE notes (
    id UUID,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    user_id UUID,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);