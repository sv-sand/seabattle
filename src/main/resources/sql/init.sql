CREATE TABLE _params (
    version INTEGER NOT NULL
);

CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (id)
);

CREATE TABLE scores (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    date DATETIME not null,
    score integer,

    PRIMARY KEY (id),
    UNIQUE (id)
);

ALTER TABLE scores
    ADD FOREIGN KEY (user_id)
    REFERENCES users(id);
