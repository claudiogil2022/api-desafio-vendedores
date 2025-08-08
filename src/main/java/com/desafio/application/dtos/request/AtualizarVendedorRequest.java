package com.desafio.application.dtos.request;

import com.desafio.domain.enums.TipoContratacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AtualizarVendedorRequest {
    
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    
    @Pattern(regexp = "\\d{11}|\\d{14}", message = "Documento deve ter 11 dígitos (CPF) ou 14 dígitos (CNPJ)")
    private String documento;
    
    @Email(message = "Email deve ter formato válido")
    private String email;
    
    private TipoContratacao tipoContratacao;
    
    private String filialId;
}
