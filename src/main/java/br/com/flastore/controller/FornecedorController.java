package br.com.flastore.controller;

import br.com.flastore.model.Fornecedor;
import br.com.flastore.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FornecedorController {
    private final FornecedorService fornecedores;

    public FornecedorController(FornecedorService fornecedores) {
        this.fornecedores = fornecedores;
    }

    @InitBinder("fornecedor")
    public void limitarCampos(WebDataBinder binder) {
        binder.setAllowedFields("nome", "cnpj", "telefone", "email");
    }

    @GetMapping("/fornecedores")
    public String listar(Model model) {
        model.addAttribute("fornecedor", new Fornecedor());
        model.addAttribute("fornecedores", fornecedores.listar());
        return "fornecedores";
    }

    @PostMapping("/fornecedores")
    public String cadastrar(@Valid @ModelAttribute("fornecedor") Fornecedor fornecedor,
            BindingResult erros, Model model, RedirectAttributes redirect) {
        if (!erros.hasErrors()) {
            String erro = fornecedores.cadastrar(fornecedor);
            if (erro != null) {
                erros.rejectValue("cnpj", "duplicado", erro);
            }
        }
        if (erros.hasErrors()) {
            model.addAttribute("fornecedores", fornecedores.listar());
            return "fornecedores";
        }
        redirect.addFlashAttribute("mensagem", "Fornecedor cadastrado com sucesso.");
        return "redirect:/fornecedores";
    }
}
