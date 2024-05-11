package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAT_ID_GEN")
    @SequenceGenerator(name = "CAT_ID_GEN", sequenceName = "CAT_ID_GEN")
    private Long categorieId;
    private String categorieLibelle;
    private BigDecimal categorieCapacite;
    @ManyToOne @JoinColumn(name = "STA_CODE")
    private Statut statut;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Categorie(String categorieLibelle, BigDecimal categorieCapacite) {
        this.categorieLibelle = categorieLibelle;
        this.categorieCapacite = categorieCapacite;
    }

    public Categorie(Long categorieId) {
        this.categorieId = categorieId;
    }
}
