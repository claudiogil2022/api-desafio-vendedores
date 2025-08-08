package com.desafio.application.dtos.request;

import com.desafio.domain.enums.TipoContratacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CriarVendedorRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    
    @NotBlank(message = "CPF ou CNPJ é obrigatório")
    @Pattern(regexp = "\\d{11}|\\d{14}", message = "Informe um CPF (11 dígitos) ou CNPJ (14 dígitos) válido")
    private String documento;
    
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Informe um e-mail válido (ex: usuario@empresa.com)")
    @Size(max = 150, message = "E-mail não pode ter mais de 150 caracteres")
    private String email;
    
    @NotNull(message = "Tipo de contratação é obrigatório (CLT, OUTSOURCING ou PESSOA_JURIDICA)")
    private TipoContratacao tipoContratacao;
    
    @NotBlank(message = "Filial é obrigatória")
    private String filialId;
}
