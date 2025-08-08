package com.desafio.application.dtos.response;

import com.desafio.domain.enums.TipoContratacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class VendedorResponse {
    private String id;
    private String matricula;
    private String nome;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    
    private String documento;
    private String email;
    private TipoContratacao tipoContratacao;
    private FilialResponse filial;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
