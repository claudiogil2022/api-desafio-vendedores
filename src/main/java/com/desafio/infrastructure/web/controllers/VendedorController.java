package com.desafio.infrastructure.web.controllers;

import com.desafio.application.dtos.request.AtualizarVendedorRequest;
import com.desafio.application.dtos.request.CriarVendedorRequest;
import com.desafio.application.dtos.response.ApiResponse;
import com.desafio.application.dtos.response.ProcessamentoResponse;
import com.desafio.application.dtos.response.VendedorResponse;
import com.desafio.application.usecases.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/vendedores")
@RequiredArgsConstructor
@Tag(name = "Vendedores", description = "APIs para gerenciamento de vendedores")
public class VendedorController {

    private final IniciarProcessamentoCriacaoVendedorUseCase iniciarProcessamentoCriacaoUseCase;
    private final ConsultarVendedorUseCase consultarVendedorUseCase;
    private final ListarVendedoresUseCase listarVendedoresUseCase;
    private final AtualizarVendedorUseCase atualizarVendedorUseCase;
    private final DeletarVendedorUseCase deletarVendedorUseCase;
    private final ConsultarVendedorPorProcessamentoUseCase consultarVendedorPorProcessamentoUseCase;

    @PostMapping
    @Operation(summary = "Criar vendedor", description = "Inicia o processamento assíncrono de criação de um vendedor")
    public ResponseEntity<ApiResponse<ProcessamentoResponse>> criarVendedor(
            @Valid @RequestBody CriarVendedorRequest request) {
        log.info("Recebida solicitação de criação de vendedor: {}", request.getNome());
        
        ProcessamentoResponse processamento = iniciarProcessamentoCriacaoUseCase.execute(request);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success("✅ Solicitação recebida! Vendedor sendo processado de forma assíncrona.\n" +
                        "📋 Use GET /api/v1/processamentos/" + processamento.getId() + " para consultar o status.\n" +
                        "🎯 Use GET /api/v1/processamentos/" + processamento.getId() + "/vendedor para acessar o vendedor após criação.", 
                        processamento));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar vendedor por ID", description = "Busca um vendedor pelo seu ID")
    public ResponseEntity<ApiResponse<VendedorResponse>> consultarVendedor(
            @Parameter(description = "ID do vendedor") @PathVariable String id) {
        log.info("Consultando vendedor por ID: {}", id);
        
        VendedorResponse vendedor = consultarVendedorUseCase.execute(id);
        
        return ResponseEntity.ok(ApiResponse.success(vendedor));
    }

    @GetMapping("/matricula/{matricula}")
    @Operation(summary = "Consultar vendedor por matrícula", description = "Busca um vendedor pela sua matrícula")
    public ResponseEntity<ApiResponse<VendedorResponse>> consultarVendedorPorMatricula(
            @Parameter(description = "Matrícula do vendedor") @PathVariable String matricula) {
        log.info("Consultando vendedor por matrícula: {}", matricula);
        
        VendedorResponse vendedor = consultarVendedorUseCase.executePorMatricula(matricula);
        
        return ResponseEntity.ok(ApiResponse.success(vendedor));
    }

    @GetMapping("/processamento/{processamentoId}")
    @Operation(summary = "Consultar vendedor por processamento", 
               description = "Consulta o vendedor criado através do ID do processamento inicial")
    public ResponseEntity<ApiResponse<VendedorResponse>> consultarVendedorPorProcessamento(
            @Parameter(description = "ID do processamento retornado na criação") @PathVariable String processamentoId) {
        log.info("Consultando vendedor por processamento: {}", processamentoId);
        
        VendedorResponse vendedor = consultarVendedorPorProcessamentoUseCase.execute(processamentoId);
        
        return ResponseEntity.ok(ApiResponse.success("✅ Vendedor encontrado!", vendedor));
    }

    @GetMapping
    @Operation(summary = "Listar vendedores", description = "Lista todos os vendedores ou filtra por filial")
    public ResponseEntity<ApiResponse<List<VendedorResponse>>> listarVendedores(
            @Parameter(description = "ID da filial para filtrar") @RequestParam(required = false) String filialId) {
        log.info("Listando vendedores{}", filialId != null ? " da filial: " + filialId : "");
        
        List<VendedorResponse> vendedores = filialId != null 
                ? listarVendedoresUseCase.executePorFilial(filialId)
                : listarVendedoresUseCase.execute();
        
        return ResponseEntity.ok(ApiResponse.success(vendedores));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vendedor", description = "Atualiza os dados de um vendedor")
    public ResponseEntity<ApiResponse<VendedorResponse>> atualizarVendedor(
            @Parameter(description = "ID do vendedor") @PathVariable String id,
            @Valid @RequestBody AtualizarVendedorRequest request) {
        log.info("Atualizando vendedor: {}", id);
        
        VendedorResponse vendedor = atualizarVendedorUseCase.execute(id, request);
        
        return ResponseEntity.ok(ApiResponse.success("Vendedor atualizado com sucesso", vendedor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar vendedor", description = "Remove um vendedor do sistema")
    public ResponseEntity<ApiResponse<Void>> deletarVendedor(
            @Parameter(description = "ID do vendedor") @PathVariable String id) {
        log.info("Deletando vendedor: {}", id);
        
        deletarVendedorUseCase.execute(id);
        
        return ResponseEntity.ok(ApiResponse.success("Vendedor deletado com sucesso", null));
    }
}
