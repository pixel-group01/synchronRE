package com.pixel.synchronre.sychronremodule.model.dto.statut.response;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.ExistingCesId;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesTel;
import com.pixel.synchronre.sychronremodule.model.dto.statut.validator.ExistingStatId;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatutDetailsResp
{
    private Long staId;
    private String staCode;
    private String staLibelle;
    private String staLibelleLong;
    private String staType;
}
