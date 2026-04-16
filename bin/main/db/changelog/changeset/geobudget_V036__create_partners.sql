-- liquibase formatted sql
-- changeset geobudget:V036

CREATE TABLE IF NOT EXISTS partners (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    partner_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_partners_user_partner UNIQUE (user_id, partner_id),
    CONSTRAINT fk_partners_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_partners_partner FOREIGN KEY (partner_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_partners_status CHECK (status IN ('pending', 'accepted', 'rejected')),
    CONSTRAINT chk_partners_different CHECK (user_id != partner_id)
);

CREATE INDEX IF NOT EXISTS idx_partners_user_id ON partners(user_id);
CREATE INDEX IF NOT EXISTS idx_partners_partner_id ON partners(partner_id);
CREATE INDEX IF NOT EXISTS idx_partners_status ON partners(status);
CREATE INDEX IF NOT EXISTS idx_partners_user_status ON partners(user_id, status);
