-- liquibase formatted sql
-- changeset geobudget:V033

CREATE TABLE IF NOT EXISTS receipt_category_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    inn VARCHAR(32) NOT NULL,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_receipt_category_preferences_category
        FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT uq_receipt_category_preferences_user_inn UNIQUE (user_id, inn)
);
