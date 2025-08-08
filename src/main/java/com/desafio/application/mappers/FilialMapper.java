package com.desafio.application.mappers;

import com.desafio.application.dtos.response.FilialResponse;
import com.desafio.domain.entities.Filial;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FilialMapper {
    FilialResponse toResponse(Filial filial);
}
