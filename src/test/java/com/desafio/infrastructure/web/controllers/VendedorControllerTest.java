package com.desafio.infrastructure.web.controllers;

import com.desafio.application.dtos.request.CriarVendedorRequest;
import com.desafio.application.dtos.response.ProcessamentoResponse;
import com.desafio.application.usecases.IniciarProcessamentoCriacaoVendedorUseCase;
import com.desafio.domain.enums.StatusProcessamento;
import com.desafio.domain.enums.TipoContratacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendedorController.class)
class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IniciarProcessamentoCriacaoVendedorUseCase iniciarProcessamentoCriacaoUseCase;

    // Mock other use cases as needed
    @MockBean
    private com.desafio.application.usecases.ConsultarVendedorUseCase consultarVendedorUseCase;
    @MockBean
    private com.desafio.application.usecases.ListarVendedoresUseCase listarVendedoresUseCase;
    @MockBean
    private com.desafio.application.usecases.AtualizarVendedorUseCase atualizarVendedorUseCase;
    @MockBean
    private com.desafio.application.usecases.DeletarVendedorUseCase deletarVendedorUseCase;

    @Test
    void deveCriarVendedorComSucesso() throws Exception {
        // Arrange
        CriarVendedorRequest request = new CriarVendedorRequest();
        request.setNome("João Silva");
        request.setDataNascimento(LocalDate.of(1990, 1, 1));
        request.setDocumento("11144477735");
        request.setEmail("joao@email.com");
        request.setTipoContratacao(TipoContratacao.CLT);
        request.setFilialId("1");

        ProcessamentoResponse response = ProcessamentoResponse.builder()
                .id("proc-123")
                .status(StatusProcessamento.PENDENTE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(iniciarProcessamentoCriacaoUseCase.execute(any(CriarVendedorRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("proc-123"))
                .andExpect(jsonPath("$.data.status").value("PENDENTE"));
    }

    @Test
    void deveRejeitarRequestInvalido() throws Exception {
        // Arrange
        CriarVendedorRequest request = new CriarVendedorRequest();
        // Request vazio - deve falhar na validação

        // Act & Assert
        mockMvc.perform(post("/api/v1/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
