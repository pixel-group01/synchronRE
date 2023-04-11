package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class ParamCessionLegale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paramCesLegId;
    private String paramCesLegLibelle;
    private float paramCesLegCapital;
    private float paramCesLegTaux;
    @ManyToOne @JoinColumn(name = "paysId")
    private Pays pays;
    @ManyToOne @JoinColumn(name = "cedId")
    private Cedante cedante;
    @ManyToOne @JoinColumn(name = "paramStatut")
    private Statut statut;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public ParamCessionLegale(Long paramCesLegalId)
    {
        this.paramCesLegId = paramCesLegalId;
    }
}
