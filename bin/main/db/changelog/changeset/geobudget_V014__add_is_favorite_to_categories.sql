-- Add is_favorite to categories
ALTER TABLE categories ADD COLUMN is_favorite BOOLEAN DEFAULT FALSE;
