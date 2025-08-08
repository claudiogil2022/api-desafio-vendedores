package com.desafio.application.usecases;

import com.desafio.application.dtos.request.CriarVendedorRequest;
import com.desafio.application.dtos.response.ProcessamentoResponse;
import com.desafio.application.mappers.ProcessamentoMapper;
import com.desafio.domain.entities.ProcessamentoVendedor;
import com.desafio.domain.enums.StatusProcessamento;
import com.desafio.domain.repositories.ProcessamentoVendedorRepository;
import com.desafio.domain.services.FilialService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IniciarProcessamentoCriacaoVendedorUseCase {

    private final ProcessamentoVendedorRepository processamentoRepository;
    private final ProcessamentoMapper processamentoMapper;
    private final ObjectMapper objectMapper;
    private final FilialService filialService;
    private final ProcessarCriacaoVendedorUseCase processarCriacaoVendedorUseCase;

    public ProcessamentoResponse execute(CriarVendedorRequest request) {
        log.info("Iniciando processamento de criação de vendedor");

        // Validação básica da filial
        if (!filialService.isFilialAtiva(request.getFilialId())) {
            throw new IllegalArgumentException("Filial não encontrada ou inativa");
        }

        // Criar registro de processamento
        ProcessamentoVendedor processamento = criarProcessamento(request);
        processamento = processamentoRepository.save(processamento);

        // Iniciar processamento assíncrono
        processarAssincrono(processamento.getId(), request);

        return processamentoMapper.toResponse(processamento);
    }

    private ProcessamentoVendedor criarProcessamento(CriarVendedorRequest request) {
        try {
            String dadosOriginais = objectMapper.writeValueAsString(request);
            return ProcessamentoVendedor.builder()
                    .id(UUID.randomUUID().toString())
                    .status(StatusProcessamento.PENDENTE)
                    .dadosOriginais(dadosOriginais)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar dados do vendedor", e);
            throw new RuntimeException("Erro interno do servidor");
        }
    }

    @Async
    public void processarAssincrono(String processamentoId, CriarVendedorRequest request) {
        log.info("Processamento assíncrono iniciado para ID: {}", processamentoId);
        processarCriacaoVendedorUseCase.execute(processamentoId, request);
    }
}
