CREATE TABLE receipt (
    id SERIAL PRIMARY KEY,
    company_name VARCHAR(255),
    time_of_purchase TIMESTAMP,
    total_sum NUMERIC(10,2),
    region VARCHAR(255),
    inn VARCHAR(12),
    shop_address VARCHAR(255),
    timestamp BIGINT,
    category_id INT REFERENCES categories(id)
);

CREATE TABLE receipt_item (
    id SERIAL PRIMARY KEY,
    receipt_id INT REFERENCES receipt(id) ON DELETE CASCADE,
    name VARCHAR(255),
    quantity NUMERIC(10,3),
    price NUMERIC(10,2),
    amount NUMERIC(10,2)
);
