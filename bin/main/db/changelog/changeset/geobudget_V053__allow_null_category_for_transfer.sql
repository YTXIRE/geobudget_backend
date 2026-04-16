-- liquibase formatted sql
-- changeset geobudget:V053

ALTER TABLE transactions ALTER COLUMN category_id DROP NOT NULL;

ALTER TABLE transactions DROP CONSTRAINT IF EXISTS chk_transactions_type;
ALTER TABLE transactions ADD CONSTRAINT chk_transactions_type CHECK (type IN ('income', 'expense', 'transfer_to_goal'));

ALTER TABLE transactions DROP CONSTRAINT IF EXISTS chk_expense_category_required;
ALTER TABLE transactions ADD CONSTRAINT chk_expense_category_required CHECK (
    type = 'income' 
    OR type = 'transfer_to_goal'
    OR category_id IS NOT NULL
);
