# FlaStore Controle

## Integrantes

- Leonardo Wayner
- Lucas Lessa

## Descrição

Sistema de controle de produtos de uma loja fictícia de artigos rubro-negros. Integração do protótipo em HTML, CSS e JavaScript com Java, Spring Boot e Thymeleaf, preservando as cinco telas e sua identidade visual.

Possui login demonstrativo, dashboard com totais dinâmicos, listagem com busca, cadastro e detalhes com edição. Os oito produtos iniciais são criados no Java. Os dados ficam em uma lista em memória e voltam ao estado inicial quando o servidor reinicia. Não utiliza banco de dados.

## Tecnologias

Java 17, Spring Boot 3.5.16, Spring Web, Thymeleaf, Bean Validation, HTML, CSS, JavaScript e Maven. Testes de integração com JUnit e MockMvc.

## Como executar

Instale um JDK 17 ou superior e configure `JAVA_HOME` para a pasta do JDK. A primeira execução precisa de internet para baixar as dependências. O Maven Wrapper está incluído, dispensando a instalação separada do Maven.

No PowerShell, dentro da pasta do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:

```sh
sh mvnw spring-boot:run
```

Abra **http://localhost:8080** no navegador e entre com:

- E-mail: `adm@teste.com`
- Senha: `teste`

O login é demonstrativo, com credenciais fixas e sessão HTTP. Não utiliza Spring Security ou autenticação de produção. Encerre o servidor com `Ctrl+C`. As páginas devem ser acessadas pelas rotas, sem abrir arquivos HTML diretamente.

Para gerar e executar o JAR:

```powershell
.\mvnw.cmd clean package
java -jar target/flastore-1.0.0.jar
```

## Principais rotas

| Método | Rota | Função |
| --- | --- | --- |
| GET | `/login` | Tela de entrada |
| POST | `/login` | Valida o acesso demonstrativo |
| POST | `/sair` | Encerra a sessão |
| GET | `/` | Dashboard com totais enviados pelo Java |
| GET | `/produtos` | Lista os produtos |
| GET | `/produtos/novo` | Formulário de cadastro |
| POST | `/produtos` | Valida e cadastra em memória |
| GET | `/produtos/{codigo}` | Detalhes e formulário de edição |
| POST | `/produtos/{codigo}` | Valida e atualiza o produto |

Exemplo: `/produtos/001`. Código inexistente retorna HTTP 404.

## Estrutura

```text
src/main/java/br/com/flastore/
  FlaStoreApplication.java
  config/WebConfig.java
  controller/LoginController.java
  controller/ProdutoController.java
  model/Produto.java
  service/ProdutoService.java
src/main/resources/
  application.properties
  templates/                 # Cinco telas com Thymeleaf
  static/css/                # Estilos reaproveitados
  static/js/                 # Busca e marcação da página atual
  static/img/                # Imagens originais
src/test/java/br/com/flastore/FluxoAplicacaoTest.java
prototipo-anterior/           # Etapa anterior, apenas para referência
pom.xml
mvnw
mvnw.cmd
.mvn/wrapper/
```

## Funcionamento da integração

O `ProdutoController` recebe requisições, consulta o `ProdutoService` e envia objetos pelo `Model`. As páginas usam `th:text`, `th:each`, `th:href`, `th:action`, `th:object` e `th:field`. Cadastro e edição enviam POST para o backend e redirecionam após salvar, evitando duplicação ao atualizar a página.

Os campos são validados no servidor. Preço mínimo: R$ 1,00. Estoque: inteiro não negativo. A situação é calculada pelo estoque: zero = esgotado; de 1 a 5 = baixo estoque; acima de 5 = disponível. O JavaScript reaproveita a busca na tabela. O seletor de loja permanece ilustrativo, como no protótipo: o estoque demonstrativo é único.

## Testes

```powershell
.\mvnw.cmd test
```

Os testes verificam login, saída, proteção das rotas, telas e arquivos estáticos, cadastro, edição, rejeição de dados inválidos e produto inexistente.

Roteiro manual: entre, confira o dashboard, busque um produto, cadastre um item, abra seus detalhes, altere o estoque para zero e confira novamente o dashboard. Saia pelo botão Sair.

## Entrega

Publique o projeto em um repositório GitHub e envie o link na atividade. Inclua `src`, `pom.xml`, `README.md`, `mvnw`, `mvnw.cmd` e `.mvn/wrapper`. As pastas `.tools` e `target` são locais e estão ignoradas pelo Git.

Referência: [requisitos oficiais do Spring Boot 3.5](https://docs.spring.io/spring-boot/3.5/system-requirements.html).
