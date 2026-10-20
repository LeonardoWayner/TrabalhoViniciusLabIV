-- Dados ficticios para a apresentacao academica. Senha demonstrativa: teste.
INSERT INTO categorias (nome, descricao) VALUES
('Camisas', 'Camisas e vestuario'), ('Acessórios', 'Acessorios de torcida'),
('Casa', 'Artigos para casa'), ('Kits', 'Conjuntos para presente');

INSERT INTO fornecedores (nome, telefone, email) VALUES
('Distribuidora Rubro-Negra', '85999990001', 'contato@rubro-negra.example'),
('Artigos da Torcida', '85999990002', 'contato@torcida.example');

INSERT INTO usuarios (nome, email, senha_hash, ativo) VALUES
('Administrador', 'adm@teste.com', '$2a$10$UxN.4CvlS9HWdC3IrIlw.ek6R1oCWwF1B8dns.lb.eDtZ4B91Z53.', TRUE);

INSERT INTO produtos (codigo, nome, descricao, preco, categoria_id, estoque) VALUES
('001', 'Camisa Rubro-Negra', 'Modelo listrado em tecido leve.', 189.90, (SELECT id FROM categorias WHERE nome = 'Camisas'), 24),
('002', 'Jaqueta Arquibancada', 'Jaqueta preta com detalhe vermelho.', 249.90, (SELECT id FROM categorias WHERE nome = 'Camisas'), 8),
('003', 'Cachecol Torcida', 'Cachecol com faixas vermelhas e pretas.', 79.90, (SELECT id FROM categorias WHERE nome = 'Acessórios'), 5),
('004', 'Caneca Listrada', 'Caneca de ceramica para cafe.', 49.90, (SELECT id FROM categorias WHERE nome = 'Casa'), 18),
('005', 'Boné Nação', 'Bone com aba curva e ajuste traseiro.', 69.90, (SELECT id FROM categorias WHERE nome = 'Acessórios'), 3),
('006', 'Kit Presente', 'Caixa com camisa, caneca e cachecol.', 299.90, (SELECT id FROM categorias WHERE nome = 'Kits'), 11),
('007', 'Garrafa de Treino', 'Garrafa esportiva rubro-negra.', 59.90, (SELECT id FROM categorias WHERE nome = 'Acessórios'), 20),
('008', 'Camisa Retrô', 'Peca de algodao com listras classicas.', 219.90, (SELECT id FROM categorias WHERE nome = 'Camisas'), 0);

-- Todo saldo inicial tem uma entrada correspondente, evitando historico incompleto.
INSERT INTO entradas (fornecedor_id, usuario_id, observacao)
SELECT f.id, u.id, 'Estoque inicial de demonstracao'
FROM fornecedores f, usuarios u
WHERE f.nome = 'Distribuidora Rubro-Negra' AND u.email = 'adm@teste.com';

INSERT INTO itens_entrada (entrada_id, produto_id, quantidade, custo_unitario)
SELECT e.id, p.id, p.estoque, 0.00
FROM entradas e, produtos p
WHERE e.observacao = 'Estoque inicial de demonstracao' AND p.estoque > 0;
