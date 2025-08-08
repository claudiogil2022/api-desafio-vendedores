package com.desafio.application.dtos.response;

import com.desafio.domain.enums.StatusProcessamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessamentoResponse {
    private String id; // ID do processamento (para consultar status)
    private String vendedorId; // ID do vendedor criado (disponível após CONCLUIDO)
    private StatusProcessamento status;
    private String erro;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
