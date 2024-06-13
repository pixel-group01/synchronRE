package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class Association
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

    @ManyToOne @JoinColumn(name = "terr_id")
    private Territorialite territorialite;

    @ManyToOne @JoinColumn(name = "TYP_ID")
    private Type type;

    @ManyToOne @JoinColumn(name = "STA_CODE")
    private Statut statut;

    public Association(Pays pays, Organisation organisation, Statut statut,Type type) {
        this.pays = pays;
        this.organisation = organisation;
        this.statut = statut;
        this.type=type;
    }

    public Association(Categorie categorie, Cedante cedante,Type type) {
        this.categorie = categorie;
        this.cedante = cedante;
        this.type=type;
    }

    public Association(Pays pays, Tranche tranche,Categorie categorie,  Type type) {
        this.pays = pays;
        this.tranche = tranche;
        this.categorie = categorie;
        this.type = type;
    }

    public Association(Organisation organisation, Pays pays, Territorialite territorialite, Type type) {
        this.organisation = organisation;
        this.pays = pays;
        this.territorialite = territorialite;
        this.type = type;
    }
}
