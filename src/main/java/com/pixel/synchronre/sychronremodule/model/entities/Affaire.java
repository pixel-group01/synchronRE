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

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "affType")
public class Affaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long affId;
    @Column(unique = true)
    protected String affCode;
    protected String affAssure;
    protected String affActivite;
    protected LocalDate affDateEffet;
    protected LocalDate affDateEcheance;
    private BigDecimal affCapitalInitial; //Capital à 100
    private BigDecimal affTauxCommissionReassureur;
    private String facNumeroPolice;
    //private float facCapitaux;
    private BigDecimal facSmpLci; // Sinistre max
    private BigDecimal facPrime;
    @ManyToOne @JoinColumn(name = "cedente_id")
    protected Cedante cedante;
    @ManyToOne @JoinColumn(name = "statut_code")
    protected Statut statut;
    @ManyToOne @JoinColumn(name = "couverture_id")
    protected Couverture couverture;

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


    public Affaire(Long affId) {
        this.affId = affId;
    }

    public Affaire(BigDecimal affCapitalInitial, String affCode, String affAssure, String affActivite, LocalDate affDateEffet, LocalDate affDateEcheance) {
        this.affCapitalInitial = affCapitalInitial;
        this.affCode = affCode;
        this.affAssure = affAssure;
        this.affActivite = affActivite;
        this.affDateEffet = affDateEffet;
        this.affDateEcheance = affDateEcheance;
    }
}
