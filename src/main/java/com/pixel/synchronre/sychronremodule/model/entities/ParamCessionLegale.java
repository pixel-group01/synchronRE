package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class ParamCessionLegale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paramCesLegId;
    private String paramCesLegLibelle;
    private BigDecimal paramCesLegCapital;
    private BigDecimal paramCesLegTaux;
    @ManyToOne @JoinColumn(name = "pays_code")
    private Pays pays;
    @ManyToOne @JoinColumn(name = "paramStatut")
    private Statut statut;
    private Long numOdre;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public ParamCessionLegale(Long paramCesLegalId)
    {
        this.paramCesLegId = paramCesLegalId;
    }
}
