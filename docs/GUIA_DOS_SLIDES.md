# Como o FlaStore funciona — conteúdos das aulas 6 a 16

Este guia explica o projeto com os assuntos apresentados nos slides. Não é uma explicação linha por linha: a ideia é entender o papel de cada parte e como elas trabalham juntas.

## Primeiro: qual é a função de cada parte?

O FlaStore permite cadastrar produtos e registrar entradas e saídas do estoque. O usuário interage com páginas no navegador, mas as regras e o acesso ao banco são executados pela aplicação Java.

| Parte | Explicação simples |
| --- | --- |
| HTML | Define a estrutura da página: títulos, campos, tabelas e botões. |
| CSS | Define a aparência: cores, tamanhos e espaçamentos. |
| JavaScript | Acrescenta pequenas interações no navegador. |
| Spring Boot | Inicia e configura a aplicação Java que atende os pedidos do navegador. |
| Controller | Recebe um pedido e organiza a resposta da aplicação. |
| Thymeleaf | Preenche as páginas HTML com os dados enviados pelo Java. |
| Entidade | Classe Java que representa dados guardados em uma tabela. |
| Service | Executa as operações e aplica as regras do sistema. |
| Repository | Oferece operações para salvar, buscar e excluir dados. |
| PostgreSQL | É o banco que armazena os registros. |
| Flyway | Cria e evolui a estrutura do banco por arquivos SQL versionados. |

## Mapa das pastas e dos arquivos do projeto

O programa utilizado pelo Spring está dentro de `src/main`. Os arquivos de protótipo que ficaram fora dessa pasta não são as páginas usadas pela aplicação em execução.

### Pastas

| Pasta | Responsabilidade |
| --- | --- |
| `src/` | Reúne os arquivos-fonte da aplicação. |
| `src/main/java/br/com/flastore/` | Guarda as classes Java do sistema. `br.com.flastore` é o pacote principal, isto é, a organização lógica dessas classes. |
| `config/` | Configura comportamentos da aplicação Web. |
| `controller/` | Recebe os pedidos do navegador e prepara a resposta: página, dados ou redirecionamento. |
| `form/` | Define os campos recebidos nos formulários de entrada e saída. Esses objetos não são tabelas. |
| `model/` | Guarda as oito entidades que representam as tabelas de negócio. |
| `repository/` | Declara as interfaces de acesso aos registros de cada entidade. |
| `service/` | Executa as operações e aplica as regras de negócio. |
| `src/main/resources/` | Guarda configurações, páginas, recursos visuais e SQL de inicialização. |
| `templates/` | Guarda as páginas HTML que o Thymeleaf preenche com dados. |
| `static/css/` | Guarda os estilos da aplicação. |
| `static/js/` | Guarda o JavaScript executado no navegador. |
| `static/img/` | Guarda as imagens apresentadas nas páginas. |
| `db/migration/` | Guarda os scripts SQL aplicados em ordem pelo Flyway. |
| `.mvn/wrapper/` | Guarda a configuração usada pelos scripts de execução do Maven. |
| `database/` | Guarda o DDL consolidado para consulta e a explicação do banco. Não é a pasta que o Flyway lê ao iniciar. |
| `docs/` | Guarda este guia de estudo. Não participa da execução do site. |
| `assets/` | Guarda a imagem utilizada pelo protótipo da raiz. |
| `target/` | Recebe arquivos gerados pelo Maven, como classes compiladas e JAR. Não é onde devemos editar o código-fonte. |
| `.git/` | Guarda o histórico e as informações do controle de versão Git. Não faz parte das regras de estoque. |

As pastas `config`, `controller`, `form`, `model`, `repository` e `service` citadas acima ficam dentro do pacote Java. As pastas `templates`, `static` e `db/migration` ficam dentro de `resources`.

### Arquivos da raiz e de configuração

| Arquivo | Responsabilidade |
| --- | --- |
| `pom.xml` | Informa nome, versão, Java utilizado, bibliotecas e plugin Maven do Spring Boot. |
| `mvnw.cmd` | Inicia o Maven Wrapper no Windows, permitindo executar comandos sem instalar Maven separadamente. |
| `mvnw` | Faz o mesmo papel em sistemas compatíveis com shell, como Linux. |
| `.mvn/wrapper/maven-wrapper.properties` | Indica a distribuição Maven que os scripts devem obter e utilizar. |
| `.gitignore` | Indica arquivos locais que não devem ser versionados, como a pasta `target`. |
| `README.md` | Explica a proposta, a organização e a execução do projeto. |
| `database/ddl.sql` | Reúne a estrutura das oito tabelas em um SQL de consulta ou execução manual separada. O sistema utiliza as migrations, não esse arquivo. |
| `database/README.md` | Explica as tabelas, as regras e consultas de conferência do banco. |
| `docs/GUIA_DOS_SLIDES.md` | Relaciona os assuntos dos slides ao funcionamento do nosso projeto. |

### Arquivos Java: aplicação, configuração e Controllers

Todos os caminhos desta tabela começam em `src/main/java/br/com/flastore/`.

| Arquivo | Responsabilidade |
| --- | --- |
| `FlaStoreApplication.java` | Contém `main`, o ponto de partida que inicia o Spring Boot. |
| `config/WebConfig.java` | Configura a verificação de sessão antes das rotas protegidas. Se não houver login válido, redireciona para a tela de entrada. |
| `controller/LoginController.java` | Mostra o login, recebe e-mail/senha, pede autenticação ao Service e inicia ou encerra a sessão. |
| `controller/ProdutoController.java` | Atende painel, lista, cadastro, detalhes, edição e exclusão de produtos. Envia dados ao HTML e interpreta os erros dos formulários. |
| `controller/EstoqueController.java` | Mostra formulários e históricos de entradas/saídas. Recebe os dados, verifica a validação e chama o Service de estoque. |

### Arquivos Java: formulários e entidades

| Arquivo | Responsabilidade |
| --- | --- |
| `form/EntradaForm.java` | Recebe produto, fornecedor, quantidade, custo unitário e observação enviados no formulário de entrada. |
| `form/SaidaForm.java` | Recebe produto, quantidade, motivo e observação enviados no formulário de saída. |
| `model/Produto.java` | Representa a tabela de produtos: ID, código, nome, descrição, categoria, preço e saldo. Também calcula a situação exibida conforme o saldo. |
| `model/Categoria.java` | Representa uma classificação compartilhada por produtos, como Camisas ou Casa. |
| `model/Fornecedor.java` | Representa quem fornece os recebimentos, com nome e dados de contato. |
| `model/Usuario.java` | Representa o usuário que pode entrar no sistema e ser identificado como responsável pelas movimentações. |
| `model/Entrada.java` | Representa o cabeçalho de um recebimento: fornecedor, usuário, data e observação. |
| `model/ItemEntrada.java` | Representa um produto recebido naquela entrada, com quantidade e custo unitário. |
| `model/Saida.java` | Representa o cabeçalho de uma retirada: usuário, data, motivo e observação. |
| `model/ItemSaida.java` | Representa um produto retirado naquela saída e sua quantidade. |

**Por que existem Form e Entity?** O formulário de entrada precisa produzir registros em mais de uma tabela. `EntradaForm` representa o pedido recebido da página; `Entrada` e `ItemEntrada` representam os dados que serão persistidos. Não são a mesma responsabilidade.

### Arquivos Java: Repositories e Services

| Arquivo | Responsabilidade |
| --- | --- |
| `repository/ProdutoRepository.java` | Acessa produtos e declara as consultas de código duplicado e busca de produto para atualização. |
| `repository/CategoriaRepository.java` | Acessa as categorias usadas nos produtos e nos selects. |
| `repository/FornecedorRepository.java` | Acessa os fornecedores utilizados nas entradas. |
| `repository/UsuarioRepository.java` | Acessa usuários e permite procurá-los pelo e-mail. |
| `repository/EntradaRepository.java` | Acessa os cabeçalhos de recebimento. |
| `repository/ItemEntradaRepository.java` | Acessa os itens recebidos e verifica se um produto possui histórico de entrada. |
| `repository/SaidaRepository.java` | Acessa os cabeçalhos de retirada. |
| `repository/ItemSaidaRepository.java` | Acessa os itens retirados e verifica se um produto possui histórico de saída. |
| `service/ProdutoService.java` | Lista e busca produtos, verifica código/categoria e coordena cadastro, atualização e exclusão permitida. |
| `service/EstoqueService.java` | Consulta os históricos e executa entradas/saídas, conferindo referências e saldo disponível. |
| `service/UsuarioService.java` | Confere usuário, estado ativo e senha informada para a autenticação. |

Uma interface Repository pode oferecer métodos herdados que o sistema não expõe em tela. Ter `CategoriaRepository`, por exemplo, não significa que exista um CRUD visual de categorias.

### Arquivos de `src/main/resources/`

| Arquivo | Responsabilidade |
| --- | --- |
| `application.properties` | Define porta, codificação, conexão PostgreSQL e configurações de JPA/Thymeleaf. |
| `messages.properties` | Define mensagens legíveis quando um campo não pode ser convertido, como texto inválido para um número. |
| `db/migration/V1__criar_tabelas.sql` | Cria as oito tabelas, suas chaves e restrições. |
| `db/migration/V2__adicionar_saldo_estoque.sql` | Acrescenta a coluna de saldo à tabela produtos. |
| `db/migration/V3__dados_demonstracao.sql` | Insere categorias, fornecedores, usuário, produtos e a entrada correspondente ao saldo inicial. |
| `templates/login.html` | Mostra o formulário de acesso e a mensagem de falha no login. |
| `templates/index.html` | Mostra o painel com contadores e atalhos para movimentar estoque. |
| `templates/produtos.html` | Mostra a tabela de produtos, a busca visual e as ações de detalhes/edição e exclusão. |
| `templates/cadastro.html` | Mostra o formulário de cadastro de produto e seus erros. |
| `templates/detalhes.html` | Mostra as informações de um produto e seu formulário de edição. |
| `templates/entradas.html` | Mostra o formulário de recebimento e o histórico de itens recebidos. |
| `templates/saidas.html` | Mostra o formulário de retirada e o histórico de itens retirados. |
| `static/css/style.css` | Aplica cores, layout, estilos de tabelas, botões e destaques de mensagens. |
| `static/js/script.js` | Destaca o menu atual e prepara o filtro visual da tabela de produtos. |
| `static/img/produtos-rubro-negros.png` | Fornece a imagem apresentada no painel e na página de detalhes. |

### Arquivos antigos que não atendem às rotas atuais

| Arquivo ou grupo | Responsabilidade |
| --- | --- |
| `index.html`, `styles.css` e `script.js` da raiz | Formam um protótipo estático separado. Alterá-los não muda os templates utilizados pelo Spring Boot. |
| `assets/hero-produtos-rubro-negros.png` | Imagem desse protótipo da raiz. |

## Aula 6 — JavaScript no navegador

JavaScript permite reagir às ações do usuário sem precisar reconstruir a página inteira. No projeto, o arquivo utilizado é `src/main/resources/static/js/script.js`.

### O que o JavaScript faz no projeto

Quando a página fica pronta, ele identifica o link do menu correspondente à página atual e acrescenta a classe CSS `ativo`. Essa classe produz o destaque visual.

Na listagem de produtos, ele também prepara o campo de busca. Conforme você digita, o código lê o texto das linhas, esconde as que não correspondem à pesquisa e atualiza o contador de produtos visíveis.

Isso é um **filtro visual**: os produtos já foram carregados na página. O JavaScript não consulta novamente o PostgreSQL nessa busca.

### Termos dos slides

- **DOM:** representação dos elementos HTML que o JavaScript pode acessar.
- **`querySelector`:** encontra o primeiro elemento de um seletor, como `#filtroProduto`.
- **`querySelectorAll`:** encontra vários elementos, como todos os links do menu.
- **Evento:** algo que acontece na página. `input` ocorre quando o conteúdo de um campo muda; `submit` está relacionado ao envio de um formulário.
- **`addEventListener`:** associa uma função a um evento.
- **`classList`:** permite acrescentar, remover ou alternar classes CSS.
- **`textContent`:** lê ou altera o texto de um elemento.
- **`preventDefault`:** impede o comportamento padrão de um evento, como enviar um formulário inválido no exemplo da aula.
- **`confirm`:** pede confirmação e devolve verdadeiro ou falso. O projeto usa isso antes de enviar uma exclusão.

O slide mostra `defer` para executar o script depois da análise do HTML. O projeto atual carrega o script no final da página e também espera `DOMContentLoaded`, o evento que indica que a estrutura HTML está pronta.

**Por que JavaScript não é suficiente para validar?** Porque alguém pode enviar dados diretamente ao servidor, sem usar a página. O navegador ajuda o usuário; o backend também precisa conferir os dados, como ensina a aula 14.

O contador de caracteres é um exemplo opcional dos slides e não está implementado na aplicação atual. A restrição a frameworks da aula 6 pertence à primeira entrega visual; as aulas seguintes introduzem Spring Boot explicitamente.

### Exemplo real: uma linha aparece ou desaparece na busca

**Arquivo:** `src/main/resources/static/js/script.js`.

```javascript
for (const linha of linhas) {
  const texto = linha.textContent.toLowerCase();
  const deveMostrar = texto.includes(busca);

  linha.hidden = !deveMostrar;
  if (deveMostrar) {
    encontrados += 1;
  }
}
```

`for...of` percorre as linhas já existentes na tabela. A cada volta, `linha` representa uma delas. `textContent` lê o texto de cada uma. `toLowerCase` permite comparar sem distinguir letras maiúsculas. `includes` verifica se o texto contém a busca. A propriedade `hidden` oculta a linha quando ela não corresponde; o `!` inverte verdadeiro e falso. O contador aumenta somente para as linhas visíveis.

**Exemplo de uso:** digitando “caneca”, uma linha com “Caneca Listrada” continua visível. A quantidade de produtos no banco não muda; apenas a apresentação da tabela muda.

## Aula 7 — Spring Boot e estrutura do projeto

No protótipo, abrir um HTML no computador era suficiente para ver uma página estática. Na aplicação atual, o navegador pede uma página ao programa Java e recebe uma resposta.

O endereço `http://localhost:8080` aponta para a aplicação executada no próprio computador. `localhost` significa este computador, e `8080` é a porta usada pelo site.

**Spring** é o framework que oferece a infraestrutura. **Spring Boot** facilita configurar e executar uma aplicação Spring, incluindo o servidor incorporado.

### Onde ficam os arquivos

| Local | Conteúdo |
| --- | --- |
| `src/main/java` | Classes Java. |
| `src/main/resources/templates` | Páginas processadas pelo Thymeleaf. |
| `src/main/resources/static` | CSS, JavaScript e imagens. |
| `pom.xml` | Dependências e configuração Maven do projeto. |
| `application.properties` | Configurações da aplicação e da conexão com o banco. |

A classe `FlaStoreApplication` tem o método `main`, que chama `SpringApplication.run`. Esse é o ponto de entrada da aplicação.

No Maven, **Group** identifica o grupo do projeto e **Artifact** identifica o artefato. Um **JAR** é o pacote Java gerado para execução. Uma **dependência** é uma biblioteca utilizada pelo projeto, como Spring Web ou Thymeleaf.

**Por que separar `templates` e `static`?** Porque a página precisa ser preenchida com dados, enquanto o CSS, o JavaScript e as imagens são enviados como recursos estáticos.

**Por que não abrir o HTML diretamente?** Porque isso não executa o Controller, não consulta os dados e não processa o Thymeleaf.

### Exemplo real: iniciar a aplicação

**Arquivo:** `src/main/java/br/com/flastore/FlaStoreApplication.java`.

```java
@SpringBootApplication
public class FlaStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlaStoreApplication.class, args);
    }
}
```

`@SpringBootApplication` identifica a configuração principal. `main` é o método inicial do programa Java. `SpringApplication.run` pede ao Spring Boot que monte e inicie a aplicação. `args` permite receber argumentos do comando de execução.

**Por que isso fica separado dos Controllers?** Iniciar o sistema é uma responsabilidade; atender uma rota como `/produtos` é outra. O `main` não precisa conter a lógica de cada página.

## Aula 8 — Rotas, Controller, Model e Thymeleaf

Uma **rota** é um caminho da aplicação, como `/produtos`. Quando o navegador pede esse caminho, o Spring encontra o método responsável.

Este método está no arquivo `src/main/java/br/com/flastore/controller/ProdutoController.java`:

```java
@GetMapping("/produtos")
public String listar(Model model) {
    model.addAttribute("produtos", produtos.listar());
    return "produtos";
}
```

`@GetMapping` informa qual pedido esse método atende. `model.addAttribute` disponibiliza um valor para a página. O nome `produtos` permite que o HTML encontre a lista. O retorno `"produtos"` escolhe o arquivo `templates/produtos.html`.

O **Model** é esse conjunto de dados enviado para montar a página. Não é o banco de dados.

### Como os produtos aparecem no HTML

```html
<tr th:each="produto : ${produtos}">
    <td th:text="${produto.nome}"></td>
</tr>
```

`th:each` repete a linha para cada produto da lista. `produto` é o nome usado para o item atual. `th:text` mostra o nome daquele item.

**Por que usar Thymeleaf?** Para não escrever uma linha HTML manualmente para cada produto cadastrado. A estrutura da tabela fica no HTML e os dados vêm do Java.

**Por que `/produtos` não é `produtos.html`?** Porque o navegador pede uma rota; o Controller decide qual página responder. O endereço público e o nome do arquivo são coisas diferentes.

O navegador recebe o HTML já preenchido. Ele não executa o `th:each`.

### Exemplo real: a linha da tabela usa o objeto relacionado

**Arquivo:** `src/main/resources/templates/produtos.html`, trecho de dentro da linha repetida.

```html
<td th:text="${produto.codigo}"></td>
<td th:text="${produto.nome}"></td>
<td th:text="${produto.categoria.nome}"></td>
```

Cada célula lê uma informação do produto atual. Na terceira, primeiro acessamos a categoria associada e depois o nome dessa categoria. A tabela não contém esses nomes fixados manualmente: eles vêm dos objetos recebidos pelo Model.

## Aula 9 — Objetos e formulários

Um produto tem várias informações relacionadas: código, nome, categoria, preço e descrição. Por isso ele é representado por um **objeto**, e não por uma única String.

Uma **classe** define quais dados e métodos seus objetos possuem. `Produto` é a classe; uma caneca cadastrada é um produto concreto representado por um objeto dessa classe.

**Getter** é um método que lê uma propriedade, como `getNome`. **Setter** atribui um valor, como `setNome`. `new Produto()` cria um objeto na memória Java, mas não o salva sozinho no banco.

### Como funciona o cadastro

1. Um GET abre o formulário.
2. O Controller envia um objeto vazio e as categorias disponíveis.
3. O usuário preenche os campos.
4. O formulário envia um POST.
5. O Spring preenche um objeto Java com os valores recebidos.
6. A aplicação valida e executa o cadastro.

Esse preenchimento dos valores recebidos em propriedades Java é chamado de **binding**.

| Atributo Thymeleaf | Função |
| --- | --- |
| `th:object="${produto}"` | Escolhe o objeto usado pelo formulário. |
| `th:field="*{nome}"` | Liga o campo à propriedade nome desse objeto. |
| `th:action="@{/produtos}"` | Define a rota para a qual enviar os dados. |

**GET** é usado para abrir/consultar. **POST** é usado para enviar uma operação que altera dados. O mesmo caminho pode aceitar os dois com métodos diferentes.

### Por que usar redirect depois de salvar?

`return "cadastro"` devolve um template na mesma requisição. `return "redirect:/produtos"` manda o navegador fazer uma nova requisição para a listagem.

Depois de salvar, esse redirecionamento ajuda a evitar que atualizar a página final reenvie o formulário anterior.

Na etapa da aula 9, os registros ficam em uma `List` na memória e desaparecem quando o programa encerra. O projeto atual já evoluiu para o banco. Ainda usa listas para transportar resultados, mas o armazenamento definitivo é o PostgreSQL.

### Exemplo real: preparar o objeto do formulário

**Arquivo:** `controller/ProdutoController.java`, dentro do pacote Java do projeto.

```java
@GetMapping("/produtos/novo")
public String novo(Model model) {
    model.addAttribute("produto", new Produto());
    model.addAttribute("categorias", produtos.listarCategorias());
    return "cadastro";
}
```

O objeto novo deixa os campos prontos para receber valores. A lista de categorias prepara as opções do select. Retornar `cadastro` escolhe `cadastro.html`; esse método não salva um produto.

No arquivo `templates/cadastro.html`, o campo de nome usa:

```html
<input id="nomeProduto" th:field="*{nome}" th:errorclass="campo-erro" maxlength="120" name="nomeProduto" type="text" required />
```

Ele está dentro do formulário cujo `th:object` é `${produto}`. Por isso `*{nome}` se refere a `produto.nome`. `th:field` gera o nome efetivo para o binding e trabalha com o valor atual e possíveis erros. Ao enviar o formulário, o Spring usa o parâmetro correspondente para preencher o objeto Java.

## Aula 10 — Identificação e rotas dinâmicas

O **ID** identifica um registro específico. Dois produtos podem ter nomes iguais, mas seus IDs são diferentes.

No projeto, o ID interno também é diferente do código comercial. O código `001` é uma informação do cadastro; o ID é a identidade usada nas relações e nas rotas.

```java
@GetMapping("/produtos/{id}")
```

`{id}` é uma parte variável. Assim, `/produtos/1` e `/produtos/2` usam o mesmo método, recebendo números diferentes por `@PathVariable`.

**Por que não criar uma rota para cada número?** Porque a quantidade de produtos pode crescer. Uma rota parametrizada atende todos os IDs válidos.

**Por que um único HTML de detalhes?** Porque a estrutura é a mesma; o objeto enviado para preencher a página é que muda.

O `th:href` monta o link com o ID do produto correspondente.

Na versão em memória dos slides, a busca usa `for` para percorrer a lista, `equals` para comparar o valor dos IDs e `break` para encerrar quando encontra. No projeto atual, a busca usa o Repository.

A exclusão usa POST porque altera dados. O `confirm` oferece uma confirmação visual, mas a regra que permite ou impede excluir continua no servidor.

### Exemplo real: consultar o produto da URL

**Arquivo:** `controller/ProdutoController.java`.

```java
@GetMapping("/produtos/{id}")
public String detalhes(@PathVariable Long id, Model model, RedirectAttributes redirect) {
    Produto produto = produtos.buscar(id);
    if (produto == null) {
        return naoEncontrado(redirect);
    }
    model.addAttribute("produto", produto);
    model.addAttribute("categorias", produtos.listarCategorias());
    return "detalhes";
}
```

Se o endereço for `/produtos/5`, o parâmetro `id` recebe 5. O Service procura esse registro. Se não existir, `null` indica ausência e o Controller executa o retorno de erro. Se existir, envia produto e categorias ao template. O mesmo método atende outros números sem copiar o código.

## Aula 11 — Edição, CRUD e feedback

**CRUD** é o nome das quatro operações básicas:

| Operação | No projeto |
| --- | --- |
| Create — criar | Cadastrar produto. |
| Read — ler | Listar e consultar detalhes. |
| Update — atualizar | Editar o cadastro. |
| Delete — excluir | Excluir produto sem histórico. |

No cadastro, o formulário recebe um objeto novo. Na edição, recebe os dados de um produto existente. `th:field` usa esses dados para mostrar os campos preenchidos.

Ao salvar uma edição, a aplicação busca o registro original e copia os campos permitidos com setters. Isso evita substituir indiscriminadamente informações que não deveriam mudar.

No FlaStore, detalhes e formulário de edição ficam na mesma página. O conceito da aula continua sendo buscar pelo ID, mostrar valores e receber as mudanças.

### Mensagens de resultado

Uma mensagem **flash** é temporária e atravessa o redirecionamento. O Controller usa `addFlashAttribute` para informar sucesso ou erro, e o HTML usa `th:if` e `th:text` para apresentar a mensagem.

**Por que flash, e não apenas Model?** Porque um redirect gera outra requisição. O atributo comum do pedido anterior não é automaticamente a mensagem do pedido seguinte.

**E se o ID não existir?** A busca pode devolver ausência. O Controller verifica isso e redireciona à lista com mensagem, em vez de tentar mostrar os dados de um objeto inexistente.

Ter CRUD não significa necessariamente ter banco: a aula demonstra CRUD em memória antes de introduzir persistência.

### Exemplo real: copiar apenas os campos do cadastro

**Arquivo:** `service/ProdutoService.java`, trecho do método `copiarCadastro`.

```java
destino.setCodigo(origem.getCodigo());
destino.setNome(origem.getNome());
destino.setDescricao(origem.getDescricao());
destino.setPreco(origem.getPreco());
```

`origem` contém os dados recebidos. `destino` é o produto que receberá essas mudanças. Cada getter lê um valor e o setter correspondente o atribui ao destino. O saldo não aparece nesse trecho: editar o cadastro não deve retirar ou acrescentar unidades sem uma movimentação.

No final da atualização, `ProdutoController.java` usa:

```java
redirect.addFlashAttribute("mensagem", "Produto atualizado com sucesso.");
return "redirect:/produtos/" + id;
```

O primeiro comando prepara a mensagem temporária. O segundo monta o endereço do produto usando seu ID e manda o navegador fazer outro GET. Por exemplo, ID 5 gera `/produtos/5`.

## Aula 12 — JPA, entidades e migrations

**Persistência** significa manter os registros depois de encerrar a aplicação. O banco resolve a perda de dados da lista em memória.

**ORM** é o mapeamento de objetos para tabelas. **JPA** é a especificação desse trabalho de persistência. **Hibernate** é a implementação usada no projeto. **PostgreSQL** é o banco: esses nomes não são sinônimos.

### Anotações de entidade

| Anotação | O que indica |
| --- | --- |
| `@Entity` | A classe é uma entidade persistente. |
| `@Table` | A tabela correspondente. |
| `@Id` | O campo de chave primária. |
| `@GeneratedValue` | A estratégia de geração do ID. |
| `@Column` | Detalhes de uma coluna, como nome e tamanho. |

O projeto usa `Long` e `GenerationType.IDENTITY`: o banco gera o ID. Não há um contador Java para essa identidade.

**Por que não UUID?** UUID é uma alternativa apresentada nos slides, útil quando diferentes fontes geram identificadores independentemente. Long é mais simples de acompanhar neste trabalho com um único banco. UUID não está implementado.

### Quem cria as tabelas?

O **Flyway** executa arquivos SQL em ordem de versão:

- V1 cria as oito tabelas.
- V2 acrescenta a coluna de saldo.
- V3 insere os dados de demonstração.

Eles ficam em `src/main/resources/db/migration`. O nome tem versão, dois underscores e descrição. O Flyway guarda o histórico e não reaplica a mesma versão a cada início.

O banco `flastore` precisa existir antes. O Flyway cria suas tabelas, não o banco usado para estabelecer a conexão.

**Por que `ddl-auto=validate`, e não `update`?** Para deixar a criação e a evolução da estrutura sob controle das migrations. O Hibernate confere o mapeamento, e o Flyway executa as mudanças de estrutura.

**Por que não alterar uma migration aplicada?** Porque ela faz parte do histórico. Uma mudança posterior deve ganhar uma nova versão.

O Java e o SQL precisam concordar nos nomes, tipos e limites dos dados.

### Exemplo real: identificar a entidade e gerar seu ID

**Arquivo:** `model/Produto.java`, trecho inicial da classe.

```java
@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
```

Este é apenas o começo da classe, que continua com os outros campos e métodos. O objeto Produto corresponde à tabela `produtos`. Seu campo `id` é a chave primária e recebe o valor gerado pelo banco. Não é necessário guardar um contador Java para escolher o próximo ID.

**Arquivo:** `src/main/resources/db/migration/V2__adicionar_saldo_estoque.sql`.

```sql
ALTER TABLE produtos ADD COLUMN estoque INTEGER NOT NULL DEFAULT 0 CHECK (estoque >= 0);
```

`ALTER TABLE` muda uma tabela existente. A nova coluna guarda um inteiro, não permite nulo, começa com zero quando omitida e rejeita saldo negativo. Ela está na V2 porque é uma evolução da estrutura criada na V1.

No `application.properties`, temos:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Essa configuração deixa a alteração de estrutura nos scripts do Flyway e pede ao Hibernate que confira a compatibilidade do mapeamento.

## Aula 13 — Repository e persistência

O Repository oferece operações de acesso aos registros. Exemplo do projeto:

```java
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
```

`Categoria` é o tipo da entidade e `Long` é o tipo de seu ID. A interface parece vazia, mas herda operações prontas do Spring Data JPA. Não precisamos escrever uma implementação manual para essas operações comuns.

| Método | Função |
| --- | --- |
| `save` | Salvar entidade nova ou aplicar uma atualização. |
| `findAll` | Consultar os registros. |
| `findById` | Buscar um registro pelo ID. |
| `deleteById` | Excluir pelo ID. |

**Como diferenciar cadastro de edição?** O cadastro cria uma entidade nova sem ID. A edição busca a entidade que já existe e altera seus campos. Não se deve confiar em um ID arbitrário enviado pelo navegador para substituir um registro inteiro.

`findById` devolve um **Optional**, que representa um resultado presente ou ausente. O projeto usa `orElse(null)` e depois confere se encontrou.

### Injeção de dependência

O Spring fornece o Repository ao Service pelo construtor. A classe recebe o componente de que precisa, em vez de montar manualmente toda a infraestrutura de acesso ao banco.

**Qual a diferença entre Repository e Flyway?** Repository manipula os registros durante o uso. Flyway controla as versões da estrutura. `save` não substitui uma migration de criação de tabela.

Depois de cadastrar, é possível conferir o registro no pgAdmin. Reiniciar a aplicação não deve apagar os dados persistidos.

### Exemplos reais: lista, busca e gravação

**Arquivo:** `service/ProdutoService.java`.

```java
public List<Produto> listar() {
    return produtos.findAll(Sort.by("id"));
}
```

`List<Produto>` informa que o resultado é uma lista de produtos. `findAll` consulta o Repository e `Sort.by("id")` pede ordenação pelo ID.

```java
public Produto buscar(Long id) {
    return produtos.findById(id).orElse(null);
}
```

O método recebe o ID, consulta e devolve o Produto encontrado. `orElse(null)` representa ausência com nulo. É o Controller que decide a página ou o redirecionamento para esse caso.

No método de cadastro do mesmo Service, esta linha realiza o pedido de gravação:

```java
produtos.save(novo);
```

Ela recebe a entidade nova que já foi preparada e validada. A operação é diferente de `new Produto()`: criar um objeto na memória não é o mesmo que pedir sua persistência.

## Aula 14 — Validações no backend

Não basta o campo HTML ter `required`. O backend precisa conferir os dados recebidos independentemente da interface usada para enviá-los.

O projeto usa **Bean Validation**, com regras declaradas nas propriedades dos objetos.

| Restrição | Significado |
| --- | --- |
| `@NotBlank` | Texto não pode ser nulo, vazio ou somente espaços. |
| `@NotNull` | Um valor precisa estar presente. |
| `@Size` | Limita o comprimento do texto. |

Por exemplo, nome de produto é obrigatório e tem limite de 120 caracteres. Uma categoria relacionada é um objeto: usa `@NotNull`, não `@NotBlank`.

`@Valid` pede ao Spring que valide o objeto recebido. O **BindingResult** guarda os erros de preenchimento, conversão e validação. Ele deve vir imediatamente depois do objeto validado na assinatura do método.

### O que acontece quando há erro?

O Controller não salva. Ele retorna o formulário na mesma requisição, mantendo os valores e as mensagens. Também recarrega as listas necessárias aos selects.

`th:errors` apresenta o erro do campo. `th:errorclass` aplica uma classe CSS para destacá-lo. `#fields.hasErrors` permite verificar se há mensagens para mostrar.

**Por que não redirecionar no erro?** Porque um novo pedido perderia os dados e erros daquele formulário sem um tratamento adicional. No sucesso, o redirect continua apropriado.

### Validação Java e restrição SQL

`NOT NULL` no banco impede nulo. `@NotBlank` também rejeita texto vazio e espaços e permite dar uma mensagem compreensível. São proteções complementares.

Se aumentar o limite de nome no Java, precisa conferir o limite da coluna e do formulário. A mudança de estrutura deve ser uma nova migration, não uma edição da versão aplicada.

### Exemplo real: o nome precisa de conteúdo e tamanho permitido

**Arquivo:** `model/Produto.java`.

```java
@NotBlank(message = "Informe o nome.")
@Size(max = 120, message = "O nome deve ter até 120 caracteres.")
@Column(nullable = false, length = 120)
private String nome;
```

`@NotBlank` rejeita nome vazio ou apenas espaços. `@Size` limita o comprimento. `@Column` descreve a coluna do banco. As mensagens explicam o problema ao usuário; elas não são os valores do nome.

No método `cadastrar` de `ProdutoController.java`, a assinatura recebe `@Valid Produto` e imediatamente depois `BindingResult erros`. O tratamento de erro contém:

```java
if (erros.hasErrors()) {
    model.addAttribute("categorias", produtos.listarCategorias());
    return "cadastro";
}
```

O formulário é devolvido com os dados e mensagens. A lista de categorias é carregada outra vez porque retornar o template não executa automaticamente o método GET `novo`.

Em `templates/cadastro.html`, o erro específico do nome aparece com:

```html
<p class="erro-campo" th:if="${#fields.hasErrors('nome')}" th:errors="*{nome}"></p>
```

`th:if` só inclui a mensagem quando o campo tem erro. `th:errors` usa o resultado da validação para preencher o texto, sem escrever manualmente uma mensagem diferente no HTML para cada regra.

## Aula 15 — Service e regras de negócio

O Service é o lugar das operações e regras da aplicação. Ele coordena Repositories, mas não escolhe páginas HTML.

| Parte | Responsabilidade |
| --- | --- |
| Controller | Pedido HTTP, formulário, Model, template e redirect. |
| Service | Regras e execução das operações. |
| Repository | Acesso aos registros. |

### Exemplos reais de regras do FlaStore

- Produto novo começa com saldo zero.
- Código de produto não pode se repetir.
- A categoria escolhida precisa existir.
- Uma saída não pode ultrapassar o saldo disponível.
- Produto com movimentações não pode ser excluído.

**Qual a diferença para validar um campo?** Quantidade positiva é uma regra sobre o valor recebido. Quantidade que cabe no saldo depende do estado do estoque: essa decisão é da operação de negócio.

### Como uma saída funciona

O Controller recebe o formulário e observa os erros de validação. Sem erro, chama o `EstoqueService`. O Service consulta os registros necessários, confere o saldo e decide se pode registrar a saída. O Controller usa o resultado para mostrar sucesso ou erro.

**Por que não fazer tudo no Controller?** Porque regras da loja ficariam misturadas com endereço de página e redirecionamento. Separadas, podem ser compreendidas e usadas sem depender de uma tela específica.

**Um Service precisa usar só um Repository?** Não. Uma entrada precisa relacionar produto, fornecedor e usuário, além de gravar os registros do movimento. Coordenar essas entidades é justamente uma função do Service.

**O Service é apenas outro nome para Repository?** Não. Além de pedir consultas e gravações, ele decide o que é permitido. Os exemplos de saldo e histórico mostram esse propósito.

### Exemplo real: recusar retirada sem saldo

**Arquivo:** `service/EstoqueService.java`, dentro de `registrarSaida`.

```java
if (dados.getQuantidade() > produto.getEstoque()) {
    return "Estoque insuficiente para essa saída.";
}
```

`dados` é o objeto preenchido pelo formulário. `produto` é o registro consultado. Se a quantidade desejada for maior que o saldo, o método para e devolve uma mensagem. Por exemplo, quantidade 8 com saldo 7 é rejeitada antes de salvar a saída.

Na operação aceita, o mesmo método contém:

```java
produto.setEstoque(produto.getEstoque() - dados.getQuantidade());
produtos.save(produto);
return null;
```

A primeira linha calcula o saldo que restou; a segunda pede sua gravação. Nesse método, `null` significa que não há mensagem de impedimento. O Controller interpreta esse resultado para apresentar sucesso ou erro. Não é o Service que retorna um nome de página.

**Por que isso não é apenas `@Min(1)`?** Porque uma quantidade pode ser positiva e ainda ultrapassar o saldo. A condição precisa do estado atual do produto, por isso está no Service.

## Aula 16 — Relacionamentos JPA

Os registros têm relações. Um produto pertence a uma categoria; uma entrada tem fornecedor e usuário; um item liga um produto a uma movimentação.

**Cardinalidade** indica quantos registros podem se relacionar. Uma categoria pode ter muitos produtos: 1:N. Pelo lado de Produto, muitos produtos podem ter uma categoria: N:1.

### ManyToOne e chave estrangeira

```java
@ManyToOne(optional = false)
@JoinColumn(name = "categoria_id", nullable = false)
private Categoria categoria;
```

`@ManyToOne` descreve a relação de muitos produtos para uma categoria. `@JoinColumn` informa qual coluna da tabela produtos guarda a referência.

Na classe, temos um objeto Categoria. No banco, a coluna `categoria_id` guarda seu ID. A **chave estrangeira**, ou FK, exige que a categoria referenciada exista.

**Por que não guardar só o nome da categoria?** Porque seria texto repetido sem o mesmo vínculo com um cadastro compartilhado. A associação permite consultar `produto.categoria.nome` e manter integridade entre os registros.

O select mostra o nome da categoria e envia seu ID. O Service consulta a categoria existente e a associa ao produto. O número recebido pelo formulário e o objeto relacionado na entidade são duas partes desse caminho.

### Relação unidirecional

Produto conhece Categoria. Categoria não precisa ter uma lista de produtos dentro de seu objeto Java. Isso mantém o modelo simples.

`@OneToMany` poderia representar uma coleção inversa. Em uma relação bidirecional, `mappedBy` indica a propriedade Java que é dona do vínculo. Esse mapeamento de coleção não está no projeto atual.

### ManyToMany e tabela associativa

Os slides usam o exemplo de vários chamados com várias tags. É uma relação N:N. Ela exige uma tabela com uma linha por vínculo, em vez de guardar IDs numa string como `1,3,5`.

`@ManyToMany` representa a coleção. `@JoinTable` define a tabela associativa; `joinColumns` aponta ao lado dono e `inverseJoinColumns` ao outro lado. Uma chave composta pelos dois IDs impede repetir o mesmo vínculo. Checkboxes podem permitir selecionar os vários registros no formulário e mostrar as escolhas na edição.

Esse exemplo não está implementado literalmente no FlaStore.

### Por que o FlaStore usa entidades de itens?

Uma entrada pode reunir vários produtos e um produto pode aparecer em várias entradas. Porém, a ligação também tem **quantidade e custo unitário**. Ela não é apenas um par de IDs.

Por isso existe `ItemEntrada`, ligado a Entrada e Produto por duas relações `@ManyToOne`. `ItemSaida` segue a mesma ideia, com quantidade. São associações com dados próprios.

**Por que não usar `CascadeType.ALL` automaticamente?** Porque remover um registro não deve apagar outras entidades compartilhadas. Excluir um produto não deveria apagar sua categoria. A aula pede cuidado com cascatas, e o projeto não usa remoção em cascata nessas associações.

### Exemplo real: selecionar uma categoria existente

**Arquivo:** `templates/cadastro.html`.

```html
<select id="categoriaProduto" th:field="*{categoria.id}" name="categoriaProduto" required>
  <option value="">Selecione</option>
  <option th:each="categoria : ${categorias}" th:value="${categoria.id}" th:text="${categoria.nome}"></option>
</select>
```

O laço monta as opções consultadas no banco. `th:text` apresenta o nome e `th:value` guarda o ID enviado. `categoria.id` informa onde essa seleção entra no objeto do formulário. A opção vazia pede uma escolha real.

O `ProdutoService`, ao copiar os dados para a entidade, usa:

```java
destino.setCategoria(categorias.findById(origem.getCategoria().getId()).orElseThrow());
```

Ele recebe o ID escolhido, busca a categoria verdadeira no banco e atribui essa entidade ao produto. `orElseThrow` gera uma falha se ela não puder ser obtida; a validação do Service normalmente já conferiu sua existência antes dessa etapa.

### Exemplo real: um item tem duas associações

**Arquivo:** `model/ItemEntrada.java`.

```java
@ManyToOne(optional = false)
@JoinColumn(name = "entrada_id", nullable = false)
private Entrada entrada;

@ManyToOne(optional = false)
@JoinColumn(name = "produto_id", nullable = false)
private Produto produto;
```

O item aponta para a entrada a que pertence e para o produto recebido. Na mesma classe, os campos `quantidade` e `custoUnitario` guardam os dados dessa ligação. Isso explica por que ela é uma entidade própria, em vez de uma associação N:N simples sem atributos.

## Ligando tudo: caminho de um cadastro

1. O navegador faz GET para abrir o formulário.
2. O Controller envia objeto vazio e categorias pelo Model.
3. O Thymeleaf monta o formulário.
4. O usuário preenche e envia um POST.
5. O Spring faz o binding dos valores para o objeto Java.
6. `@Valid` e `BindingResult` permitem conferir os campos.
7. O Service aplica as regras do cadastro.
8. O Repository solicita a gravação usando JPA/Hibernate.
9. O PostgreSQL armazena o registro e gera o ID.
10. O Controller envia uma mensagem flash e redireciona.
11. Um novo GET consulta a lista, e o Thymeleaf mostra o produto.

## O que você deve conseguir explicar

Antes da apresentação, tente responder sem ler:

1. O que fica no navegador e o que fica no servidor?
2. Qual a diferença entre rota e arquivo HTML?
3. Como um valor sai do Java e aparece no HTML?
4. Como o formulário preenche um objeto Java?
5. Por que GET abre a tela e POST envia alterações?
6. Por que a edição precisa encontrar o registro original?
7. Por que a lista em memória foi substituída por banco?
8. Qual a diferença entre JPA, Hibernate, Repository e PostgreSQL?
9. Qual é o papel do Flyway?
10. Por que validar no backend e manter restrições no banco?
11. Por que regras de saldo ficam no Service?
12. Como o objeto Categoria corresponde à FK do banco?
13. Qual a diferença entre uma relação N:1 e uma N:N?
14. Por que a associação entre produto e entrada virou uma entidade de item?

As respostas estão nas seções correspondentes deste guia. O importante é entender o caminho dos dados e o motivo de cada parte existir, em vez de decorar os nomes das anotações.
