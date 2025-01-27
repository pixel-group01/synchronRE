package com.pixel.synchronre.statsmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StatChiffreAffaireParPeriodeDTO
{
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
    private LocalDate affDateEffet;
    private LocalDate affDateEcheance;
    private String exeCode;
    private Long repId;
    private BigDecimal montantCede;
    private BigDecimal commissionNelsonre;
    private BigDecimal montantAReverser;
    private BigDecimal montantReverse;
    private LocalDate dateReversement;
    private BigDecimal montantEncaisse;
}
