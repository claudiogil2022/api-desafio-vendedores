package com.desafio.domain.entities;

import com.desafio.domain.enums.StatusProcessamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessamentoVendedor {
    private String id;
    private String vendedorId;
    private StatusProcessamento status;
    private String dadosOriginais;
    private String erro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void marcarComoProcessando() {
        this.status = StatusProcessamento.PROCESSANDO;
        this.updatedAt = LocalDateTime.now();
    }

    public void marcarComoConcluido(String vendedorId) {
        this.status = StatusProcessamento.CONCLUIDO;
        this.vendedorId = vendedorId;
        this.updatedAt = LocalDateTime.now();
    }

    public void marcarComoErro(String erro) {
        this.status = StatusProcessamento.ERRO;
        this.erro = erro;
        this.updatedAt = LocalDateTime.now();
    }
}
