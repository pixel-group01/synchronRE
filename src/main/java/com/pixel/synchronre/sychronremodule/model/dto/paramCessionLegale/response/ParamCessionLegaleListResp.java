package com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response;


import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParamCessionLegaleListResp
{
    private Long paramCesLegId;
    private String paramCesLegLibelle;
    private Long paramCesLegCapital;
    private float paramCesLegTaux;
    private String paysNom;
    private String cedNomFiliale;
    private String staLibelle;
}
