package com.desafio.domain.repositories;

import com.desafio.domain.entities.Vendedor;

import java.util.List;
import java.util.Optional;

public interface VendedorRepository {
    Vendedor save(Vendedor vendedor);
    Optional<Vendedor> findById(String id);
    Optional<Vendedor> findByMatricula(String matricula);
    List<Vendedor> findAll();
    List<Vendedor> findByFilialId(String filialId);
    void deleteById(String id);
    boolean existsByMatricula(String matricula);
    boolean existsByDocumento(String documento);
    boolean existsByEmail(String email);
    long getNextSequentialNumber();
}
