CREATE TABLE IF NOT EXISTS room_user
(
    room_id   uuid                        NOT NULL,
    user_id   uuid                        NOT NULL,
    joined_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT room_user_index
        UNIQUE (room_id, user_id),

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
