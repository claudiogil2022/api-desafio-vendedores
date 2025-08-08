package com.desafio.infrastructure.persistence.entities;

import com.desafio.domain.enums.StatusProcessamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "processamento_vendedores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessamentoVendedorEntity {

    @Id
    private String id;

    @Column(name = "vendedor_id")
    private String vendedorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProcessamento status;

    @Column(name = "dados_originais", columnDefinition = "TEXT")
    private String dadosOriginais;

    @Column(columnDefinition = "TEXT")
    private String erro;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
