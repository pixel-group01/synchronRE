package com.pixel.synchronre.sychronremodule.model.entities;


import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Entity
public class Repartition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repId;
    private Float repCapital;
    private Float repTaux;
    private Float repSousCommission;
    private String repInterlocuteur;
    private boolean repStatut;
    @ManyToOne @JoinColumn(name = "aff_id")
    private Affaire affaire;
    @ManyToOne @JoinColumn(name = "cessionnaire_id")
    private Cessionnaire cessionnaire;
    @ManyToOne @JoinColumn(name = "typ_id")
    private Type type;
    @ManyToOne @JoinColumn(name = "param_ces_legal_id")
    private ParamCessionLegale paramCessionLegale;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
