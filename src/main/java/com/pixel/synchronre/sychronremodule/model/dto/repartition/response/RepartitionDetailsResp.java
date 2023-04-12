package com.pixel.synchronre.sychronremodule.model.dto.repartition.response;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepartitionDetailsResp {
    private Long repId;
    private BigDecimal repCapital;
    private BigDecimal repTaux;
    private BigDecimal repSousCommission;
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
