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

    private Long cesId;
    private String cesNom;
    private String cesSigle;

    private Long sinId;
    private String sinCode;
    private BigDecimal sinMontant100;
    private LocalDate sinDateSurvenance;
    private LocalDate sinDateDeclaration;
    private String sinCommentaire;

    public ReglementListResp(Long regId,String regReference, LocalDate regDate, BigDecimal regMontant, BigDecimal regCommission,String regMode) {
        this.regId = regId;
        this.regReference = regReference;
        this.regDate = regDate;
        this.regMontant = regMontant;
        this.regCommission = regCommission;
        this.regMode=regMode;
    }

    public ReglementListResp(Long regId,String regReference, LocalDate regDate, BigDecimal regMontant, BigDecimal regCommission,String regMode, Long cesId, String cesNom) {
        this.regId = regId;
        this.regReference = regReference;
        this.regDate = regDate;
        this.regMontant = regMontant;
        this.regCommission = regCommission;
        this.regMode=regMode;
        this.cesId = cesId;
        this.cesNom = cesNom;
    }
}
