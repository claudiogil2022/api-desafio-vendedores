package com.desafio.application.usecases;

import com.desafio.domain.repositories.VendedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeletarVendedorUseCase {

    private final VendedorRepository vendedorRepository;

    @Transactional
    public void execute(String vendedorId) {
        if (!vendedorRepository.findById(vendedorId).isPresent()) {
            throw new RuntimeException("Vendedor não encontrado");
        }

        vendedorRepository.deleteById(vendedorId);
        log.info("Vendedor deletado com sucesso: {}", vendedorId);
    }
}
