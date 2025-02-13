package com.pixel.synchronre.statsmodule.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Formula;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "v_stat_situation_fin_par_rea_ced") // Nom de la vue PostgreSQL
public class VStatSituationFinParReaCed {

    @Id
    private Long rId;
    private Long debId;
    private Long affId;
    private String bordNum;
    private String affCode;
    private Long cedId;
    private String cedNomFiliale;
    private Long cesId;
    private String cesNom;
    private String affAssure;
    private String couLibelle;
    private String affDateEffet;
    private String affDateEcheance;
    private Long exeCode;
    private Long repId;
    private String montantCede;
    private String commissionNelsonre;
    @Column(name = "montant_a_reverser")
    private String montantAReverser;
    private String montantEncaisse;
    private String montantReverse;
    @Column(name = "reste_a_reverse")
    private String resteAReverse;
    private String statutEnvoie;
    private String statutEncaissement;
    @Formula("(SELECT REPLACE(UNACCENT(UPPER(REPLACE(stat.statut_encaissement, ' ', ''))), '''', '') FROM v_stat_situation_fin_par_rea_ced stat WHERE stat.r_Id = r_Id)")
    private String statutEncaissementNormalise;

}
