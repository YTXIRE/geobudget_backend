-- Add type column and set system type for existing categories
ALTER TABLE categories ADD COLUMN type VARCHAR(20) DEFAULT 'system';
UPDATE categories SET type = 'system' WHERE type IS NULL OR type = '';
