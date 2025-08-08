package com.desafio.application.usecases;

import com.desafio.application.dtos.response.VendedorResponse;
import com.desafio.application.mappers.VendedorMapper;
import com.desafio.domain.entities.ProcessamentoVendedor;
import com.desafio.domain.enums.StatusProcessamento;
import com.desafio.domain.repositories.ProcessamentoVendedorRepository;
import com.desafio.domain.repositories.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarVendedorPorProcessamentoUseCase {

    private final ProcessamentoVendedorRepository processamentoRepository;
    private final VendedorRepository vendedorRepository;
    private final VendedorMapper vendedorMapper;

    public VendedorResponse execute(String processamentoId) {
        ProcessamentoVendedor processamento = processamentoRepository.findById(processamentoId)
                .orElseThrow(() -> new RuntimeException("Processamento não encontrado"));

        if (processamento.getStatus() != StatusProcessamento.CONCLUIDO) {
            throw new RuntimeException("Vendedor ainda não foi criado. Status: " + processamento.getStatus().getDescricao());
        }

        if (processamento.getVendedorId() == null) {
            throw new RuntimeException("Processamento concluído mas vendedor não encontrado");
        }

        return vendedorRepository.findById(processamento.getVendedorId())
                .map(vendedorMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
    }
}
