package com.pixel.synchronre.sychronremodule.model.dto.sinistre.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SinistreDetailsResp
{
    private Long sinId;
    private String sinCode;
    private BigDecimal sinMontant100;
    private LocalDate sinDateSurvenance;
    private LocalDate sinDateDeclaration;
    private String sinCommentaire;
    private String affCode;
    private String affAssure;
    private String affActivite;
    private BigDecimal affCapitalInitial;

    private BigDecimal dejaRegle;
    private BigDecimal restARegle;
    private BigDecimal tauxDeReglement;

    public SinistreDetailsResp(Long sinId, BigDecimal sinMontant100, LocalDate sinDateSurvenance, LocalDate sinDateDeclaration, String sinCommentaire, String affCode, String affAssure, String affActivite, BigDecimal affCapitalInitial) {
        this.sinId = sinId;
        this.sinMontant100 = sinMontant100;
        this.sinDateSurvenance = sinDateSurvenance;
        this.sinDateDeclaration = sinDateDeclaration;
        this.sinCommentaire = sinCommentaire;
        this.affCode = affCode;
        this.affAssure = affAssure;
        this.affActivite = affActivite;
        this.affCapitalInitial = affCapitalInitial;
    }


}
