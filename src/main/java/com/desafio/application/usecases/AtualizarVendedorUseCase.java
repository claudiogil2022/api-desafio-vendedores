package com.desafio.application.usecases;

import com.desafio.application.dtos.request.AtualizarVendedorRequest;
import com.desafio.application.dtos.response.VendedorResponse;
import com.desafio.application.mappers.VendedorMapper;
import com.desafio.domain.entities.Vendedor;
import com.desafio.domain.repositories.VendedorRepository;
import com.desafio.domain.services.FilialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AtualizarVendedorUseCase {

    private final VendedorRepository vendedorRepository;
    private final VendedorMapper vendedorMapper;
    private final FilialService filialService;

    @Transactional
    public VendedorResponse execute(String vendedorId, AtualizarVendedorRequest request) {
        Vendedor vendedor = vendedorRepository.findById(vendedorId)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));

        // Validações de negócio
        validarRegrasNegocio(vendedor, request);

        // Atualizar campos
        vendedorMapper.updateFromRequest(request, vendedor);
        
        // Atualizar filial se fornecida
        if (request.getFilialId() != null) {
            vendedor.setFilial(filialService.findById(request.getFilialId())
                    .orElseThrow(() -> new IllegalArgumentException("Filial não encontrada")));
            vendedor.setFilialId(request.getFilialId());
        }

        // Validar documento se foi alterado
        if (request.getDocumento() != null && !vendedor.isDocumentoValido()) {
            throw new IllegalArgumentException("Documento inválido");
        }

        vendedor.setUpdatedAt(LocalDateTime.now());
        vendedor = vendedorRepository.save(vendedor);

        return vendedorMapper.toResponse(vendedor);
    }

    private void validarRegrasNegocio(Vendedor vendedorExistente, AtualizarVendedorRequest request) {
        // Validar filial se fornecida
        if (request.getFilialId() != null && !filialService.isFilialAtiva(request.getFilialId())) {
            throw new IllegalArgumentException("Filial não encontrada ou inativa");
        }

        // Validar unicidade do documento se foi alterado
        if (request.getDocumento() != null && 
            !request.getDocumento().equals(vendedorExistente.getDocumento()) &&
            vendedorRepository.existsByDocumento(request.getDocumento())) {
            throw new IllegalArgumentException("Já existe um vendedor com este documento");
        }

        // Validar unicidade do email se foi alterado
        if (request.getEmail() != null && 
            !request.getEmail().equals(vendedorExistente.getEmail()) &&
            vendedorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Já existe um vendedor com este email");
        }

        // Validar tipo de documento se o tipo de contratação foi alterado
        if (request.getTipoContratacao() != null && 
            request.getTipoContratacao() != vendedorExistente.getTipoContratacao()) {
            validarTipoDocumento(vendedorExistente.getDocumento(), request.getTipoContratacao());
        }
    }

    private void validarTipoDocumento(String documento, com.desafio.domain.enums.TipoContratacao tipoContratacao) {
        String documentoLimpo = documento.replaceAll("\\D", "");
        
        if (tipoContratacao == com.desafio.domain.enums.TipoContratacao.PESSOA_JURIDICA) {
            if (documentoLimpo.length() != 14) {
                throw new IllegalArgumentException("Pessoa Jurídica deve ter CNPJ (14 dígitos)");
            }
        } else {
            if (documentoLimpo.length() != 11) {
                throw new IllegalArgumentException("Pessoa Física deve ter CPF (11 dígitos)");
            }
        }
    }
}
