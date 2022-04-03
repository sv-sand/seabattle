CREATE TABLE users (
    id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (id)
);

CREATE TABLE scores (
    id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    date DATETIME not null,
    score integer,
    PRIMARY KEY (id),
    UNIQUE (id)
);

ALTER TABLE scores
    ADD FOREIGN KEY (user_id)
    REFERENCES users(id);
