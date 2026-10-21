BEGIN;


CREATE TABLE categorias (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(60) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

CREATE TABLE fornecedores (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    cnpj VARCHAR(14) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(120)
);

CREATE TABLE usuarios (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE produtos (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(2000) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL CHECK (preco >= 1.00),
    categoria_id BIGINT NOT NULL REFERENCES categorias(id)
);

CREATE TABLE entradas (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fornecedor_id BIGINT NOT NULL REFERENCES fornecedores(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    data_entrada TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacao VARCHAR(255)
);

CREATE TABLE itens_entrada (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    entrada_id BIGINT NOT NULL REFERENCES entradas(id),
    produto_id BIGINT NOT NULL REFERENCES produtos(id),
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    custo_unitario DECIMAL(10, 2) NOT NULL CHECK (custo_unitario >= 0),
    UNIQUE (entrada_id, produto_id)
);

CREATE TABLE saidas (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    data_saida TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo VARCHAR(20) NOT NULL CHECK (motivo IN ('VENDA', 'PERDA', 'USO_INTERNO')),
    observacao VARCHAR(255)
);

CREATE TABLE itens_saida (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    saida_id BIGINT NOT NULL REFERENCES saidas(id),
    produto_id BIGINT NOT NULL REFERENCES produtos(id),
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    UNIQUE (saida_id, produto_id)
);


ALTER TABLE produtos ADD COLUMN estoque INTEGER NOT NULL DEFAULT 0 CHECK (estoque >= 0);

COMMIT;
