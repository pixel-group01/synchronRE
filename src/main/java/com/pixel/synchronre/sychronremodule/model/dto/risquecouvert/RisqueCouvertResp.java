package com.pixel.synchronre.sychronremodule.model.dto.risquecouvert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RisqueCouvertResp
{
    private Long risqueId;
    private Long couId;
    private String couLibelle;
    private String description;
    private Long traiteNpId;
    private  String traiReference;
    private String staCode;
    private String staLibelle;
}
