package com.desafio.application.usecases;

import com.desafio.application.dtos.response.VendedorResponse;
import com.desafio.application.mappers.VendedorMapper;
import com.desafio.domain.repositories.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarVendedoresUseCase {

    private final VendedorRepository vendedorRepository;
    private final VendedorMapper vendedorMapper;

    public List<VendedorResponse> execute() {
        return vendedorRepository.findAll()
                .stream()
                .map(vendedorMapper::toResponse)
                .toList();
    }

    public List<VendedorResponse> executePorFilial(String filialId) {
        return vendedorRepository.findByFilialId(filialId)
                .stream()
                .map(vendedorMapper::toResponse)
                .toList();
    }
}
