package com.pixel.synchronre.sychronremodule.model.dto.repartition.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CalculRepartitionResp
{
    private float capital;
    private float taux;
    private float tauxBesoinFac;
    private float besoinFacRestant;
}
