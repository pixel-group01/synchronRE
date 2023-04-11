package com.pixel.synchronre.sychronremodule.model.dto.facultative.response;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FacultativeDetailsResp
{
    private Long affId;
    private String affCode;
    private String affAssure;
    private String affActivite;
    private LocalDate affDateEffet;
    private LocalDate affDateEcheance;
    private String facNumeroPolice;
    private Long facCapitaux;
    private Long facSmpLci;
    private float facPrime;
    private Long cedenteId;
    private String statutCode;
    protected Long couvertureId;
    private float restARepartir;
    private float capitalDejaReparti;
}
