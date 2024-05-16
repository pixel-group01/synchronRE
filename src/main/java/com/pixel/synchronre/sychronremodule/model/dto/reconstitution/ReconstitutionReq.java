package com.pixel.synchronre.sychronremodule.model.dto.reconstitution;

import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.ExistingTrancheId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ReconstitutionReq {
    private Long reconstitutionId;
    private Long nbrReconstitution;
    private BigDecimal tauxReconstitution;
    private BigDecimal tauxPrimeReconstitution;
    private String modeCalculReconstitution;
    @ExistingTrancheId
    private Long trancheId;
    @ExistingTNPId
    private Long traiteNpId;

}
