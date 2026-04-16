-- V048: Fix duplicate system groups
-- Problem 1: V020 inserted duplicate "Образование" group
-- Problem 2: V045 created "Еда и продукты" but old "Еда" group from V020 still exists
-- Solution: Update categories to reference the correct group, then delete duplicates

-- Step 1: Update "Еда" categories to point to "Еда и продукты"
UPDATE categories c
SET group_id = (
    SELECT id FROM groups g
    WHERE g.name = 'Еда и продукты' AND g.user_id IS NULL
    LIMIT 1
)
WHERE c.group_id = (
    SELECT id FROM groups g
    WHERE g.name = 'Еда' AND g.user_id IS NULL
    LIMIT 1
);

-- Step 2: Delete "Еда" group after categories are updated
DELETE FROM groups
WHERE name = 'Еда' AND user_id IS NULL;

-- Step 3: Update categories that reference duplicate "Образование" to point to the first one
UPDATE categories c
SET group_id = (
    SELECT MIN(g.id)
    FROM groups g
    WHERE g.name = 'Образование' AND g.user_id IS NULL
)
WHERE c.group_id IN (
    SELECT id FROM groups
    WHERE name = 'Образование' AND user_id IS NULL
)
AND c.group_id != (
    SELECT MIN(id) FROM groups WHERE name = 'Образование' AND user_id IS NULL
);

-- Step 4: Delete duplicate "Образование" groups (keep only the one with smallest id)
DELETE FROM groups
WHERE name IN (
    SELECT name FROM groups
    WHERE user_id IS NULL AND is_system = TRUE
    GROUP BY name
    HAVING COUNT(*) > 1
)
AND id NOT IN (
    SELECT MIN(id) FROM groups
    WHERE user_id IS NULL AND is_system = TRUE
    GROUP BY name
    HAVING COUNT(*) > 1
);
