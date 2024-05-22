package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tranche_categorie")@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrancheCategorie
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANCHE_CATEGORIE_ID_GEN")
    @SequenceGenerator(name = "TRANCHE_CATEGORIE_ID_GEN", sequenceName = "TRANCHE_CATEGORIE_ID_GEN")
    private Long trancheCategorieId;
    @ManyToOne @JoinColumn(name = "tranche_ID")
    private Tranche tranche;
    @ManyToOne @JoinColumn(name = "categorie_id")
    private Categorie categorie;
}
