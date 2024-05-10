package com.pixel.synchronre.sychronremodule.model.dto.souslimite.request;


import lombok.*;

import java.math.BigDecimal;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateSousLimiteReq
{
    private Long sousLimiteSouscriptionId;
    private BigDecimal sousLimMontant;
    private Long risqueCouvertId;
    private Long traiteNonProportionnelId;
    private Long trancheId;
    private String statutCode;

}
