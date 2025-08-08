package com.desafio.infrastructure.persistence.mappers;

import com.desafio.domain.entities.ProcessamentoVendedor;
import com.desafio.infrastructure.persistence.entities.ProcessamentoVendedorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProcessamentoEntityMapper {

    ProcessamentoVendedorEntity toEntity(ProcessamentoVendedor processamento);

    ProcessamentoVendedor toDomain(ProcessamentoVendedorEntity entity);
}
