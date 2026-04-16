-- liquibase formatted sql
-- changeset geobudget:V054

CREATE TABLE tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    color VARCHAR(7),
    icon VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tags_user_id ON tags(user_id);

CREATE TABLE tag_transactions (
    id BIGSERIAL PRIMARY KEY,
    tag_id BIGINT NOT NULL,
    transaction_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tag_transactions_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
    CONSTRAINT fk_tag_transactions_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
);

CREATE INDEX idx_tag_transactions_tag_id ON tag_transactions(tag_id);
CREATE INDEX idx_tag_transactions_transaction_id ON tag_transactions(transaction_id);
CREATE INDEX idx_tag_transactions_user_id ON tag_transactions(user_id);
