-- Saldo atual mantido junto com o historico pela mesma transacao.
ALTER TABLE produtos ADD COLUMN estoque INTEGER NOT NULL DEFAULT 0 CHECK (estoque >= 0);
