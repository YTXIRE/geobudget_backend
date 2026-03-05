-- liquibase formatted sql
-- changeset geobudget:V022

-- Indexes for foreign keys
CREATE INDEX IF NOT EXISTS idx_users_country_id ON users(country_id);
CREATE INDEX IF NOT EXISTS idx_okveds_category_id ON okveds(category_id);
CREATE INDEX IF NOT EXISTS idx_receipt_category_id ON receipt(category_id);
CREATE INDEX IF NOT EXISTS idx_receipt_item_receipt_id ON receipt_item(receipt_id);
CREATE INDEX IF NOT EXISTS idx_categories_group_id ON categories(group_id);
CREATE INDEX IF NOT EXISTS idx_categories_user_id ON categories(user_id);
CREATE INDEX IF NOT EXISTS idx_groups_user_id ON groups(user_id);
CREATE INDEX IF NOT EXISTS idx_confirmation_tokens_user_id ON confirmation_tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_password_reset_tokens_user_id ON password_reset_tokens(user_id);

-- Additional performance indexes
CREATE INDEX IF NOT EXISTS idx_receipt_time_of_purchase ON receipt(time_of_purchase);
CREATE INDEX IF NOT EXISTS idx_receipt_timestamp ON receipt(timestamp);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);