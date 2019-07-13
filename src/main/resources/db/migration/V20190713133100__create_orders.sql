CREATE TABLE IF NOT EXISTS orders
(
    id         uuid                        NOT NULL,
    user_id    uuid                        NOT NULL,
    ordered_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT orders_pkey
        PRIMARY KEY (id)
)
