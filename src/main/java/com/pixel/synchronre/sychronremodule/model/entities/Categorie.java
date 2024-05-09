package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
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
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Categorie(String categorieLibelle, BigDecimal categorieCapacite) {
        this.categorieLibelle = categorieLibelle;
        this.categorieCapacite = categorieCapacite;
    }
}
