package com.pixel.synchronre.sychronremodule.model.dto.statut.response;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesTel;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatutListResp
{
    private Long staId;
    private String staCode;
    private String staLibelle;
    private String staLibelleLong;
    private String staType;
}
