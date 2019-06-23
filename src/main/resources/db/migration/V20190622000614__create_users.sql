CREATE TABLE IF NOT EXISTS users
(
    id          uuid         NOT NULL,
    username    varchar(30)  NOT NULL,
    password    varchar(60)  NOT NULL,
    credentials varchar(250) NOT NULL,
    active      boolean      NOT NULL DEFAULT true,
    deleted     boolean      NOT NULL DEFAULT false,
    CONSTRAINT users_pkey
        PRIMARY KEY (id)
)
