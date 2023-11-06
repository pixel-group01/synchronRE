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
    @Column(precision = 50, scale = 20)
    private BigDecimal repCapital;
    private String repCapitalLettre;
    @Column(precision = 50, scale = 20)
    private BigDecimal repPrime;
    @Column(precision = 50, scale = 20)
    private BigDecimal repTaux;
    @Column(precision = 50, scale = 20)
    private BigDecimal repSousCommission;
    @Column(precision = 50, scale = 20)
    private BigDecimal repTauxComCed;
    @Column(precision = 50, scale = 20)
    private BigDecimal repTauxComCourt;
    private String autreInterlocuteurs;
    @ManyToOne @JoinColumn(name = "rep_interlocuteur_principal_id")
    private Interlocuteur interlocuteurPrincipal;
    private boolean repStatut;
    @ManyToOne @JoinColumn(name = "sta_code")
    private Statut repStaCode;
    @ManyToOne @JoinColumn(name = "aff_id")
    private Affaire affaire;
    @Column(precision = 50, scale = 20)
    protected BigDecimal repCoursDevise;
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
