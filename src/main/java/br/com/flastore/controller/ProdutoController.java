package br.com.flastore.controller;

import br.com.flastore.model.Produto;
import br.com.flastore.service.ProdutoService;
import java.util.ArrayList;
import java.util.List;
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
    public ProdutoController(ProdutoService produtos) {
        this.produtos = produtos;
    }

    @InitBinder("produto")
    public void limitarCampos(WebDataBinder binder) {
        binder.setAllowedFields("codigo", "nome", "categoria.id", "preco", "descricao");
    }

    @GetMapping("/")
    public String inicio(Model model) {
        List<Produto> lista = produtos.listar();
        List<String> nomesEsgotados = new ArrayList<>();
        for (Produto produto : lista) {
            if (produto.getEstoque() == 0) {
                nomesEsgotados.add(produto.getNome());
            }
        }

        String mensagem = "Nenhum produto está esgotado.";
        if (!nomesEsgotados.isEmpty()) {
            mensagem = String.join(", ", nomesEsgotados) + ".";
        }

        model.addAttribute("totalProdutos", lista.size());
        model.addAttribute("totalDisponiveis", lista.size() - nomesEsgotados.size());
        model.addAttribute("totalEsgotados", nomesEsgotados.size());
        model.addAttribute("listaEsgotados", mensagem);
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
            if (erro != null) {
                erros.reject("negocio", erro);
            }
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
        if (produto == null) {
            return naoEncontrado(redirect);
        }
        model.addAttribute("produto", produto);
        model.addAttribute("categorias", produtos.listarCategorias());
        return "detalhes";
    }

    @PostMapping("/produtos/{id}/editar")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("produto") Produto produto,
            BindingResult erros, Model model, RedirectAttributes redirect) {
        Produto atual = produtos.buscar(id);
        if (atual == null) {
            return naoEncontrado(redirect);
        }
        produto.setId(id);
        produto.setEstoque(atual.getEstoque());
        if (!erros.hasErrors()) {
            String erro = produtos.atualizar(id, produto);
            if (erro != null) {
                erros.reject("negocio", erro);
            }
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
        if (erro == null) {
            redirect.addFlashAttribute("mensagem", "Produto excluído com sucesso.");
        } else {
            redirect.addFlashAttribute("erro", erro);
        }
        return "redirect:/produtos";
    }

    private String naoEncontrado(RedirectAttributes redirect) {
        redirect.addFlashAttribute("erro", "Produto não encontrado.");
        return "redirect:/produtos";
    }
}
