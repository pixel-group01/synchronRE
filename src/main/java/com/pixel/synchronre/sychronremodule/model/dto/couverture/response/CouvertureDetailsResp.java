package com.pixel.synchronre.sychronremodule.model.dto.couverture.response;

import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CouvertureDetailsResp
{
    private Long couId;
    private String couLibelle;
    private String couLibelleAbrege;
    private Long branId;
}
