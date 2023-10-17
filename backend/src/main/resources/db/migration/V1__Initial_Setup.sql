CREATE TABLE customers
(
    id    BIGSERIAL PRIMARY KEY,
    name  TEXT NOT NULL,
    email TEXT NOT NULL,
    age   INT  NOT NULL
);

ALTER TABLE customers
    ADD CONSTRAINT customers_email_unique UNIQUE (email);
