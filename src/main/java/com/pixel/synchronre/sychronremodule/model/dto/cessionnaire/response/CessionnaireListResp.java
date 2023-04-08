package com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response;


import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CessionnaireListResp
{
    private Long cesId;
    private String cesNom;
    private String cesSigle;
    private String cesEmail;
    private String cesTelephone;
    private String cesAdressePostale;
    private String cesSituationGeo;
    private String staLibelle;
}
