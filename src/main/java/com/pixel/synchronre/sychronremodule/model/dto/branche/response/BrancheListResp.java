package com.pixel.synchronre.sychronremodule.model.dto.branche.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BrancheListResp
{
    private Long branId;
    private String branLibelle;
    private String branLibelleAbrege;
    private String staLibelle;
}
