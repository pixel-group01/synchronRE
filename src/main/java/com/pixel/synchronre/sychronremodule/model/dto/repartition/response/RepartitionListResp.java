package com.pixel.synchronre.sychronremodule.model.dto.repartition.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepartitionListResp {
    private Long repId;
    private Float repCapital;
    private Float repTaux;
    private Float repSousCommission;
    private String repInterlocuteur;
    private boolean repStatut;
    private Long affId;
    private String affCode;
    private String affAssure;
    private String affActivite;
    private Long cesId;
    private String cesNom;
    private String cesSigle;
    private String cesEmail;
    private String cesTelephone;
}
