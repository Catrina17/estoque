package com.saep.estoque.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private Integer quantidade;

    private LocalDateTime dataMovimentacao;

    @ManyToOne
    private Produto produto;

    @ManyToOne
    private Usuario usuario;
}
