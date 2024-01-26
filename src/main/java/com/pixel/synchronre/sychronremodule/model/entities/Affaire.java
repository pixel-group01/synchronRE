package com.pixel.synchronre.sychronremodule.model.entities;


import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "affType")
public class Affaire
{
    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "AFF_ID_GEN", sequenceName = "AFF_ID_GEN", allocationSize = 1)
    protected Long affId;
    @Column(unique = true)
    protected String affCode;
    protected String affAssure;
    protected String affActivite;
    protected LocalDate affDateEffet;
    protected LocalDate affDateEcheance;
    protected LocalDate aff_date_limite_Paiement;
    @Column(precision = 50, scale = 20)
    private BigDecimal affCapitalInitial; //Capital à 100
    //private BigDecimal affTauxCommissionReassureur;
    private String facNumeroPolice;
    //private float facCapitaux;
    @Column(precision = 50, scale = 20)
    private BigDecimal facSmpLci; // Sinistre max
    @Column(precision = 50, scale = 20)
    private BigDecimal facPrime;
    private BigDecimal partCedante; //montant de la smplci soumuise en réassurance
    protected String affStatutCreation; //Statut à la création de l'affaire ( Réalisée:REALISEE / En instance:INSTANCE / Non Réalisée:NON_REALISEE )
    @ManyToOne @JoinColumn(name = "cedente_id")
    protected Cedante cedante;
    @ManyToOne @JoinColumn(name = "statut_code")
    protected Statut statut;
    @ManyToOne @JoinColumn(name = "couverture_id")
    protected Couverture couverture;

    @ManyToOne @JoinColumn(name = "exe_code")
    protected Exercice exercice;

    @ManyToOne @JoinColumn(name = "type_code")
    protected Type affType;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    @ManyToOne @JoinColumn(name = "aff_user_creator")
    private AppUser affUserCreator;
    @ManyToOne @JoinColumn(name = "aff_fon_creator")
    private AppFunction affFonCreator;
    @ManyToOne @JoinColumn(name = "devise_code")
    protected Devise devise;
    @Column(precision = 50, scale = 20)
    protected BigDecimal affCoursDevise;
    @ManyToOne @JoinColumn(name = "aff_source_id")
    private Affaire affSource;


    public Affaire(Long affId) {
        this.affId = affId;
    }

    public Affaire(BigDecimal affCapitalInitial, String affCode, String affAssure, String affActivite, LocalDate affDateEffet, LocalDate affDateEcheance,Exercice exercice,String affStatutCreation) {
        this.affCapitalInitial = affCapitalInitial;
        this.affCode = affCode;
        this.affAssure = affAssure;
        this.affActivite = affActivite;
        this.affDateEffet = affDateEffet;
        this.affDateEcheance = affDateEcheance;
        this.exercice=exercice;
        this.affStatutCreation=affStatutCreation;
    }

    public Affaire(BigDecimal affCapitalInitial, BigDecimal facSmpLci,String affCode, String affAssure, String affActivite, LocalDate affDateEffet, LocalDate affDateEcheance,Exercice exercice,String affStatutCreation,Type affType) {
        this.affCapitalInitial = affCapitalInitial;
        this.facSmpLci = facSmpLci;
        this.affCode = affCode;
        this.affAssure = affAssure;
        this.affActivite = affActivite;
        this.affDateEffet = affDateEffet;
        this.affDateEcheance = affDateEcheance;
        this.exercice=exercice;
        this.affStatutCreation=affStatutCreation;
    }


}
