ALTER TABLE usuarios
    ADD COLUMN senha VARCHAR(255);

UPDATE usuarios SET senha = 'TEMPORARIO';

ALTER TABLE usuarios
    ALTER COLUMN senha SET NOT NULL;
