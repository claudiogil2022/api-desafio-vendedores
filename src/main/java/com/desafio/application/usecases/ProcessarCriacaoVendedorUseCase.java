package com.desafio.application.usecases;

import com.desafio.application.dtos.request.CriarVendedorRequest;
import com.desafio.application.mappers.VendedorMapper;
import com.desafio.domain.entities.ProcessamentoVendedor;
import com.desafio.domain.entities.Vendedor;
import com.desafio.domain.enums.TipoContratacao;
import com.desafio.domain.repositories.ProcessamentoVendedorRepository;
import com.desafio.domain.repositories.VendedorRepository;
import com.desafio.domain.services.FilialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Processamento assíncrono de criação de vendedores.
 * 
 * @author Claudio Gil
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessarCriacaoVendedorUseCase {

    private final VendedorRepository vendedorRepository;
    private final ProcessamentoVendedorRepository processamentoRepository;
    private final VendedorMapper vendedorMapper;
    private final FilialService filialService;

    @Transactional
    public void execute(String processamentoId, CriarVendedorRequest request) {
        ProcessamentoVendedor processamento = processamentoRepository.findById(processamentoId)
                .orElseThrow(() -> new RuntimeException("Processamento não encontrado"));

        try {
            processamento.marcarComoProcessando();
            processamentoRepository.save(processamento);

            // TODO: Implementar validação mais robusta de filiais
            validarRegrasNegocio(request);

            // FIXME: Otimizar geração de matrícula - pode gerar duplicatas em alta concorrência
            Vendedor vendedor = criarVendedor(request);
            vendedor = vendedorRepository.save(vendedor);

            processamento.marcarComoConcluido(vendedor.getId());
            processamentoRepository.save(processamento);

            log.info("Vendedor criado: {}", vendedor.getId());

        } catch (Exception e) {
            log.error("Falha ao processar vendedor - processamento: {}", processamentoId, e);
            processamento.marcarComoErro(e.getMessage());
            processamentoRepository.save(processamento);
        }
    }

    private void validarRegrasNegocio(CriarVendedorRequest request) {
        // Verificar se filial existe e está ativa
        if (!filialService.isFilialAtiva(request.getFilialId())) {
            throw new IllegalArgumentException("Filial não encontrada ou inativa");
        }

        // Check documento único
        if (vendedorRepository.existsByDocumento(request.getDocumento())) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }

        // Check email único  
        if (vendedorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        validarDocumento(request.getDocumento(), request.getTipoContratacao());
    }

    private void validarDocumento(String documento, TipoContratacao tipoContratacao) {
        String doc = documento.replaceAll("\\D", "");
        
        // Validação básica de tamanho conforme tipo
        if (tipoContratacao == TipoContratacao.PESSOA_JURIDICA) {
            if (doc.length() != 14) {
                throw new IllegalArgumentException("CNPJ deve ter 14 dígitos");
            }
        } else {
            if (doc.length() != 11) {
                throw new IllegalArgumentException("CPF deve ter 11 dígitos");
            }
        }
    }

    private Vendedor criarVendedor(CriarVendedorRequest request) {
        Vendedor vendedor = vendedorMapper.toEntity(request);
        
        vendedor.setId(UUID.randomUUID().toString());
        vendedor.setMatricula(gerarMatricula(request.getTipoContratacao()));
        
        // Validação do algoritmo de CPF/CNPJ
        if (!vendedor.isDocumentoValido()) {
            throw new IllegalArgumentException("Documento inválido");
        }
        
        vendedor.setFilial(filialService.findById(request.getFilialId())
                .orElseThrow(() -> new IllegalArgumentException("Filial não encontrada")));
        
        LocalDateTime agora = LocalDateTime.now();
        vendedor.setCreatedAt(agora);
        vendedor.setUpdatedAt(agora);
        
        return vendedor;
    }

    /**
     * Gera matrícula sequencial por tipo de contratação.
     * Formato: XXXXXXXX-TIPO
     */
    private String gerarMatricula(TipoContratacao tipoContratacao) {
        long proximo = vendedorRepository.getNextSequentialNumber();
        String sufixo = tipoContratacao.getSufixoMatricula();
        return String.format("%08d-%s", proximo, sufixo);
    }
}
