package com.desafio.infrastructure.persistence.mappers;

import com.desafio.domain.entities.Vendedor;
import com.desafio.infrastructure.persistence.entities.VendedorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VendedorEntityMapper {

    @Mapping(source = "filialId", target = "filialId")
    VendedorEntity toEntity(Vendedor vendedor);

    @Mapping(source = "filialId", target = "filialId")
    @Mapping(target = "filial", ignore = true)
    Vendedor toDomain(VendedorEntity entity);
}
