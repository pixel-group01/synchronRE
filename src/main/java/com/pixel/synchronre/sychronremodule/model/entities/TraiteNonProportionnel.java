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

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
public class TraiteNonProportionnel
{
    @Id
    protected String traiReference;
    protected String traiNumeroPolice;
    protected String traiLibelle;
    protected String traiAuteur;
    @Enumerated(EnumType.STRING)
    protected EXERCICE_RATTACHEMENT traiEcerciceRattachement;
    protected LocalDate traiDateEffet;
    protected LocalDate traiDateEcheance;
    @Column(precision = 50, scale = 20)
    protected BigDecimal traiCoursDevise;
    @Enumerated(EnumType.STRING)
    protected PERIODICITE traiPeriodicite;
    protected Long traiDelaiEnvoi;
    protected Long traiDelaiConfirmation;
    protected BigDecimal traiTauxCourtier;
    protected BigDecimal traiTauxSurcommission;
    @ManyToOne @JoinColumn(name = "exe_code")
    protected Exercice exercice;
    @ManyToOne @JoinColumn(name = "trai_source_id")
    protected TraiteNonProportionnel traiSource;
    @ManyToOne @JoinColumn(name = "nat_id")
    protected Nature nature;
    @ManyToOne @JoinColumn(name = "devise_code")
    protected Devise traiDevise;
    @ManyToOne @JoinColumn(name = "comote_devise_code")
    protected Devise traiCompteDevise;
    @ManyToOne @JoinColumn(name = "STA_CODE")
    protected Statut statut;
    @ManyToOne @JoinColumn(name = "trai_user_creator")
    protected AppUser traiUserCreator;
    @ManyToOne @JoinColumn(name = "trai_fon_creator")
    protected AppFunction traiFonCreator;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;


    public TraiteNonProportionnel(String traiReference) {
        this.traiReference = traiReference;
    }
}
/*
    Est-il possible que tous les traites sensés s'appliquer sur une affaire ne puissent pas s'appliquer du fait que certains traités est absorbé tout le capital de l'affaire ?
    Comment sont libellées les conditions sur un traité ?
    Existe t-il des condition d'exclusion sur les traités ? C'est à dire des conditions qui lorsqu'elles se réalisent sur affaire conduisent systématiquement à la non application du traité sur l'affaire
*/