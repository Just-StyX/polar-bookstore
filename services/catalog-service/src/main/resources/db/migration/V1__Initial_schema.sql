CREATE TABLE book (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    title VARCHAR(255) NOT NULL,
    version INTEGER NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL
);