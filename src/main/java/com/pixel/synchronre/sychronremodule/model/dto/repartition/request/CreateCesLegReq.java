package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau @CoherentCapitalAndTaux @LimitedNumberOfCesLeg
public class CreateCesLegReq
{
    private Long repId;

    @NotNull(message = "Veuillez saisir le capital")
    @PositiveOrZero(message = "Le capital doit être un nombre positif")
    private BigDecimal repCapital;

    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    private BigDecimal repTaux;

    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    @Max(value = 100)
    @SeuilRepTauBesoinFac
    private BigDecimal repTauxBesoinFac;

    @NotNull(message = "Veuillez saisir la sous commission")
    @PositiveOrZero(message = "La sous commission doit être un nombre positif")
    private BigDecimal repSousCommission; //TODO A Valider

    @ExistingAffId @NotNull(message = "Veuillez choisir l'affaire'")
    private Long affId;
    @ExistingParamCesLegId @NotNull(message = "Veuillez choisir le paramétrage de la cession légale")
    private Long paramCesLegalId;
}