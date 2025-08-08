package com.desafio.infrastructure.web.controllers;

import com.desafio.application.dtos.response.ApiResponse;
import com.desafio.application.dtos.response.FilialResponse;
import com.desafio.application.mappers.FilialMapper;
import com.desafio.domain.services.FilialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/filiais")
@RequiredArgsConstructor
@Tag(name = "Filiais", description = "APIs para consulta de filiais")
public class FilialController {

    private final FilialService filialService;
    private final FilialMapper filialMapper;

    @GetMapping
    @Operation(summary = "Listar filiais", description = "Lista todas as filiais ativas")
    public ResponseEntity<ApiResponse<List<FilialResponse>>> listarFiliais() {
        log.info("Listando filiais");
        
        List<FilialResponse> filiais = filialService.findAll()
                .stream()
                .map(filialMapper::toResponse)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success(filiais));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar filial por ID", description = "Busca uma filial pelo seu ID")
    public ResponseEntity<ApiResponse<FilialResponse>> consultarFilial(
            @Parameter(description = "ID da filial") @PathVariable String id) {
        log.info("Consultando filial: {}", id);
        
        FilialResponse filial = filialService.findById(id)
                .map(filialMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Filial não encontrada"));
        
        return ResponseEntity.ok(ApiResponse.success(filial));
    }
}
