package com.pixel.synchronre.sychronremodule.model.dto.mouvement.request;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MvtSuivantAffaireReq
{
    private String staCode;
    @ExistingAffId
    private Long affId;
}
