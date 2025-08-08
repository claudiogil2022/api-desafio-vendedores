package com.desafio.domain.services;

import com.desafio.domain.entities.Filial;

import java.util.List;
import java.util.Optional;

public interface FilialService {
    Optional<Filial> findById(String id);
    List<Filial> findAll();
    boolean isFilialAtiva(String id);
}
