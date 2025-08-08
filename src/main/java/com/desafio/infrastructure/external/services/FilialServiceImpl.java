package com.desafio.infrastructure.external.services;

import com.desafio.domain.entities.Filial;
import com.desafio.domain.services.FilialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementação mock do serviço de filiais.
 */
@Slf4j
@Service
public class FilialServiceImpl implements FilialService {

    // TODO: Substituir por integração real com API de filiais
    private final List<Filial> dadosMock = List.of(
            Filial.builder()
                    .id("1")
                    .nome("Filial São Paulo")
                    .cnpj("11222333000101")
                    .cidade("São Paulo")
                    .uf("SP")
                    .tipo("Matriz")
                    .ativo(true)
                    .dataCadastro(LocalDateTime.of(2020, 1, 1, 0, 0))
                    .ultimaAtualizacao(LocalDateTime.now())
                    .build(),
            Filial.builder()
                    .id("2")
                    .nome("Filial Rio de Janeiro")
                    .cnpj("11222333000202")
                    .cidade("Rio de Janeiro")
                    .uf("RJ")
                    .tipo("Filial")
                    .ativo(true)
                    .dataCadastro(LocalDateTime.of(2020, 6, 15, 0, 0))
                    .ultimaAtualizacao(LocalDateTime.now())
                    .build(),
            Filial.builder()
                    .id("3")
                    .nome("Filial Belo Horizonte")
                    .cnpj("11222333000303")
                    .cidade("Belo Horizonte")
                    .uf("MG")
                    .tipo("Filial")
                    .ativo(true)
                    .dataCadastro(LocalDateTime.of(2021, 3, 10, 0, 0))
                    .ultimaAtualizacao(LocalDateTime.now())
                    .build(),
            Filial.builder()
                    .id("4")
                    .nome("Filial Inativa") // Para testes
                    .cnpj("11222333000404")
                    .cidade("Salvador")
                    .uf("BA")
                    .tipo("Filial")
                    .ativo(false)
                    .dataCadastro(LocalDateTime.of(2019, 12, 1, 0, 0))
                    .ultimaAtualizacao(LocalDateTime.now())
                    .build()
    );

    @Override
    @Cacheable("filiais")
    public Optional<Filial> findById(String id) {
        log.debug("Consultando filial: {}", id);
        
        // Simula latência de rede
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return dadosMock.stream()
                .filter(filial -> filial.getId().equals(id))
                .findFirst();
    }

    @Override
    @Cacheable("filiais-ativas")
    public List<Filial> findAll() {
        log.debug("Listando filiais ativas");
        
        // Simula latência de rede
        try {
            Thread.sleep(150); // Chamada mais pesada
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return dadosMock.stream()
                .filter(Filial::getAtivo)
                .toList();
    }

    @Override
    @Cacheable("filial-ativa")
    public boolean isFilialAtiva(String id) {
        log.debug("Verificando status da filial: {}", id);
        
        return findById(id)
                .map(Filial::getAtivo)
                .orElse(false);
    }
}
