package br.com.flastore.controller;

import br.com.flastore.model.Produto;
import br.com.flastore.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProdutoController {
    private final ProdutoService produtos;
    public ProdutoController(ProdutoService produtos) { this.produtos = produtos; }

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
        return "cadastro";
    }
    @PostMapping("/produtos")
    public String cadastrar(@Valid @ModelAttribute("produto") Produto produto, BindingResult erros, RedirectAttributes redirect) {
        if (erros.hasErrors()) return "cadastro";
        produtos.cadastrar(produto);
        redirect.addFlashAttribute("mensagem", "Produto cadastrado com sucesso.");
        return "redirect:/produtos";
    }
    @GetMapping("/produtos/{codigo}")
    public String detalhes(@PathVariable String codigo, Model model) {
        model.addAttribute("produto", produtos.buscar(codigo));
        return "detalhes";
    }
    @PostMapping("/produtos/{codigo}")
    public String atualizar(@PathVariable String codigo, @Valid @ModelAttribute("produto") Produto produto, BindingResult erros, RedirectAttributes redirect) {
        produtos.buscar(codigo);
        produto.setCodigo(codigo);
        if (erros.hasErrors()) return "detalhes";
        produtos.atualizar(codigo, produto);
        redirect.addFlashAttribute("mensagem", "Produto atualizado com sucesso.");
        return "redirect:/produtos/" + codigo;
    }
}
