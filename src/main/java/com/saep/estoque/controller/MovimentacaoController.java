package com.saep.estoque.controller;

import com.saep.estoque.model.Movimentacao;
import com.saep.estoque.model.Produto;
import com.saep.estoque.model.Usuario;
import com.saep.estoque.repository.MovimentacaoRepository;
import com.saep.estoque.repository.ProdutoRepository;
import com.saep.estoque.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@CrossOrigin("*")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Movimentacao> listar() {
        return movimentacaoRepository.findAll();
    }

    @GetMapping("/registrar")
    public String movimentar(@RequestParam Long produtoId,
                             @RequestParam Long usuarioId,
                             @RequestParam String tipo,
                             @RequestParam Integer quantidade) {

        Produto produto = produtoRepository.findById(produtoId).orElse(null);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (produto == null) {
            return "Produto não encontrado.";
        }

        if (usuario == null) {
            return "Usuário não encontrado.";
        }

        if (quantidade <= 0) {
            return "Quantidade inválida.";
        }

        if (tipo.equalsIgnoreCase("ENTRADA")) {
            produto.setQuantidade(produto.getQuantidade() + quantidade);
        } else if (tipo.equalsIgnoreCase("SAIDA")) {
            if (produto.getQuantidade() < quantidade) {
                return "Estoque insuficiente.";
            }

            produto.setQuantidade(produto.getQuantidade() - quantidade);
        } else {
            return "Tipo inválido. Use ENTRADA ou SAIDA.";
        }

        produtoRepository.save(produto);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);
        movimentacao.setTipo(tipo.toUpperCase());
        movimentacao.setQuantidade(quantidade);
        movimentacao.setDataMovimentacao(LocalDateTime.now());

        movimentacaoRepository.save(movimentacao);

        if (produto.getQuantidade() < produto.getEstoqueMinimo()) {
            return "Movimentação realizada. ALERTA: estoque abaixo do mínimo!";
        }

        return "Movimentação realizada com sucesso.";
    }
}
