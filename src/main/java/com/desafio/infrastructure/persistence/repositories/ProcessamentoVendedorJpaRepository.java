package com.desafio.infrastructure.persistence.repositories;

import com.desafio.infrastructure.persistence.entities.ProcessamentoVendedorEntity;
import com.desafio.domain.enums.StatusProcessamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessamentoVendedorJpaRepository extends JpaRepository<ProcessamentoVendedorEntity, String> {
    
    List<ProcessamentoVendedorEntity> findByStatus(StatusProcessamento status);
}
