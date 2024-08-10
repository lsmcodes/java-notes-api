CREATE TABLE users (
    id UUID,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);