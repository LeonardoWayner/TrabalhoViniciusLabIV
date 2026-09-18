package br.com.flastore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("logado")) ? "redirect:/" : "login";
    }
    @PostMapping("/login")
    public String entrar(@RequestParam String emailLogin, @RequestParam String senhaLogin, HttpServletRequest request, Model model) {
        if ("adm@teste.com".equals(emailLogin.trim()) && "teste".equals(senhaLogin)) {
            request.getSession();
            request.changeSessionId();
            request.getSession().setAttribute("logado", true);
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
