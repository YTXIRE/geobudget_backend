-- liquibase formatted sql
-- changeset geobudget:V021
ALTER TABLE categories ADD COLUMN group_id BIGINT;

-- Map existing group_name values to group_id
UPDATE categories c SET group_id = g.id
FROM groups g
WHERE c.group_name = g.name AND g.is_system = TRUE;

ALTER TABLE categories ADD CONSTRAINT fk_categories_group_id FOREIGN KEY (group_id) REFERENCES groups(id);

DROP INDEX IF EXISTS idx_categories_group_name;

ALTER TABLE categories DROP COLUMN group_name;
