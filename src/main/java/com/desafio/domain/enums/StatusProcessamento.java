package com.desafio.domain.enums;

public enum StatusProcessamento {
    PENDENTE("Pendente"),
    PROCESSANDO("Processando"),
    CONCLUIDO("Concluído"),
    ERRO("Erro");

    private final String descricao;

    StatusProcessamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
