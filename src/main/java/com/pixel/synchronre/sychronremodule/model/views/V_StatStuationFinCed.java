package com.pixel.synchronre.sychronremodule.model.views;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "v_stat_situation_fin_par_ced") // Nom de la vue dans PostgreSQL
public class V_StatStuationFinCed
{
    @Id
    private Long rId;
    private String exeCode;
    private Long cedId;
    private String cedNomFiliale;
    private String uniqueCode;
    private Long affId;
    private String affCode;
    private String affAssure;
    private LocalDate affDateEffet;
    private LocalDate affDateEcheance;
    private String couLibelle;
    private String bordNum;
    private BigDecimal montantNoteDebit;
    private BigDecimal montantEncaisse;
    private BigDecimal resteAEncaisser;
}
