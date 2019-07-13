CREATE TABLE IF NOT EXISTS items
(
    id   uuid          NOT NULL,
    name varchar(1000) NOT NULL,
    CONSTRAINT items_pkey
        PRIMARY KEY (id)
)
