CREATE TABLE inventory (
                           id BIGSERIAL PRIMARY KEY,
                           product VARCHAR(255) NOT NULL,
                           quantity INTEGER NOT NULL
);

INSERT INTO inventory (product, quantity) VALUES ('Laptop', 100);
INSERT INTO inventory (product, quantity) VALUES ('Phone', 200);
INSERT INTO inventory (product, quantity) VALUES ('Tablet', 150);