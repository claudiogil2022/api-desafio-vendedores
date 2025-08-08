package com.desafio.domain.repositories;

import com.desafio.domain.entities.ProcessamentoVendedor;
import com.desafio.domain.enums.StatusProcessamento;

import java.util.List;
import java.util.Optional;

public interface ProcessamentoVendedorRepository {
    ProcessamentoVendedor save(ProcessamentoVendedor processamento);
    Optional<ProcessamentoVendedor> findById(String id);
    List<ProcessamentoVendedor> findByStatus(StatusProcessamento status);
    List<ProcessamentoVendedor> findAll();
    void deleteById(String id);
}
