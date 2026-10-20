package br.com.flastore.controller;

import br.com.flastore.model.Produto;
import br.com.flastore.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProdutoController {
    private final ProdutoService produtos;
    public ProdutoController(ProdutoService produtos) { this.produtos = produtos; }

    @InitBinder("produto")
    public void limitarCampos(WebDataBinder binder) {
        binder.setAllowedFields("codigo", "nome", "categoria.id", "preco", "descricao");
    }

    @GetMapping("/")
    public String inicio(Model model) {
        var lista = produtos.listar();
        var esgotados = lista.stream().filter(p -> p.getEstoque() == 0).toList();
        model.addAttribute("totalProdutos", lista.size());
        model.addAttribute("totalDisponiveis", lista.size() - esgotados.size());
        model.addAttribute("totalEsgotados", esgotados.size());
        model.addAttribute("listaEsgotados", esgotados.isEmpty() ? "Nenhum produto está esgotado." : String.join(", ", esgotados.stream().map(Produto::getNome).toList()) + ".");
        return "index";
    }

    @GetMapping("/produtos")
    public String listar(Model model) {
        model.addAttribute("produtos", produtos.listar());
        return "produtos";
    }

    @GetMapping("/produtos/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("categorias", produtos.listarCategorias());
        return "cadastro";
    }

    @PostMapping("/produtos")
    public String cadastrar(@Valid @ModelAttribute("produto") Produto produto, BindingResult erros,
            Model model, RedirectAttributes redirect) {
        produto.setId(null);
        if (!erros.hasErrors()) {
            String erro = produtos.cadastrar(produto);
            if (erro != null) erros.reject("negocio", erro);
        }
        if (erros.hasErrors()) {
            model.addAttribute("categorias", produtos.listarCategorias());
            return "cadastro";
        }
        redirect.addFlashAttribute("mensagem", "Produto cadastrado com sucesso. Registre uma entrada para adicionar estoque.");
        return "redirect:/produtos";
    }

    @GetMapping("/produtos/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Produto produto = produtos.buscar(id);
        if (produto == null) return naoEncontrado(redirect);
        model.addAttribute("produto", produto);
        model.addAttribute("categorias", produtos.listarCategorias());
        return "detalhes";
    }

    @PostMapping("/produtos/{id}/editar")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("produto") Produto produto,
            BindingResult erros, Model model, RedirectAttributes redirect) {
        Produto atual = produtos.buscar(id);
        if (atual == null) return naoEncontrado(redirect);
        produto.setId(id);
        produto.setEstoque(atual.getEstoque());
        if (!erros.hasErrors()) {
            String erro = produtos.atualizar(id, produto);
            if (erro != null) erros.reject("negocio", erro);
        }
        if (erros.hasErrors()) {
            model.addAttribute("categorias", produtos.listarCategorias());
            return "detalhes";
        }
        redirect.addFlashAttribute("mensagem", "Produto atualizado com sucesso.");
        return "redirect:/produtos/" + id;
    }

    @PostMapping("/produtos/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        String erro = produtos.excluir(id);
        redirect.addFlashAttribute(erro == null ? "mensagem" : "erro",
                erro == null ? "Produto excluído com sucesso." : erro);
        return "redirect:/produtos";
    }

    private String naoEncontrado(RedirectAttributes redirect) {
        redirect.addFlashAttribute("erro", "Produto não encontrado.");
        return "redirect:/produtos";
    }
}
