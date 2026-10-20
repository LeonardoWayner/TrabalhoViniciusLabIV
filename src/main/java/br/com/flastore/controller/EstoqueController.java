package br.com.flastore.controller;

import br.com.flastore.form.*;
import br.com.flastore.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EstoqueController {
    private final ProdutoService produtos;
    private final EstoqueService estoque;

    public EstoqueController(ProdutoService produtos, EstoqueService estoque) {
        this.produtos = produtos;
        this.estoque = estoque;
    }

    @GetMapping("/entradas")
    public String entradas(Model model) {
        model.addAttribute("entradaForm", new EntradaForm());
        carregarEntradas(model);
        return "entradas";
    }

    @PostMapping("/entradas")
    public String registrarEntrada(@Valid @ModelAttribute("entradaForm") EntradaForm dados,
            BindingResult erros, Model model, HttpSession session, RedirectAttributes redirect) {
        if (!erros.hasErrors()) {
            String erro = estoque.registrarEntrada(dados, (Long) session.getAttribute("usuarioId"));
            if (erro != null) erros.reject("negocio", erro);
        }
        if (erros.hasErrors()) {
            carregarEntradas(model);
            return "entradas";
        }
        redirect.addFlashAttribute("mensagem", "Entrada registrada com sucesso.");
        return "redirect:/entradas";
    }

    @GetMapping("/saidas")
    public String saidas(Model model) {
        model.addAttribute("saidaForm", new SaidaForm());
        carregarSaidas(model);
        return "saidas";
    }

    @PostMapping("/saidas")
    public String registrarSaida(@Valid @ModelAttribute("saidaForm") SaidaForm dados,
            BindingResult erros, Model model, HttpSession session, RedirectAttributes redirect) {
        if (!erros.hasErrors()) {
            String erro = estoque.registrarSaida(dados, (Long) session.getAttribute("usuarioId"));
            if (erro != null) erros.reject("negocio", erro);
        }
        if (erros.hasErrors()) {
            carregarSaidas(model);
            return "saidas";
        }
        redirect.addFlashAttribute("mensagem", "Saída registrada com sucesso.");
        return "redirect:/saidas";
    }

    private void carregarEntradas(Model model) {
        model.addAttribute("produtos", produtos.listar());
        model.addAttribute("fornecedores", estoque.listarFornecedores());
        model.addAttribute("itens", estoque.listarEntradas());
    }

    private void carregarSaidas(Model model) {
        model.addAttribute("produtos", produtos.listar());
        model.addAttribute("itens", estoque.listarSaidas());
    }
}
