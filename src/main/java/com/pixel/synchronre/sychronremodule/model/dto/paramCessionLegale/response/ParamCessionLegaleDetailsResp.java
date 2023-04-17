package com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response;

import lombok.*;

import java.math.BigDecimal;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParamCessionLegaleDetailsResp
{

    private Long paramCesLegId;
    private String paramCesLegLibelle;
    private BigDecimal paramCesLegCapital;
    private BigDecimal paramCesLegTaux;
    private String paysCode;
}
