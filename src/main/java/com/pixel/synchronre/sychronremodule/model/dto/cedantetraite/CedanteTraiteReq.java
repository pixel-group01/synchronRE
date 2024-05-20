package com.pixel.synchronre.sychronremodule.model.dto.cedantetraite;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.ExistingCedId;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CedanteTraiteReq
{
    private Long cedanteTraiteId;
    @NotNull(message = "Veuillez saisir l'assiette de prime")
    private BigDecimal assiettePrime;
    @NotNull(message = "Veuillez saisir le taux de prime")
    private BigDecimal tauxPrime;
    @NotNull(message = "Veuillez saisir la PMD")
    private BigDecimal pmd;
    @NotNull(message = "Veuillez choisir la cédante")
    @ExistingCedId
    private Long cedId;
    @NotNull(message = "Veuillez selectionner le traité concerné")
    @ExistingTNPId
    private Long traiteNpId;
    private List<CesLeg> cessionsLegales;

    public CedanteTraiteReq(Long cedanteTraiteId, BigDecimal assiettePrime, BigDecimal tauxPrime, BigDecimal pmd, Long cedId, Long traiteNpId) {
        this.cedanteTraiteId = cedanteTraiteId;
        this.assiettePrime = assiettePrime;
        this.tauxPrime = tauxPrime;
        this.pmd = pmd;
        this.cedId = cedId;
        this.traiteNpId = traiteNpId;
    }

    public CedanteTraiteReq(Long cedId, Long traiteNpId) {
        this.cedId = cedId;
        this.traiteNpId = traiteNpId;
    }
}
