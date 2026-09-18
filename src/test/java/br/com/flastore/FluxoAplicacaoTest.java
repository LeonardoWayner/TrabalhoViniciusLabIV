package br.com.flastore;

import br.com.flastore.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FluxoAplicacaoTest {
    @Autowired MockMvc mvc;
    @Autowired ProdutoService produtos;

    @Test
    void loginProtegeAsPaginasEPermiteSair() throws Exception {
        mvc.perform(get("/produtos")).andExpect(redirectedUrl("/login"));
        mvc.perform(get("/login")).andExpect(status().isOk());
        mvc.perform(post("/login").param("emailLogin", "adm@teste.com").param("senhaLogin", "errada"))
            .andExpect(content().string(containsString("E-mail ou senha incorretos.")));
        var resposta = mvc.perform(post("/login").param("emailLogin", "adm@teste.com").param("senhaLogin", "teste"))
            .andExpect(redirectedUrl("/")).andReturn();
        var session = (MockHttpSession) resposta.getRequest().getSession(false);
        mvc.perform(get("/").session(session)).andExpect(status().isOk());
        mvc.perform(post("/sair").session(session)).andExpect(redirectedUrl("/login"));
        assertThat(session.isInvalid()).isTrue();
    }

    @Test
    void renderizaTelasECadastraEditaValidaProduto() throws Exception {
        var session = new MockHttpSession(); session.setAttribute("logado", true);
        for (String rota : new String[]{"/", "/produtos", "/produtos/novo", "/produtos/001", "/css/style.css", "/js/script.js", "/img/produtos-rubro-negros.png"}) {
            mvc.perform(get(rota).session(session)).andExpect(status().isOk());
        }
        int antes = produtos.listar().size();
        mvc.perform(post("/produtos").session(session).param("nome", "Produto de teste").param("categoria", "Casa")
            .param("preco", "19.90").param("estoque", "2").param("descricao", "Descrição de teste"))
            .andExpect(redirectedUrl("/produtos"));
        assertThat(produtos.listar()).hasSize(antes + 1);
        String codigo = produtos.listar().get(antes).getCodigo();
        mvc.perform(get("/produtos").session(session)).andExpect(content().string(containsString("Produto de teste")));
        mvc.perform(post("/produtos/" + codigo).session(session).param("nome", "Produto atualizado").param("categoria", "Kits")
            .param("preco", "29.90").param("estoque", "0").param("descricao", "Descrição atualizada"))
            .andExpect(redirectedUrl("/produtos/" + codigo));
        assertThat(produtos.buscar(codigo).getSituacao()).isEqualTo("Esgotado");
        mvc.perform(get("/produtos/" + codigo).session(session)).andExpect(content().string(containsString("Produto atualizado")));
        mvc.perform(post("/produtos").session(session).param("nome", " ").param("categoria", "Inválida")
            .param("preco", "-1").param("estoque", "-2").param("descricao", ""))
            .andExpect(status().isOk()).andExpect(content().string(containsString("Confira os campos")));
        assertThat(produtos.listar()).hasSize(antes + 1);
        mvc.perform(post("/produtos/" + codigo).session(session).param("nome", "Inválido").param("categoria", "Casa")
            .param("preco", "abc").param("estoque", "abc").param("descricao", "Teste"))
            .andExpect(status().isOk()).andExpect(content().string(containsString("Confira os campos")));
        assertThat(produtos.buscar(codigo).getNome()).isEqualTo("Produto atualizado");
        mvc.perform(get("/produtos/inexistente").session(session)).andExpect(status().isNotFound());
    }
}
