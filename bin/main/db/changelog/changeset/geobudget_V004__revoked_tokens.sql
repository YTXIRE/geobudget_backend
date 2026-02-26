CREATE TABLE revoked_tokens (
    token VARCHAR(512) PRIMARY KEY,
    revocation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_revoked_tokens_date ON revoked_tokens (revocation_date);
