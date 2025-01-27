package com.pixel.synchronre.statsmodule.model.views;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "v_stat_situation_fin_par_rea_ced") // Nom de la vue PostgreSQL
public class VStatSituationFinReaCed
{
    @Id
    private Long rId; // Identifiant unique généré par ROW_NUMBER()
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
    private LocalDate affDateEffet;
    private LocalDate affDateEcheance;
    private String exeCode;
    private Long repId;
    private BigDecimal montantCede; // Correspond à deb_prime
    private BigDecimal commissionNelsonre; // Correspond à deb_commission
    private BigDecimal montantAReverser; // Correspond à deb_prime_areverser
    private BigDecimal montantEncaisse; // Correspond au champ calculé
    private BigDecimal montantReverse; // Correspond au champ calculé
    private BigDecimal resteAReverse; // Correspond au champ calculé
}
