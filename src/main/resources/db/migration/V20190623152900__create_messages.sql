CREATE TABLE IF NOT EXISTS messages
(
    id      uuid                        NOT NULL,
    room_id uuid                        NOT NULL,
    user_id uuid                        NOT NULL,
    message varchar(1000)               NOT NULL,
    sent_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT messages_pkey
        PRIMARY KEY (id),

    CONSTRAINT foreign_key_room
        FOREIGN KEY (room_id)
            REFERENCES rooms (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT foreign_key_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
