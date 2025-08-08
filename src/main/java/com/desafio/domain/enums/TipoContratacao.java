package com.desafio.domain.enums;

public enum TipoContratacao {
    OUTSOURCING("Outsourcing"),
    CLT("CLT"),
    PESSOA_JURIDICA("Pessoa Jurídica");

    private final String descricao;

    TipoContratacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSufixoMatricula() {
        return switch (this) {
            case OUTSOURCING -> "OUT";
            case CLT -> "CLT";
            case PESSOA_JURIDICA -> "PJ";
        };
    }

    public static TipoContratacao fromSufixoMatricula(String sufixo) {
        return switch (sufixo) {
            case "OUT" -> OUTSOURCING;
            case "CLT" -> CLT;
            case "PJ" -> PESSOA_JURIDICA;
            default -> throw new IllegalArgumentException("Sufixo de matrícula inválido: " + sufixo);
        };
    }
}
