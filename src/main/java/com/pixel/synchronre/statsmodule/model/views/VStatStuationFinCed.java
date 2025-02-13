package com.pixel.synchronre.statsmodule.model.views;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "v_stat_situation_fin_par_ced") // Nom de la vue dans PostgreSQL
public class VStatStuationFinCed
{
    @Id
    private Long rId;                      // Identifiant de la ligne
    private Long exeCode;                // Code de l'exercice
    private Long cedId;                    // Identifiant de la cédante
    private String cedNomFiliale;          // Nom de la filiale de la cédante
    private String uniqueCode;             // Code unique du type de bordereau
    private Long affId;                    // Identifiant de l'affaire
    private String affCode;                // Code de l'affaire
    private String affAssure;              // Nom de l'assuré
    private String affDateEffet;           // Date d'effet de l'affaire
    private String affDateEcheance;        // Date d'échéance de l'affaire
    private String couLibelle;             // Libellé de la couverture
    private String bordNum;                // Numéro du bordereau
    private BigDecimal montantNoteDebit;       // Montant de la note de débit
    private BigDecimal montantEncaisse;        // Montant encaissé
    @Column(name = "reste_a_encaisser")
    private BigDecimal resteAEncaisser;        // Reste à encaisser
    private String statutEnvoie;           // Statut d'envoi
    private String statutEncaissement;     // Statut d'encaissement
    @Formula("(SELECT REPLACE(UNACCENT(UPPER(REPLACE(stat.statut_envoie, ' ', ''))), '''', '') FROM v_stat_situation_fin_par_ced stat WHERE stat.r_Id = r_Id)")
    private String statutEnvoieNormalise;

    @Formula("(SELECT REPLACE(UNACCENT(UPPER(REPLACE(stat.statut_encaissement, ' ', ''))), '''', '') FROM v_stat_situation_fin_par_ced stat WHERE stat.r_Id = r_Id)")
    private String statutEncaissementNormalise;
}
// Cicare
// ZEPRE SUPR--> CICARE DOUALA
// CICARE ABIDJAN SUR NOTE DEBIT ( avec les information de zepre)
