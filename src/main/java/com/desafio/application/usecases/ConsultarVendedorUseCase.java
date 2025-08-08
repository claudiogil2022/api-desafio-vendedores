package com.desafio.application.usecases;

import com.desafio.application.dtos.response.VendedorResponse;
import com.desafio.application.mappers.VendedorMapper;
import com.desafio.domain.repositories.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarVendedorUseCase {

    private final VendedorRepository vendedorRepository;
    private final VendedorMapper vendedorMapper;

    public VendedorResponse execute(String vendedorId) {
        return vendedorRepository.findById(vendedorId)
                .map(vendedorMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
    }

    public VendedorResponse executePorMatricula(String matricula) {
        return vendedorRepository.findByMatricula(matricula)
                .map(vendedorMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
    }
}
