package com.desafio.infrastructure.persistence.repositories.impl;

import com.desafio.domain.entities.Vendedor;
import com.desafio.domain.repositories.VendedorRepository;
import com.desafio.domain.services.FilialService;
import com.desafio.infrastructure.persistence.mappers.VendedorEntityMapper;
import com.desafio.infrastructure.persistence.repositories.VendedorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VendedorRepositoryImpl implements VendedorRepository {

    private final VendedorJpaRepository jpaRepository;
    private final VendedorEntityMapper mapper;
    private final FilialService filialService;

    @Override
    public Vendedor save(Vendedor vendedor) {
        var entity = mapper.toEntity(vendedor);
        
        // Se o vendedor já existe (UPDATE), preservar createdAt
        if (vendedor.getId() != null) {
            var existingEntity = jpaRepository.findById(vendedor.getId());
            if (existingEntity.isPresent()) {
                entity.setCreatedAt(existingEntity.get().getCreatedAt());
            }
        }
        
        var savedEntity = jpaRepository.save(entity);
        var domain = mapper.toDomain(savedEntity);
        
        // Carregar filial se necessário
        if (domain.getFilialId() != null) {
            domain.setFilial(filialService.findById(domain.getFilialId()).orElse(null));
        }
        
        return domain;
    }

    @Override
    public Optional<Vendedor> findById(String id) {
        return jpaRepository.findById(id)
                .map(entity -> {
                    var domain = mapper.toDomain(entity);
                    if (domain.getFilialId() != null) {
                        domain.setFilial(filialService.findById(domain.getFilialId()).orElse(null));
                    }
                    return domain;
                });
    }

    @Override
    public Optional<Vendedor> findByMatricula(String matricula) {
        return jpaRepository.findByMatricula(matricula)
                .map(entity -> {
                    var domain = mapper.toDomain(entity);
                    if (domain.getFilialId() != null) {
                        domain.setFilial(filialService.findById(domain.getFilialId()).orElse(null));
                    }
                    return domain;
                });
    }

    @Override
    public List<Vendedor> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(entity -> {
                    var domain = mapper.toDomain(entity);
                    if (domain.getFilialId() != null) {
                        domain.setFilial(filialService.findById(domain.getFilialId()).orElse(null));
                    }
                    return domain;
                })
                .toList();
    }

    @Override
    public List<Vendedor> findByFilialId(String filialId) {
        return jpaRepository.findByFilialId(filialId)
                .stream()
                .map(entity -> {
                    var domain = mapper.toDomain(entity);
                    if (domain.getFilialId() != null) {
                        domain.setFilial(filialService.findById(domain.getFilialId()).orElse(null));
                    }
                    return domain;
                })
                .toList();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByMatricula(String matricula) {
        return jpaRepository.existsByMatricula(matricula);
    }

    @Override
    public boolean existsByDocumento(String documento) {
        return jpaRepository.existsByDocumento(documento);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public long getNextSequentialNumber() {
        return jpaRepository.getNextSequentialNumber();
    }
}
