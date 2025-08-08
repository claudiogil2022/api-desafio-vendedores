package com.desafio.infrastructure.persistence.repositories.impl;

import com.desafio.domain.entities.ProcessamentoVendedor;
import com.desafio.domain.enums.StatusProcessamento;
import com.desafio.domain.repositories.ProcessamentoVendedorRepository;
import com.desafio.infrastructure.persistence.mappers.ProcessamentoEntityMapper;
import com.desafio.infrastructure.persistence.repositories.ProcessamentoVendedorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProcessamentoVendedorRepositoryImpl implements ProcessamentoVendedorRepository {

    private final ProcessamentoVendedorJpaRepository jpaRepository;
    private final ProcessamentoEntityMapper mapper;

    @Override
    public ProcessamentoVendedor save(ProcessamentoVendedor processamento) {
        var entity = mapper.toEntity(processamento);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ProcessamentoVendedor> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<ProcessamentoVendedor> findByStatus(StatusProcessamento status) {
        return jpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ProcessamentoVendedor> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
