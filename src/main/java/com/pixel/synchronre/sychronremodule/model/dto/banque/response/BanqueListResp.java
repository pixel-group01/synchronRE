package com.pixel.synchronre.sychronremodule.model.dto.banque.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BanqueListResp
{
    private Long banId;
    private String banCode;
    private String banLibelle;
    private String banLibelleAbrege;
    private String staLibelle;
}