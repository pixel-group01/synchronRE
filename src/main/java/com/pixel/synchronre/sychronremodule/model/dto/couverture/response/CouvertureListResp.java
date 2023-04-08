package com.pixel.synchronre.sychronremodule.model.dto.couverture.response;


import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CouvertureListResp
{
    private Long couId;
    private String couLibelle;
    private String couLibelleAbrege;
    private String brancheLibelle;
    private String staLibelle;

}
