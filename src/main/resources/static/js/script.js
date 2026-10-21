document.addEventListener("DOMContentLoaded", function () {
  const links = document.querySelectorAll(".menu a");
  for (const link of links) {
    if (link.pathname === location.pathname) {
      link.classList.add("ativo");
    }
  }
  configurarFiltroProdutos();
});

function configurarFiltroProdutos() {
  const filtro = document.querySelector("#filtroProduto");
  const tabela = document.querySelector("#tabelaProdutos");
  const contador = document.querySelector("#contadorProdutos");

  if (!filtro || !tabela || !contador) {
    return;
  }

  filtro.addEventListener("input", function () {
    const linhas = tabela.querySelectorAll("tbody tr");
    const busca = filtro.value.trim().toLowerCase();
    let encontrados = 0;

    for (const linha of linhas) {
      const texto = linha.textContent.toLowerCase();
      const deveMostrar = texto.includes(busca);

      linha.hidden = !deveMostrar;
      if (deveMostrar) {
        encontrados += 1;
      }
    }

    contador.textContent = `${encontrados} produto(s) encontrado(s)`;
  });
}
