ALTER TABLE transactions
    ADD COLUMN IF NOT EXISTS receipt_id BIGINT;

ALTER TABLE transactions
    DROP CONSTRAINT IF EXISTS fk_transactions_receipt;

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_receipt
        FOREIGN KEY (receipt_id) REFERENCES receipt(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_transactions_receipt_id ON transactions(receipt_id);
