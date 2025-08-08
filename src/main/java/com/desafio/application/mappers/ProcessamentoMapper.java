package com.desafio.application.mappers;

import com.desafio.application.dtos.response.ProcessamentoResponse;
import com.desafio.domain.entities.ProcessamentoVendedor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProcessamentoMapper {
    ProcessamentoResponse toResponse(ProcessamentoVendedor processamento);
}
