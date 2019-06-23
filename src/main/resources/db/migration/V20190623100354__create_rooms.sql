CREATE TABLE IF NOT EXISTS rooms
(
    id         uuid                        NOT NULL,
    name       varchar(30)                 NOT NULL,
    creator_id uuid                        NOT NULL,
    created_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT rooms_pkey
        PRIMARY KEY (id)
)
