ALTER TABLE subscriptions
    ADD COLUMN category_id BIGINT;

ALTER TABLE subscriptions
    ADD CONSTRAINT fk_subscriptions_category
        FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL;

CREATE INDEX idx_subscriptions_category_id ON subscriptions(category_id);
