package com.pixel.synchronre.sychronremodule.model.dto.souslimite.request;


import com.pixel.synchronre.sychronremodule.model.dto.souslimite.validator.ExistingSousLimiteSouscriptionId;
import lombok.*;

import java.math.BigDecimal;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateSousLimite
{
    @ExistingSousLimiteSouscriptionId
    private Long sousLimiteSouscriptionId;
    private BigDecimal sousLimMontant;
    private Long risqueCouvertId;
    private Long traiteNonProportionnelId;
    private Long trancheId;
    private String statutCode;
}
