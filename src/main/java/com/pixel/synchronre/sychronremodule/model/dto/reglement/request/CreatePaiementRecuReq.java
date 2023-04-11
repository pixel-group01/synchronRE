package com.pixel.synchronre.sychronremodule.model.dto.reglement.request;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreatePaiementRecuReq {
    private Long regId;
    private String regCode;
    private String regReference;
    private LocalDate regDate;
    private float regMontant;
    private float regCommission;
    private String typeReglement;
    private Long affId;
    private Long userId;
}
