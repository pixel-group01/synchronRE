package com.pixel.synchronre.sychronremodule.model.dto.repartition.response;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CalculRepartitionResp
{
    private BigDecimal capital;
    private BigDecimal taux;
    private BigDecimal tauxBesoinFac;
    private BigDecimal besoinFac;
    private BigDecimal besoinFacRestant;

    private BigDecimal cmsRea;
    private BigDecimal cmsCourtage;
    private BigDecimal cmsCedante;
    private BigDecimal primeNetteCessionnaire;
}
