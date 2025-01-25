CREATE TABLE productions
(
    id          CHAR(36) NOT NULL,
    order_id    CHAR(36) NOT NULL,
    status      VARCHAR(255) NOT NULL,
    received_at TIMESTAMP WITHOUT TIME ZONE,
    started_at  TIMESTAMP WITHOUT TIME ZONE,
    finished_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_productions PRIMARY KEY (id)
);

CREATE TABLE productions_items (
    production_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (production_id, product_id),
    FOREIGN KEY (production_id) REFERENCES productions(id)
);