package com.pixel.synchronre.sychronremodule.model.dto.cedantetraite;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.ExistingCedId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    private Long traiteNPId;
    private List<CesLeg> cessionsLegales;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class CesLeg
    {
        private Long repId;
        @NotNull(message = "Veuillez saisir le taux du cessionnaire légal")
        @PositiveOrZero(message = "Le taux doit être un nombre positif")
        private BigDecimal tauxCesLeg;
        @NotNull(message = "Veuillez saisir la PMD")
        @PositiveOrZero(message = "La PMD doit être un nombre positif")
        private BigDecimal pmd;
        private String paramCesLegalLibelle;
        @ExistingParamCesLegId
        @NotNull(message = "Veuillez choisir le paramétrage de la cession légale")
        private Long paramCesLegalId;
        private boolean accepte;
    }
}
