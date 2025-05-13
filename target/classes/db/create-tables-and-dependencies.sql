CREATE TABLE IF NOT EXISTS pet (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    pet_name VARCHAR(50),
    pet_type VARCHAR(50) NOT NULL,
    birthdate DATE NOT NULL,
    pet_owner VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS order_ (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    customer_name VARCHAR(50) NOT NULL,
    order_date DATE NOT NULL,
    status VARCHAR(50) DEFAULT ('CREATED'),
    FOREIGN KEY (pet_id) REFERENCES pet(id)
);
