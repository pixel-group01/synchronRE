package com.pixel.synchronre.sychronremodule.model.dto.reglement.response;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaiementDetailsResp {
    private Long regId;
    private String regReference;
    private LocalDate regDate;
    private float regMontant;
    private float regCommission;
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
