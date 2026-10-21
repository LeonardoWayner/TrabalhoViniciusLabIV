package br.com.flastore.service;

import br.com.flastore.model.Usuario;
import br.com.flastore.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarios;
    private final BCryptPasswordEncoder senhas = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Transactional(readOnly = true)
    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarios.findByEmail(email.trim()).orElse(null);
        if (usuario == null || !usuario.getAtivo() || !senhas.matches(senha, usuario.getSenhaHash())) {
            return null;
        }
        return usuario;
    }
}
