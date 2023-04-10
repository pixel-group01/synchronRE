package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateRepartitionReq {
    private Long repId;
    private float repCapital;
    private float repTaux;
    private float repSousCommission;
    private String repInterlocuteur;
    private boolean repStatut;
    private Long affId;
    private Long cesId;
    private Long paramCesLegalId;
    private Long typId;
}
