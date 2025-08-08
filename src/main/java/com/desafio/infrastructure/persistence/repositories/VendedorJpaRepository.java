package com.desafio.infrastructure.persistence.repositories;

import com.desafio.infrastructure.persistence.entities.VendedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendedorJpaRepository extends JpaRepository<VendedorEntity, String> {
    
    Optional<VendedorEntity> findByMatricula(String matricula);
    
    List<VendedorEntity> findByFilialId(String filialId);
    
    boolean existsByMatricula(String matricula);
    
    boolean existsByDocumento(String documento);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(v.matricula, 1, 8) AS long)), 0) + 1 FROM VendedorEntity v")
    long getNextSequentialNumber();
}
