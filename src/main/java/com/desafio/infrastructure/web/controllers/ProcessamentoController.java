package com.desafio.infrastructure.web.controllers;

import com.desafio.application.dtos.response.ApiResponse;
import com.desafio.application.dtos.response.ProcessamentoResponse;
import com.desafio.application.usecases.ConsultarProcessamentoUseCase;
import com.desafio.domain.enums.StatusProcessamento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/processamentos")
@RequiredArgsConstructor
@Tag(name = "Processamentos", description = "APIs para consulta de status de processamentos")
public class ProcessamentoController {

    private final ConsultarProcessamentoUseCase consultarProcessamentoUseCase;

    @GetMapping("/{id}")
    @Operation(summary = "Consultar processamento", description = "Consulta o status de um processamento pelo ID")
    public ResponseEntity<ApiResponse<ProcessamentoResponse>> consultarProcessamento(
            @Parameter(description = "ID do processamento") @PathVariable String id) {
        log.info("Consultando processamento: {}", id);
        
        ProcessamentoResponse processamento = consultarProcessamentoUseCase.execute(id);
        
        String message = getStatusMessage(processamento.getStatus(), processamento.getErro());
        
        return ResponseEntity.ok(ApiResponse.success(message, processamento));
    }

    @GetMapping("/{id}/vendedor")
    @Operation(summary = "Consultar vendedor por processamento", 
               description = "Consulta diretamente o vendedor criado através do ID do processamento")
    public ResponseEntity<ApiResponse<Object>> consultarVendedorPorProcessamento(
            @Parameter(description = "ID do processamento") @PathVariable String id) {
        log.info("Consultando vendedor através do processamento: {}", id);
        
        ProcessamentoResponse processamento = consultarProcessamentoUseCase.execute(id);
        
        // Se não foi concluído, retorna status do processamento
        if (processamento.getStatus() != StatusProcessamento.CONCLUIDO) {
            String message = getStatusMessage(processamento.getStatus(), processamento.getErro());
            return ResponseEntity.ok(ApiResponse.success(message, processamento));
        }
        
        // Se concluído, redireciona para o vendedor
        if (processamento.getVendedorId() != null) {
            return ResponseEntity.status(302)
                    .header("Location", "/api/v1/vendedores/" + processamento.getVendedorId())
                    .body(ApiResponse.success("✅ Vendedor criado! Redirecionando para os dados do vendedor.", 
                            processamento));
        }
        
        return ResponseEntity.ok(ApiResponse.success("⚠️ Processamento concluído mas vendedor não encontrado.", processamento));
    }

    private String getStatusMessage(StatusProcessamento status, String erro) {
        switch (status) {
            case PENDENTE:
                return "⏳ Processamento iniciado. Aguarde enquanto validamos os dados...";
            case PROCESSANDO:
                return "🔄 Validando dados e criando vendedor. Quase pronto!";
            case CONCLUIDO:
                return "🎉 Vendedor criado com sucesso! Já está disponível no sistema.";
            case ERRO:
                return erro != null ? "❌ " + erro : "❌ Ocorreu um erro durante o processamento.";
            default:
                return "📋 Status do processamento: " + status.getDescricao();
        }
    }
}
