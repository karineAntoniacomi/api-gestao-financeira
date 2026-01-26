ALTER TABLE usuarios
    ADD COLUMN logradouro VARCHAR(100),
    ADD COLUMN bairro VARCHAR(100),
    ADD COLUMN cep VARCHAR(9),
    ADD COLUMN cidade VARCHAR(100),
    ADD COLUMN uf CHAR(2),
    ADD COLUMN complemento VARCHAR(100),
    ADD COLUMN numero VARCHAR(20);

-- Atualiza registros existentes
UPDATE usuarios
SET
    logradouro = 'NÃO INFORMADO',
    bairro = 'NÃO INFORMADO',
    cep = '00000-000',
    cidade = 'NÃO INFORMADO',
    uf = 'NA';

-- Agora aplica NOT NULL
ALTER TABLE usuarios
    ALTER COLUMN logradouro SET NOT NULL,
ALTER COLUMN bairro SET NOT NULL,
    ALTER COLUMN cep SET NOT NULL,
    ALTER COLUMN cidade SET NOT NULL,
    ALTER COLUMN uf SET NOT NULL;

