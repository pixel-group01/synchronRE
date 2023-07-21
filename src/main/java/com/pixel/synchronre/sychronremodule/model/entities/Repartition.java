package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Entity
public class Repartition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repId;
    private BigDecimal repCapital;
    private String repCapitalLettre;
    private BigDecimal repTaux;
    private BigDecimal repSousCommission;
    private BigDecimal repTauxComCed;
    private BigDecimal repTauxComCourt;
    private String repInterlocuteur;
    private boolean repStatut;
    @ManyToOne @JoinColumn(name = "sta_code")
    private Statut repStaCode;
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

    @ManyToOne @JoinColumn(name = "sin_id")
    private Sinistre sinistre;

    public Repartition(Long repId) {
        this.repId = repId;
    }
}
