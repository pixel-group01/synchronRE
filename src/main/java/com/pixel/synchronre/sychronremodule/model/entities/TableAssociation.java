package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class TableAssociation
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASSO_ID_GEN")
    @SequenceGenerator(name = "ASSO_ID_GEN", sequenceName = "ASSO_ID_GEN")
    private Long assoId;
    @ManyToOne @JoinColumn(name = "traite_np_id")
    private TraiteNonProportionnel traiteNonProportionnel;

    @ManyToOne @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne @JoinColumn(name = "couverture_id")
    private Couverture couverture;

    @ManyToOne @JoinColumn(name = "ced_id")
    private Cedante cedante;

    @ManyToOne @JoinColumn(name = "tranche_ID")
    private Tranche tranche;

    @ManyToOne @JoinColumn(name = "PAY_CODE")
    private Pays pays;
    @ManyToOne @JoinColumn(name = "ORGAN_CODE")
    private Organisation organisation;

    @ManyToOne @JoinColumn(name = "TYP_ID")
    private Type type;

}
