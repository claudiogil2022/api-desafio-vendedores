package com.desafio.domain.entities;

import com.desafio.domain.enums.TipoContratacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor {
    private String id;
    private String matricula;
    private String nome;
    private LocalDate dataNascimento;
    private String documento;
    private String email;
    private TipoContratacao tipoContratacao;
    private String filialId;
    private Filial filial;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isPessoaJuridica() {
        return tipoContratacao == TipoContratacao.PESSOA_JURIDICA;
    }

    public boolean isDocumentoValido() {
        if (documento == null || documento.trim().isEmpty()) {
            return false;
        }
        
        String documentoLimpo = documento.replaceAll("\\D", "");
        
        if (isPessoaJuridica()) {
            return isValidCNPJ(documentoLimpo);
        } else {
            return isValidCPF(documentoLimpo);
        }
    }

    private boolean isValidCPF(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += (cpf.charAt(i) - '0') * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) firstDigit = 0;

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += (cpf.charAt(i) - '0') * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) secondDigit = 0;

            return (cpf.charAt(9) - '0') == firstDigit && (cpf.charAt(10) - '0') == secondDigit;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidCNPJ(String cnpj) {
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int sum = 0;
            for (int i = 0; i < 12; i++) {
                sum += (cnpj.charAt(i) - '0') * weight1[i];
            }
            int firstDigit = sum % 11 < 2 ? 0 : 11 - (sum % 11);

            sum = 0;
            for (int i = 0; i < 13; i++) {
                sum += (cnpj.charAt(i) - '0') * weight2[i];
            }
            int secondDigit = sum % 11 < 2 ? 0 : 11 - (sum % 11);

            return (cnpj.charAt(12) - '0') == firstDigit && (cnpj.charAt(13) - '0') == secondDigit;
        } catch (Exception e) {
            return false;
        }
    }
}
