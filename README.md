# FlaStore Controle

## Integrantes

- Leonardo Wayner
- Lucas Lessa

## Proposta

Trabalho acadêmico de controle de estoque de uma loja fictícia de artigos rubro-negros, baseado nas aulas 6 a 16 de Laboratório de Programação.

Possui login, painel, CRUD de produtos, registro de entradas e saídas e consulta do histórico. Produtos novos começam com saldo zero. Saídas acima do saldo são recusadas, e produtos com movimentações não podem ser excluídos. Categorias, fornecedores e o usuário demonstrativo vêm cadastrados pelas migrations, sem telas administrativas próprias.

São oito tabelas: categorias, fornecedores, usuarios, produtos, entradas, itens_entrada, saidas e itens_saida.

## Tecnologias

Java 17, Maven, Spring Boot 3.5.16, Spring MVC, Thymeleaf, Bean Validation, Spring Data JPA, Hibernate, PostgreSQL e Flyway. O módulo spring-security-crypto fornece BCrypt para conferir a senha. Não há testes automatizados nem H2 na versão atual.

## Preparar o banco

Instale PostgreSQL e crie um banco vazio chamado `flastore` pelo pgAdmin ou com:

```sql
CREATE DATABASE flastore;
```

Não execute `database/ddl.sql` nesse banco: o Flyway cria as tabelas automaticamente. O DDL consolidado é material de consulta ou execução manual em outro banco vazio.

## Executar no Windows

Abra o PowerShell na pasta do projeto. Ajuste os caminhos se estiver em outro computador:

```powershell
cd C:\Users\LeoWa\TrabalhoViniciusLabIV
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'
$env:DB_URL = 'jdbc:postgresql://localhost:5432/flastore'
$env:DB_USER = 'postgres'
$env:DB_PASSWORD = Read-Host 'Digite a senha do PostgreSQL'
.\mvnw.cmd spring-boot:run
```

Digite a senha da instalação do PostgreSQL quando for solicitado, sem aspas. Essas variáveis valem para esse terminal. A primeira execução requer internet para obter dependências.

Espere `Started FlaStoreApplication` e abra **http://localhost:8080**. Entre com:

- E-mail: `adm@teste.com`
- Senha: `teste`

Mantenha o terminal aberto. Para encerrar, pressione Ctrl+C. Os dados permanecem no PostgreSQL depois de reiniciar. No Linux/macOS, use `sh mvnw spring-boot:run`, configurando antes as variáveis de conexão no respectivo shell.

## Rotas

| Método | Rota | Função |
| --- | --- | --- |
| GET / POST | `/login` | Mostrar login / autenticar |
| POST | `/sair` | Encerrar sessão |
| GET | `/` | Painel |
| GET | `/produtos` | Listar produtos |
| GET | `/produtos/novo` | Abrir cadastro |
| POST | `/produtos` | Cadastrar produto |
| GET | `/produtos/{id}` | Detalhes e formulário de edição |
| POST | `/produtos/{id}/editar` | Atualizar cadastro |
| POST | `/produtos/{id}/excluir` | Excluir produto sem histórico |
| GET / POST | `/entradas` | Consultar / registrar recebimento |
| GET / POST | `/saidas` | Consultar / registrar retirada |

Os IDs das rotas são gerados pelo banco. O código comercial, como `001`, é um campo separado.

## Organização

O código Java fica em `src/main/java/br/com/flastore/`, dividido em controller, service, repository, model, form e config. As páginas ficam em `src/main/resources/templates/`; CSS, JavaScript e imagem ficam em `static/`. As migrations ficam em `db/migration/`: V1 cria tabelas, V2 acrescenta saldo e V3 insere os dados demonstrativos.

Os arquivos HTML, CSS e JavaScript da raiz são referências visuais de etapas anteriores. A aplicação usa os arquivos de `src/main/resources`.

O painel percorre os produtos com um `for` para contar os esgotados e reunir seus nomes. As decisões de cadastro, exclusão e login usam blocos `if`. No JavaScript, o menu e o filtro da tabela usam `for...of` e funções declaradas com `function` nos eventos.

As validações, as transações e o bloqueio do produto durante alterações de estoque continuam nos Services e no Repository.

## Conferência manual

Cadastre um produto, registre entrada de 10 unidades e saída de 3. Confira saldo 7 e tente retirar 8 para demonstrar a rejeição. Tente excluir o produto com histórico. Cadastre outro sem movimentos e exclua esse segundo. Reinicie a aplicação e confira a persistência dos dados.

Para gerar o JAR:

```powershell
.\mvnw.cmd package
java -jar target/flastore-1.0.0.jar
```

## Material de estudo

- [Como o projeto funciona — conteúdos dos slides](docs/GUIA_DOS_SLIDES.md): explicação simples dos assuntos das aulas 6 a 16 aplicados ao FlaStore.
- [Explicação do banco](database/README.md).
