package com.saep.estoque.controller;

import com.saep.estoque.repository.MovimentacaoRepository;
import com.saep.estoque.model.Produto;
import com.saep.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/produtos")
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @GetMapping
    public List<Produto> listar() {
        return repository.findAll();
    }

    @GetMapping("/buscar")
    public List<Produto> buscar(@RequestParam String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        return repository.save(produto);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        return repository.save(produto);
    }

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id) {
        try {
            Produto produto = repository.findById(id).orElse(null);

            if (produto == null) {
                return "Produto não encontrado.";
            }

            movimentacaoRepository.deleteAll(
                    movimentacaoRepository.findAll()
                            .stream()
                            .filter(m -> m.getProduto().getId().equals(id))
                            .toList()
            );

            repository.delete(produto);

            return "Produto excluído com sucesso.";
        } catch (Exception e) {
            return "Erro ao excluir produto: " + e.getMessage();
        }
    }
}
