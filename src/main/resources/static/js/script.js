document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".menu a").forEach(link => {
    if (new URL(link.href).pathname === location.pathname) link.classList.add("ativo");
  });
  configurarFiltroProdutos();
});

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

