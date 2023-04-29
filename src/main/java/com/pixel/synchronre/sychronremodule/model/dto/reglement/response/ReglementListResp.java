package com.pixel.synchronre.sychronremodule.model.dto.reglement.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReglementListResp {
    private Long regId;
    private String regReference;
    private LocalDate regDate;
    private BigDecimal regMontant;
    private BigDecimal regCommission;
    private String typeReglement;
    private String regMode;


    private String affCode;
    private String affAssure;
    private String affActivite;
    private LocalDate affDateEffet;
    private LocalDate affDateEcheance;
    private BigDecimal affCapitalInitial;
    private String facNumeroPolice;

    public ReglementListResp(Long regId,String regReference, LocalDate regDate, BigDecimal regMontant, BigDecimal regCommission) {
        this.regId = regId;
        this.regReference = regReference;
        this.regDate = regDate;
        this.regMontant = regMontant;
        this.regCommission = regCommission;
        this.regMode=regMode;
    }
}
