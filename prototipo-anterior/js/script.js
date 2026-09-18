const EMAIL_PADRAO = "adm@teste.com";
const SENHA_PADRAO = "teste";
const CHAVE_LOGIN = "flastore-logado";
const CHAVE_PRODUTOS = "flastore-produtos";

const PRODUTOS_INICIAIS = [
  {
    codigo: "001",
    nome: "Camisa Rubro-Negra",
    categoria: "Camisas",
    preco: 189.9,
    estoque: 24,
    situacao: "Disponível",
    descricao: "Modelo listrado em tecido leve, usado como exemplo para demonstrar a tela de visualização do sistema."
  },
  {
    codigo: "002",
    nome: "Jaqueta Arquibancada",
    categoria: "Camisas",
    preco: 249.9,
    estoque: 8,
    situacao: "Disponível",
    descricao: "Jaqueta preta com detalhe vermelho para dias de jogo à noite."
  },
  {
    codigo: "003",
    nome: "Cachecol Torcida",
    categoria: "Acessórios",
    preco: 79.9,
    estoque: 5,
    situacao: "Baixo estoque",
    descricao: "Cachecol macio com faixas vermelhas e pretas para a arquibancada."
  },
  {
    codigo: "004",
    nome: "Caneca Listrada",
    categoria: "Casa",
    preco: 49.9,
    estoque: 18,
    situacao: "Disponível",
    descricao: "Caneca de cerâmica para café, coleção ou presente de torcedor."
  },
  {
    codigo: "005",
    nome: "Boné Nação",
    categoria: "Acessórios",
    preco: 69.9,
    estoque: 3,
    situacao: "Baixo estoque",
    descricao: "Boné preto com detalhe vermelho, aba curva e ajuste traseiro."
  },
  {
    codigo: "006",
    nome: "Kit Presente",
    categoria: "Kits",
    preco: 299.9,
    estoque: 11,
    situacao: "Disponível",
    descricao: "Caixa especial com camisa, caneca e cachecol para presentear."
  },
  {
    codigo: "007",
    nome: "Garrafa de Treino",
    categoria: "Acessórios",
    preco: 59.9,
    estoque: 20,
    situacao: "Disponível",
    descricao: "Garrafa esportiva com pegada confortável e visual rubro-negro."
  },
  {
    codigo: "008",
    nome: "Camisa Retrô",
    categoria: "Camisas",
    preco: 219.9,
    estoque: 0,
    situacao: "Esgotado",
    descricao: "Peça de algodão com listras clássicas e corte casual."
  }
];

document.addEventListener("DOMContentLoaded", () => {
  const paginaAtual = obterPaginaAtual();

  configurarLoginSistema();

  if (!controlarAcesso(paginaAtual)) {
    return;
  }

  marcarPaginaAtual(paginaAtual);
  configurarSaida();
  atualizarResumoInicio();
  renderizarTabelaProdutos();
  configurarFiltroProdutos();
  configurarCadastro();
  configurarEdicaoDetalhes();
});

function obterPaginaAtual() {
  return location.pathname.split("/").pop() || "index.html";
}

function usuarioEstaLogado() {
  return localStorage.getItem(CHAVE_LOGIN) === "sim";
}

function controlarAcesso(paginaAtual) {
  const paginaLogin = paginaAtual === "login.html";

  if (paginaLogin && usuarioEstaLogado()) {
    location.href = "index.html";
    return false;
  }

  if (!paginaLogin && !usuarioEstaLogado()) {
    location.href = "login.html";
    return false;
  }

  return true;
}

function configurarLoginSistema() {
  const form = document.querySelector("#loginSistemaForm");
  const mensagem = document.querySelector("#mensagemLogin");

  if (!form || !mensagem) {
    return;
  }

  form.addEventListener("submit", (event) => {
    event.preventDefault();

    const email = document.querySelector("#emailLogin").value.trim();
    const senha = document.querySelector("#senhaLogin").value;

    if (email === EMAIL_PADRAO && senha === SENHA_PADRAO) {
      localStorage.setItem(CHAVE_LOGIN, "sim");
      location.href = "index.html";
      return;
    }

    mensagem.textContent = "E-mail ou senha incorretos.";
    mensagem.classList.add("visivel");
  });
}

function configurarSaida() {
  const botaoSair = document.querySelector("#sairSistema");

  if (!botaoSair) {
    return;
  }

  botaoSair.addEventListener("click", () => {
    localStorage.removeItem(CHAVE_LOGIN);
    location.href = "login.html";
  });
}

function marcarPaginaAtual(paginaAtual) {
  const links = document.querySelectorAll(".menu a");

  links.forEach((link) => {
    if (link.getAttribute("href") === paginaAtual) {
      link.classList.add("ativo");
    }
  });
}

function obterProdutos() {
  const produtosSalvos = localStorage.getItem(CHAVE_PRODUTOS);

  if (!produtosSalvos) {
    salvarProdutos(PRODUTOS_INICIAIS);
    return [...PRODUTOS_INICIAIS];
  }

  try {
    return JSON.parse(produtosSalvos);
  } catch {
    salvarProdutos(PRODUTOS_INICIAIS);
    return [...PRODUTOS_INICIAIS];
  }
}

function salvarProdutos(produtos) {
  localStorage.setItem(CHAVE_PRODUTOS, JSON.stringify(produtos));
}

function encontrarProduto(codigo) {
  return obterProdutos().find((produto) => produto.codigo === codigo);
}

function obterCodigoProdutoAtual() {
  const parametros = new URLSearchParams(location.search);
  return parametros.get("id") || "001";
}

function atualizarResumoInicio() {
  const totalProdutos = document.querySelector("#totalProdutos");
  const totalDisponiveis = document.querySelector("#totalDisponiveis");
  const totalEsgotados = document.querySelector("#totalEsgotados");
  const tituloEsgotados = document.querySelector("#tituloEsgotados");
  const listaEsgotados = document.querySelector("#listaEsgotados");

  if (!totalProdutos || !totalDisponiveis || !totalEsgotados || !tituloEsgotados || !listaEsgotados) {
    return;
  }

  const produtos = obterProdutos();
  const disponiveis = produtos.filter((produto) => produto.situacao !== "Esgotado");
  const esgotados = produtos.filter((produto) => produto.situacao === "Esgotado");

  totalProdutos.textContent = produtos.length;
  totalDisponiveis.textContent = disponiveis.length;
  totalEsgotados.textContent = esgotados.length;
  tituloEsgotados.textContent = esgotados.length === 1 ? "Produto esgotado" : "Produtos esgotados";

  if (esgotados.length === 0) {
    listaEsgotados.textContent = "Nenhum produto está esgotado.";
  } else {
    listaEsgotados.textContent = `${esgotados.map((produto) => produto.nome).join(", ")}.`;
  }
}

function renderizarTabelaProdutos() {
  const tabela = document.querySelector("#tabelaProdutos");
  const contador = document.querySelector("#contadorProdutos");

  if (!tabela || !contador) {
    return;
  }

  const produtos = obterProdutos();
  const corpoTabela = tabela.querySelector("tbody");

  corpoTabela.innerHTML = produtos
    .map(
      (produto) => `
        <tr>
          <td>${produto.codigo}</td>
          <td>${produto.nome}</td>
          <td>${produto.categoria}</td>
          <td>${formatarMoeda(produto.preco)}</td>
          <td>${produto.estoque}</td>
          <td><span class="status ${obterClasseSituacao(produto.situacao)}">${produto.situacao}</span></td>
          <td><a href="detalhes.html?id=${produto.codigo}">Ver</a></td>
        </tr>
      `
    )
    .join("");

  contador.textContent = `${produtos.length} produto(s) encontrado(s)`;
}

function obterClasseSituacao(situacao) {
  if (situacao === "Esgotado") {
    return "indisponivel";
  }

  if (situacao === "Baixo estoque") {
    return "atencao";
  }

  return "ok";
}

function configurarFiltroProdutos() {
  const filtro = document.querySelector("#filtroProduto");
  const tabela = document.querySelector("#tabelaProdutos");
  const contador = document.querySelector("#contadorProdutos");

  if (!filtro || !tabela || !contador) {
    return;
  }

  filtro.addEventListener("input", () => {
    const linhas = Array.from(tabela.querySelectorAll("tbody tr"));
    const busca = filtro.value.trim().toLowerCase();
    let encontrados = 0;

    linhas.forEach((linha) => {
      const texto = linha.textContent.toLowerCase();
      const deveMostrar = texto.includes(busca);

      linha.hidden = !deveMostrar;
      if (deveMostrar) {
        encontrados += 1;
      }
    });

    contador.textContent = `${encontrados} produto(s) encontrado(s)`;
  });
}

function configurarCadastro() {
  const form = document.querySelector("#produtoForm");
  const mensagem = document.querySelector("#mensagemCadastro");

  if (!form || !mensagem) {
    return;
  }

  form.addEventListener("submit", (event) => {
    event.preventDefault();

    const produtos = obterProdutos();
    const nome = document.querySelector("#nomeProduto").value.trim();
    const categoria = document.querySelector("#categoriaProduto").value;
    const preco = Number(document.querySelector("#precoProduto").value);
    const estoque = Number(document.querySelector("#estoqueProduto").value);
    const descricao = document.querySelector("#descricaoProduto").value.trim();
    const proximoCodigo = String(produtos.length + 1).padStart(3, "0");

    produtos.push({
      codigo: proximoCodigo,
      nome,
      categoria,
      preco,
      estoque,
      situacao: definirSituacaoPorEstoque(estoque),
      descricao
    });

    salvarProdutos(produtos);
    mensagem.textContent = `Produto "${nome}" cadastrado com sucesso.`;
    mensagem.classList.add("visivel");
    form.reset();
  });
}

function configurarEdicaoDetalhes() {
  const form = document.querySelector("#editarProdutoForm");
  const mensagem = document.querySelector("#mensagemEdicao");

  if (!form || !mensagem) {
    return;
  }

  const codigo = obterCodigoProdutoAtual();
  const produto = encontrarProduto(codigo) || encontrarProduto("001");

  preencherDetalhes(produto);

  form.addEventListener("submit", (event) => {
    event.preventDefault();

    const produtos = obterProdutos();
    const indice = produtos.findIndex((item) => item.codigo === produto.codigo);
    const estoque = Number(document.querySelector("#editarEstoque").value);
    const situacao = estoque === 0 ? "Esgotado" : document.querySelector("#editarSituacao").value;

    const produtoAtualizado = {
      codigo: produto.codigo,
      nome: document.querySelector("#editarNome").value.trim(),
      categoria: document.querySelector("#editarCategoria").value,
      preco: Number(document.querySelector("#editarPreco").value),
      estoque,
      situacao,
      descricao: document.querySelector("#editarDescricao").value.trim()
    };

    produtos[indice] = produtoAtualizado;
    salvarProdutos(produtos);
    preencherDetalhes(produtoAtualizado);

    mensagem.textContent = "Informações do produto atualizadas no protótipo.";
    mensagem.classList.add("visivel");
  });
}

function preencherDetalhes(produto) {
  if (!produto) {
    return;
  }

  document.querySelector("#tituloProduto").textContent = produto.nome;
  document.querySelector("#codigoProdutoTexto").textContent = `Código do produto: ${produto.codigo}`;
  document.querySelector("#categoriaDetalhe").textContent = produto.categoria;
  document.querySelector("#precoDetalhe").textContent = formatarMoeda(produto.preco);
  document.querySelector("#estoqueDetalhe").textContent = `${produto.estoque} unidade(s)`;
  document.querySelector("#situacaoDetalhe").textContent = produto.situacao;
  document.querySelector("#descricaoDetalhe").textContent = produto.descricao;

  document.querySelector("#editarNome").value = produto.nome;
  document.querySelector("#editarCategoria").value = produto.categoria;
  document.querySelector("#editarPreco").value = produto.preco;
  document.querySelector("#editarEstoque").value = produto.estoque;
  document.querySelector("#editarSituacao").value = produto.situacao;
  document.querySelector("#editarDescricao").value = produto.descricao;
}

function definirSituacaoPorEstoque(estoque) {
  if (estoque === 0) {
    return "Esgotado";
  }

  if (estoque <= 5) {
    return "Baixo estoque";
  }

  return "Disponível";
}

function formatarMoeda(valor) {
  return valor.toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL"
  });
}
