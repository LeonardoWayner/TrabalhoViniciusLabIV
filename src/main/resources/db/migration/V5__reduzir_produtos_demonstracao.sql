UPDATE produtos p
SET estoque = p.estoque - inicial.quantidade
FROM (
    SELECT i.produto_id, SUM(i.quantidade) AS quantidade
    FROM itens_entrada i
    JOIN entradas e ON e.id = i.entrada_id
    JOIN produtos produto ON produto.id = i.produto_id
    WHERE e.observacao = 'Estoque inicial de demonstracao'
      AND produto.codigo IN ('001', '002', '003', '004', '005', '006', '007', '008')
    GROUP BY i.produto_id
) inicial
WHERE p.id = inicial.produto_id;

DELETE FROM itens_entrada i
USING entradas e, produtos p
WHERE i.entrada_id = e.id
  AND i.produto_id = p.id
  AND e.observacao = 'Estoque inicial de demonstracao'
  AND p.codigo IN ('001', '002', '003', '004', '005', '006', '007', '008');

DELETE FROM entradas e
WHERE e.observacao = 'Estoque inicial de demonstracao'
  AND NOT EXISTS (SELECT 1 FROM itens_entrada i WHERE i.entrada_id = e.id);

DELETE FROM produtos p
WHERE p.codigo IN ('002', '003', '005', '006', '007', '008')
  AND NOT EXISTS (SELECT 1 FROM itens_entrada i WHERE i.produto_id = p.id)
  AND NOT EXISTS (SELECT 1 FROM itens_saida i WHERE i.produto_id = p.id);
