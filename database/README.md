# Banco de dados da FlaStore

O banco possui **8 tabelas de domínio**, todas mapeadas por entidades JPA e utilizadas pela aplicação.

| Tabela | Dados e relacionamentos |
| --- | --- |
| categorias | Nome e descrição; uma categoria pode classificar vários produtos. |
| fornecedores | Nome, CNPJ, telefone e e-mail; fornece as entradas. |
| usuarios | Nome, e-mail único, hash de senha e ativo; identifica o responsável. |
| produtos | Código único, nome, descrição, preço, categoria e saldo atual. |
| entradas | Fornecedor, usuário, data e observação do recebimento. |
| itens_entrada | Entrada, produto, quantidade e custo unitário. |
| saidas | Usuário, data, motivo e observação da retirada. |
| itens_saida | Saída, produto e quantidade. |

## Qual SQL executar?

Na aplicação, crie apenas o banco vazio. O **Flyway** executa automaticamente:

1. `V1__criar_tabelas.sql`: cria as oito tabelas e restrições.
2. `V2__adicionar_saldo_estoque.sql`: acrescenta o saldo em produtos.
3. `V3__dados_demonstracao.sql`: insere categorias, fornecedores, usuário, produtos e entrada correspondente ao saldo inicial.

Esses arquivos estão em `src/main/resources/db/migration/`. Depois de aplicada, uma migration não deve ser editada: crie uma nova versão.

O arquivo `ddl.sql` consolida a estrutura de V1 e V2 para estudo e execução manual em **outro banco vazio**. Não inclui os dados de V3. Não o execute no banco gerenciado pelo Flyway: haveria tabelas já existentes sem o histórico de migrations.

O Flyway também mantém sua tabela técnica `flyway_schema_history`. Ela não é uma nona entidade de negócio.

## Tipos e integridade

- IDs e FKs usam `BIGINT`, correspondendo a `Long` em Java.
- Quantidades e saldo usam `INTEGER`, correspondendo a `Integer`.
- Valores monetários usam `DECIMAL(10,2)` e `BigDecimal`.
- IDs são gerados por `GENERATED ALWAYS AS IDENTITY`.
- `NOT NULL`, `UNIQUE`, `CHECK` e chaves estrangeiras protegem os dados.
- Não há exclusão em cascata. O histórico impede remover produtos referenciados.
- O par entrada/produto e o par saída/produto são únicos em seus respectivos itens.

## Saldo e movimentações

A versão integrada mantém `produtos.estoque` como saldo atual, em vez de recalcular o histórico a cada consulta. A regra continua sendo:

```text
saldo = total recebido - total retirado
```

O cadastro começa em zero. A aplicação atualiza o saldo e grava cabeçalho/item na **mesma transação**. A busca com bloqueio de escrita no produto serializa movimentações concorrentes daquele produto. A tela de edição não recebe alterações de saldo.

No `EstoqueService`, blocos `if` conferem se o produto e o usuário existem e se a operação é permitida. Na entrada, também são conferidos o fornecedor e o limite do saldo. Na saída, a quantidade não pode ser maior que o estoque. Se houver erro, o método devolve uma mensagem antes de gravar a movimentação.

A integridade entre o saldo e a soma do histórico depende do uso do Service: não há trigger que faça essa conferência. Alterações manuais no banco podem quebrar a correspondência.

A interface registra um item por operação para simplificar o trabalho. As tabelas de itens permitem representar vários produtos em um recebimento, como ocorre na entrada inicial demonstrativa.

## Consultas para apresentar

```sql
SELECT p.id, p.codigo, p.nome, c.nome AS categoria, p.estoque
FROM produtos p
JOIN categorias c ON c.id = p.categoria_id
ORDER BY p.id;

SELECT e.id, e.data_entrada, f.nome AS fornecedor, p.nome AS produto,
       i.quantidade, i.custo_unitario
FROM entradas e
JOIN fornecedores f ON f.id = e.fornecedor_id
JOIN itens_entrada i ON i.entrada_id = e.id
JOIN produtos p ON p.id = i.produto_id;

SELECT s.id, s.data_saida, s.motivo, p.nome AS produto, i.quantidade
FROM saidas s
JOIN itens_saida i ON i.saida_id = s.id
JOIN produtos p ON p.id = i.produto_id;

-- Cada soma e feita separadamente para nao multiplicar linhas dos dois historicos.
SELECT p.codigo, p.estoque,
       COALESCE((SELECT SUM(i.quantidade) FROM itens_entrada i WHERE i.produto_id = p.id), 0)
       - COALESCE((SELECT SUM(i.quantidade) FROM itens_saida i WHERE i.produto_id = p.id), 0)
       AS saldo_pelo_historico
FROM produtos p;
```
