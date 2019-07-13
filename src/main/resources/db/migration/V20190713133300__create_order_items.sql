CREATE TABLE IF NOT EXISTS order_items
(
    order_id   uuid                        NOT NULL,
    item_id    uuid                        NOT NULL,
    ordered_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT order_item_index
        UNIQUE (order_id, item_id),

    CONSTRAINT foreign_key_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT foreign_key_item
        FOREIGN KEY (item_id)
            REFERENCES items (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
