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
    private BigDecimal repPrime;//Prime à reverser au cessionnaire
    private BigDecimal repPrimeNette;//Prime nette à reverser au cessionnaire
    @Column(precision = 50, scale = 20)
    private BigDecimal repTaux;
    @Column(precision = 50, scale = 20)
    private BigDecimal repSousCommission;
    @Column(precision = 50, scale = 20)
    private BigDecimal repTauxComCed;
    @Column(precision = 50, scale = 20)
    private BigDecimal repTauxComCourt;
    @Column(precision = 50, scale = 20)
    private BigDecimal repTauxComCourtPlaceur;
    @Column(precision = 50, scale = 20)
    private BigDecimal repMontantComCourt; //Prime à reverser à nre
    @Column(precision = 50, scale = 20)
    private BigDecimal repMontantCourtierPlaceur; //Prime à reverser au courtier placeur
    private String autreInterlocuteurs;
    @ManyToOne @JoinColumn(name = "rep_interlocuteur_principal_id")
    private Interlocuteur interlocuteurPrincipal;
    private boolean repStatut;
    @ManyToOne @JoinColumn(name = "sta_code")
    private Statut repStaCode;
    @ManyToOne @JoinColumn(name = "aff_id")
    private Affaire affaire;
    @ManyToOne @JoinColumn(name = "ced_trai_id")
    private CedanteTraite cedanteTraite;
    @ManyToOne @JoinColumn(name = "traite_np_id")
    private TraiteNonProportionnel traiteNonProportionnel;
    @Column(precision = 50, scale = 20)
    protected BigDecimal repCoursDevise;
    @ManyToOne @JoinColumn(name = "cessionnaire_id")
    private Cessionnaire cessionnaire;
    @ManyToOne @JoinColumn(name = "typ_id")
    private Type type;
    @ManyToOne @JoinColumn(name = "param_ces_legal_id")
    private ParamCessionLegale paramCessionLegale;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isAperiteur;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne @JoinColumn(name = "sin_id")
    private Sinistre sinistre;

    public Repartition(Long repId) {
        this.repId = repId;
    }

    @Override
    public String toString() {
        return String.valueOf(repId) ;
    }
}
