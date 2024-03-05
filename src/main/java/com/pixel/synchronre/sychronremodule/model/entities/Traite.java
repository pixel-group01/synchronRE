package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Traite
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAITE_ID_GEN")
    @SequenceGenerator(name = "TRAITE_ID_GEN", sequenceName = "TRAITE_ID_GEN")
    protected Long traiId;
    protected String traiCode;
    protected String traiLibelle;
    protected Long traiPreavis;
    protected Long traiDure;
    protected Long traiDelaiCompte;
    protected Long traiDelaiPaiement;
    protected String traiPeriodicite;
    protected LocalDate traiDateEffet;
    protected LocalDate traiDateRenouvellement;
    protected BigDecimal traiTaux;
    protected BigDecimal traiAlimentEst;
    protected BigDecimal traiTauxResultatEstime;
    protected BigDecimal traiTauxSurcommission;
    protected BigDecimal traiTauxCourtier;
    protected BigDecimal traiTauxImpot;
    protected BigDecimal traiTauxAutreCharge;
    protected BigDecimal traiTauxParticipation;
    protected BigDecimal traiTauxFraisGeneraux;
    protected BigDecimal traiTauxDepotSinistre;
    protected BigDecimal traiTauxInteretSinistre;
    protected LocalDate dateDateResiliation;
    @ManyToOne @JoinColumn(name = "exe_code")
    protected Exercice exercice;
    @ManyToOne @JoinColumn(name = "nat_id")
    protected Nature nature;
    @ManyToOne @JoinColumn(name = "STA_CODE")
    protected Statut statut;
    @ManyToOne @JoinColumn(name = "trai_user_creator")
    protected AppUser traiUserCreator;
    @ManyToOne @JoinColumn(name = "trai_fon_creator")
    protected AppFunction traiFonCreator;
    @ManyToOne @JoinColumn(name = "devise_code")
    protected Devise devise;
    @Column(precision = 50, scale = 20)
    protected BigDecimal traiCoursDevise;
    @ManyToOne @JoinColumn(name = "trai_source_id")
    protected Traite traiSource;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;
    @Transient
    protected List<ConditionTraite> conditions;

    public Traite(Long traiId) {
        this.traiId = traiId;
    }
}
/*
    Est-il possible que tous les traites sensés s'appliquer sur une affaire ne puissent pas s'appliquer du fait que certains traités est absorbé tout le capital de l'affaire ?
    Comment sont libellées les conditions sur un traité ?
    Existe t-il des condition d'exclusion sur les traités ? C'est à dire des conditions qui lorsqu'elles se réalisent sur affaire conduisent systématiquement à la non application du traité sur l'affaire
*/