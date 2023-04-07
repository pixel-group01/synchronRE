package com.pixel.synchronre.sychronremodule.model.dto.affaire.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AffaireDetailsResp
{
    private Long staId;
    private String staCode;
    private String staLibelle;
    private String staLibelleLong;
    private String staType;
}
