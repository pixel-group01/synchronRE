package com.pixel.synchronre.sychronremodule.model.dto.reglement.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReglementDetailsResp {
    private Long regId;
    private String regReference;
    private LocalDate regDate;
    private BigDecimal regMontant;
    private BigDecimal regCommission;
    private String typeReglement;
    private String affCode;
    private String affAssure;
    private String affActivite;
    private LocalDate affDateEffet;
    private LocalDate affDateEcheance;
    private String cedNomFiliale;
    private String cedSigleFiliale;
    private Long userId;
}