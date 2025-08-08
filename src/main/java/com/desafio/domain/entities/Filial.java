package com.desafio.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filial {
    private String id;
    private String nome;
    private String cnpj;
    private String cidade;
    private String uf;
    private String tipo;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    private LocalDateTime ultimaAtualizacao;
}
