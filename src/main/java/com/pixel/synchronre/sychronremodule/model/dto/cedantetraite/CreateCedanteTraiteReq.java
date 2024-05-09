package com.pixel.synchronre.sychronremodule.model.dto.cedantetraite;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateCedanteTraiteReq
{
    private BigDecimal assiettePrime;
    private BigDecimal tauxPrime;
    private BigDecimal pmd;
    private Long cedId;
    private Long traiteNPId;

    class CesLeg
    {
        @NotNull(message = "Veuillez saisir le capital")
        @PositiveOrZero(message = "Le capital doit être un nombre positif")
        private BigDecimal pmd;


        @ExistingAffId
        @NotNull(message = "Veuillez choisir l'affaire'")
        private Long affId;
        @ExistingParamCesLegId
        @NotNull(message = "Veuillez choisir le paramétrage de la cession légale")
        private Long paramCesLegalId;
        private boolean accepte;

        protected BigDecimal affCoursDevise;
    }
}
