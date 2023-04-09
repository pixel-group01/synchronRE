package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class CessionLegale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cesLegId;
    private String cesLegLibelle;
    private Long cesLegCapital;
    private float cesLegTaux;
    @ManyToOne @JoinColumn(name = "affaireId")
    private Affaire affaire;
    @ManyToOne @JoinColumn(name = "cesParamCesLegId")
    private ParamCessionLegale paramCessionLegale;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
