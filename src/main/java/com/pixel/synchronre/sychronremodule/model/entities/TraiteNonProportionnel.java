package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
public class TraiteNonProportionnel
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRAI_ID_GEN")
    @SequenceGenerator(name = "TRAI_ID_GEN", sequenceName = "TRAI_ID_GEN")
    private Long traiId;
    @Column(unique = true)
    private String traiReference;
    private String traiNumeroPolice;
    private String traiLibelle;
    private String traiAuteur;
    @Enumerated(EnumType.STRING)
    private EXERCICE_RATTACHEMENT traiEcerciceRattachement;
    private LocalDate traiDateEffet;
    private LocalDate traiDateEcheance;
    @Column(precision = 50, scale = 20)
    private BigDecimal traiCoursDevise;
    @Enumerated(EnumType.STRING)
    private PERIODICITE traiPeriodicite;
    private Long traiDelaiEnvoi;
    private Long traiDelaiConfirmation;
    private BigDecimal traiTauxCourtier;
    private BigDecimal traiTauxSurcommission;
    @ManyToOne @JoinColumn(name = "exe_code")
    private Exercice exercice;
    @ManyToOne @JoinColumn(name = "trai_source_id")
    private TraiteNonProportionnel traiSource;
    @ManyToOne @JoinColumn(name = "nat_code")
    private Nature nature;
    @ManyToOne @JoinColumn(name = "devise_code")
    private Devise traiDevise;
    @ManyToOne @JoinColumn(name = "comote_devise_code")
    private Devise traiCompteDevise;
    @ManyToOne @JoinColumn(name = "STA_CODE")
    private Statut statut;
    @ManyToOne @JoinColumn(name = "trai_user_creator")
    private AppUser traiUserCreator;
    @ManyToOne @JoinColumn(name = "trai_fon_creator")
    private AppFunction traiFonCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public TraiteNonProportionnel(Long traiId) {
        this.traiId = traiId;
    }
}
/*
    Est-il possible que tous les traites sensés s'appliquer sur une affaire ne puissent pas s'appliquer du fait que certains traités est absorbé tout le capital de l'affaire ?
    Comment sont libellées les conditions sur un traité ?
    Existe t-il des condition d'exclusion sur les traités ? C'est à dire des conditions qui lorsqu'elles se réalisent sur affaire conduisent systématiquement à la non application du traité sur l'affaire
*/