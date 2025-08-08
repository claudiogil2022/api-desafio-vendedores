package com.desafio.application.usecases;

import com.desafio.application.dtos.response.ProcessamentoResponse;
import com.desafio.application.mappers.ProcessamentoMapper;
import com.desafio.domain.repositories.ProcessamentoVendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarProcessamentoUseCase {

    private final ProcessamentoVendedorRepository processamentoRepository;
    private final ProcessamentoMapper processamentoMapper;

    public ProcessamentoResponse execute(String processamentoId) {
        return processamentoRepository.findById(processamentoId)
                .map(processamentoMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Processamento não encontrado"));
    }
}
