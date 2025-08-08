package com.desafio.application.mappers;

import com.desafio.application.dtos.request.AtualizarVendedorRequest;
import com.desafio.application.dtos.request.CriarVendedorRequest;
import com.desafio.application.dtos.response.VendedorResponse;
import com.desafio.domain.entities.Vendedor;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VendedorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "matricula", ignore = true)
    @Mapping(target = "filial", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vendedor toEntity(CriarVendedorRequest request);

    VendedorResponse toResponse(Vendedor vendedor);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "matricula", ignore = true)
    @Mapping(target = "filial", ignore = true)
    @Mapping(target = "filialId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(AtualizarVendedorRequest request, @MappingTarget Vendedor vendedor);
}
