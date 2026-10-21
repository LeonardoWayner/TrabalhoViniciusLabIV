package br.com.flastore.controller;

import br.com.flastore.model.Usuario;
import br.com.flastore.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    private final UsuarioService usuarios;

    public LoginController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("logado"))) {
            return "redirect:/";
        }
        return "login";
    }
    @PostMapping("/login")
    public String entrar(@RequestParam String emailLogin, @RequestParam String senhaLogin, HttpServletRequest request, Model model) {
        Usuario usuario = usuarios.autenticar(emailLogin, senhaLogin);
        if (usuario != null) {
            HttpSession session = request.getSession();
            request.changeSessionId();
            session.setAttribute("logado", true);
            session.setAttribute("usuarioId", usuario.getId());
            return "redirect:/";
        }
        model.addAttribute("erro", "E-mail ou senha incorretos.");
        return "login";
    }
    @PostMapping("/sair")
    public String sair(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
