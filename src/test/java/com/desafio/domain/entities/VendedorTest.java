package com.desafio.domain.entities;

import com.desafio.domain.enums.TipoContratacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para validação de documentos de vendedores.
 */
class VendedorTest {

    @Test
    @DisplayName("Deve validar CPF com algoritmo correto")
    void deveValidarCPFCorreto() {
        // Given
        Vendedor vendedor = criarVendedorPessoaFisica("11144477735"); // CPF válido real

        // When & Then
        assertTrue(vendedor.isDocumentoValido(), "CPF válido deve passar na validação");
    }

    @Test 
    @DisplayName("Deve validar CNPJ com algoritmo correto")
    void deveValidarCNPJCorreto() {
        // Given  
        Vendedor vendedor = criarVendedorPessoaJuridica("11222333000181"); // CNPJ válido real

        // When & Then
        assertTrue(vendedor.isDocumentoValido(), "CNPJ válido deve passar na validação");
    }

    @Test
    @DisplayName("Deve rejeitar CPF com dígitos repetidos")
    void deveRejeitarCPFInvalido() {
        // Given
        Vendedor vendedor = criarVendedorPessoaFisica("11111111111");

        // When & Then  
        assertFalse(vendedor.isDocumentoValido(), "CPF com dígitos iguais deve ser inválido");
    }

    @Test
    @DisplayName("Deve rejeitar CNPJ com dígitos repetidos") 
    void deveRejeitarCNPJInvalido() {
        // Given
        Vendedor vendedor = criarVendedorPessoaJuridica("11111111111111");

        // When & Then
        assertFalse(vendedor.isDocumentoValido(), "CNPJ com dígitos iguais deve ser inválido");
    }

    @Test
    @DisplayName("Deve identificar pessoa jurídica corretamente")
    void deveIdentificarPessoaJuridica() {
        // Given
        Vendedor pj = Vendedor.builder()
                .tipoContratacao(TipoContratacao.PESSOA_JURIDICA)
                .build();

        // When & Then
        assertTrue(pj.isPessoaJuridica());
    }

    @Test
    @DisplayName("Deve identificar pessoa física corretamente")
    void deveIdentificarPessoaFisica() {
        // Given
        Vendedor pf = Vendedor.builder()
                .tipoContratacao(TipoContratacao.CLT)
                .build();

        // When & Then
        assertFalse(pf.isPessoaJuridica());
    }

    // Métodos auxiliares para criação de vendedores de teste
    private Vendedor criarVendedorPessoaFisica(String cpf) {
        return Vendedor.builder()
                .id("1")
                .matricula("00000001-CLT")
                .nome("João Silva")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .documento(cpf)
                .email("joao@email.com")
                .tipoContratacao(TipoContratacao.CLT)
                .filialId("1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Vendedor criarVendedorPessoaJuridica(String cnpj) {
        return Vendedor.builder()
                .id("1")
                .matricula("00000001-PJ")
                .nome("Empresa LTDA")
                .documento(cnpj)
                .email("empresa@email.com")
                .tipoContratacao(TipoContratacao.PESSOA_JURIDICA)
                .filialId("1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
