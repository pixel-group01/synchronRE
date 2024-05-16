package com.pixel.synchronre.sychronremodule.model.dto.reconstitution;

import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.ExistingTrancheId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class ReconstitutionResp
{
    private Long reconstitutionId;
    private Long nbrReconstitution;
    private BigDecimal tauxReconstitution;
    private BigDecimal tauxPrimeReconstitution;
    private String modeCalculReconstitution;
    private Long traiteNpId;
    private String traiReference;
    private String traiNumero;
    private String staCode;
    private String staLibelle;
    private Long trancheId;
    private String trancheLibelle;

    public ReconstitutionResp(Long reconstitutionId, Long nbrReconstitution, BigDecimal tauxReconstitution, BigDecimal tauxPrimeReconstitution, String modeCalculReconstitution, Long traiteNPId, String traiReference, String traiNumero, Long trancheId, String trancheLibelle) {
        this.reconstitutionId = reconstitutionId;
        this.nbrReconstitution = nbrReconstitution;
        this.tauxReconstitution = tauxReconstitution;
        this.tauxPrimeReconstitution = tauxPrimeReconstitution;
        this.modeCalculReconstitution = modeCalculReconstitution;
        this.traiteNpId = traiteNpId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
        this.trancheId = trancheId;
        this.trancheLibelle = trancheLibelle;
    }
}
