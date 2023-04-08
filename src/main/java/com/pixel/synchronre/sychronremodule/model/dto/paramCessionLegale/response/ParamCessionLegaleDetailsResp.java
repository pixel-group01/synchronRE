package com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response;

import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParamCessionLegaleDetailsResp
{

    private Long paramCesLegId;
    private String paramCesLegLibelle;
    private Long paramCesLegCapital;
    private float paramCesLegTaux;
    private Long paysId;
    private Long cedanteId;
}
