package com.pixel.synchronre.sychronremodule.model.dto.cedantetraite;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.ExistingCedId;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TranchePmdDto;
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
    private Long traiteNpId;
    @NotNull(message = "Veuillez choisir la cédante")
    @ExistingCedId
    private Long cedId;
    @NotNull(message = "Veuillez saisir l'assiette de prime")
    private BigDecimal assiettePrime;

    private List<TranchePmdDto> tranchePmdDtos;
    private List<CesLeg> cessionsLegales;

    public CedanteTraiteReq(Long cedanteTraiteId, Long traiteNpId, BigDecimal assiettePrime, Long cedId) {
        this.cedanteTraiteId = cedanteTraiteId;
        this.traiteNpId = traiteNpId;
        this.assiettePrime = assiettePrime;
        this.cedId = cedId;
    }
}
