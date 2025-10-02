package com.pixel.synchronre.sychronremodule.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter @Setter @Entity @Immutable @Table(name = "v_tranche_categorie")
public class VTrancheCategorie
{
    @Id
    @Column(name = "row_num")
    private Long rowNum;

    @Column(name = "traite_np_id")
    private Long traiteNpId;

    @Column(name = "tranche_id")
    private Long trancheId;

    @Size(max = 255)
    @Column(name = "tranche_libelle")
    private String trancheLibelle;

    @Column(name = "tranche_priorite", precision = 38, scale = 2)
    private BigDecimal tranchePriorite;

    @Column(name = "tranche_porte", precision = 38, scale = 2)
    private BigDecimal tranchePorte;

    @Column(name = "tranche_taux_prime", precision = 38, scale = 2)
    private BigDecimal trancheTauxPrime;

    @Column(name = "categorie_id")
    private Long categorieId;

    @Size(max = 255)
    @Column(name = "categorie_libelle")
    private String categorieLibelle;

    @Column(name = "type_id")
    private Long typeId;

    @Size(max = 255)
    @Column(name = "unique_code")
    private String uniqueCode;

}