package com.pixel.synchronre.statsmodule.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "v_stat_situation_note_cred") // Nom de la vue dans PostgreSQL
public class VStatSituationNoteCred {
    @Id
    private Long rId;
    private String numNoteDeb;
    private String numNoteCred;
    private LocalDateTime createdAt;
    private String exeCode;
    private Long cesId;
    private String cesNom;
    private Long cedId;
    private String cedNomFiliale;
    private String nomAssure;
    private String branche;
    private LocalDateTime dateEffet;
    private LocalDateTime dateEcheance;
    private BigDecimal montantCede;
    private BigDecimal montantEncaisse;
    @Column(name = "montant_a_reverser")
    private BigDecimal montantAReverser;
    private BigDecimal montantReverse;
    private BigDecimal commissionNelre;
}
