CREATE TABLE produto (
 id BIGSERIAL PRIMARY KEY,
 codigo_interno VARCHAR(80) UNIQUE,
 ean VARCHAR(14) UNIQUE,
 nome VARCHAR(200) NOT NULL,
 principio_ativo VARCHAR(200),
 concentracao VARCHAR(100),
 apresentacao VARCHAR(200) NOT NULL,
 fabricante VARCHAR(150),
 registro_ms VARCHAR(20),
 sujeito_prescricao BOOLEAN NOT NULL DEFAULT FALSE,
 publicavel BOOLEAN NOT NULL DEFAULT FALSE,
 ativo BOOLEAN NOT NULL DEFAULT TRUE,
 criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
 CONSTRAINT produto_identificacao CHECK (codigo_interno IS NOT NULL OR ean IS NOT NULL)
);
CREATE TABLE preco (
 id BIGSERIAL PRIMARY KEY,
 produto_id BIGINT NOT NULL REFERENCES produto(id),
 valor NUMERIC(12,2) NOT NULL CHECK (valor >= 0),
 registrado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_preco_produto_data ON preco(produto_id, registrado_em DESC, id DESC);
CREATE TABLE promocao (
 id BIGSERIAL PRIMARY KEY,
 produto_id BIGINT NOT NULL REFERENCES produto(id),
 preco_promocional NUMERIC(12,2) NOT NULL CHECK (preco_promocional >= 0),
 inicio TIMESTAMPTZ NOT NULL,
 fim TIMESTAMPTZ NOT NULL,
 ativa BOOLEAN NOT NULL DEFAULT TRUE,
 CHECK (fim > inicio)
);
CREATE INDEX idx_promocao_produto ON promocao(produto_id, inicio, fim);
CREATE TABLE estoque (
 produto_id BIGINT PRIMARY KEY REFERENCES produto(id),
 quantidade INTEGER CHECK (quantidade >= 0),
 atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE TABLE importacao (
 id BIGSERIAL PRIMARY KEY,
 nome_arquivo VARCHAR(255) NOT NULL,
 status VARCHAR(30) NOT NULL,
 total_linhas INTEGER NOT NULL DEFAULT 0,
 importadas INTEGER NOT NULL DEFAULT 0,
 rejeitadas INTEGER NOT NULL DEFAULT 0,
 iniciada_em TIMESTAMPTZ NOT NULL DEFAULT now(),
 finalizada_em TIMESTAMPTZ
);
CREATE TABLE importacao_erro (
 id BIGSERIAL PRIMARY KEY,
 importacao_id BIGINT NOT NULL REFERENCES importacao(id),
 numero_linha INTEGER NOT NULL,
 motivo TEXT NOT NULL
);
