CREATE TABLE dead_letter_transacao
(
    id UUID PRIMARY KEY,
    transacao_id BIGINT NOT NULL,
    usuario_id BIGINT,
    payload JSONB NOT NULL,
    erro TEXT NOT NULL,
    stacktrace TEXT,
    tipo_erro VARCHAR(255),
    tentativas INTEGER,
    status VARCHAR(50),
    topico_origem VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    last_attempt_at TIMESTAMP
);