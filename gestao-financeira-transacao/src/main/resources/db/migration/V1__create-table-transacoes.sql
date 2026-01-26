CREATE TABLE transacoes (
        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        tipo_transacao VARCHAR(50) NOT NULL,
        valor NUMERIC(15,2) NOT NULL,
        ativo BOOLEAN NOT NULL DEFAULT TRUE,
        data_transacao TIMESTAMP NOT NULL
);
