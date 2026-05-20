package com.saep.estoque.controller;

import com.saep.estoque.model.Usuario;
import com.saep.estoque.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String senha) {

        Usuario usuario = repository.findByEmailAndSenha(email, senha);

        if (usuario == null) {
            return "Email ou senha inválidos.";
        }

        return "Login realizado com sucesso. Bem-vindo " + usuario.getNome();
    }
    @GetMapping("/cadastrar")
    public Usuario cadastrar(@RequestParam String nome,
                             @RequestParam String email,
                             @RequestParam String senha) {

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        return repository.save(usuario);
    }
}