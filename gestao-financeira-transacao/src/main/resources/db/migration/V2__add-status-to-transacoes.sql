ALTER TABLE transacoes ADD COLUMN status VARCHAR(20);
UPDATE transacoes SET status = 'APPROVED' WHERE status IS NULL;
ALTER TABLE transacoes ALTER COLUMN status SET NOT NULL;
