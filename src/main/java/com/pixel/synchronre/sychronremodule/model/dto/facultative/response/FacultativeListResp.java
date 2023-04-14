package com.pixel.synchronre.sychronremodule.model.dto.facultative.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FacultativeListResp
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
    private BigDecimal facPrime;
    private Long cedenteId;
    private String statutCode;
    private String staLibelle;
    private String couLibelle;
    private String affVisibility;
}
