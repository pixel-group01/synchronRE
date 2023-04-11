package com.pixel.synchronre.sychronremodule.model.dto.repartition.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CalculRepartitionResp
{
    private Float capital;
    private Float taux;
    private Float tauxBesoinFac;
    private Float besoinFacRestant;
}
