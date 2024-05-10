package com.pixel.synchronre.sychronremodule.model.dto.cedantetraite;

import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CesLeg {
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
