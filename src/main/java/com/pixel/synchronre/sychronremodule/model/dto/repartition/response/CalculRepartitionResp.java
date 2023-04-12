package com.pixel.synchronre.sychronremodule.model.dto.repartition.response;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
}
