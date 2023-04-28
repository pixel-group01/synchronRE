package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.ExistingCesId;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.*;
import com.pixel.synchronre.typemodule.model.dtos.ExistingTypeId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau @CoherentCapitalAndTaux
public class UpdateRepartitionReq
{
    @ExistingRepId
    private Long repId;
    private BigDecimal repCapital;
    private BigDecimal repTaux;
    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    @Max(value = 100)
    @SeuilRepTauBesoinFac
    private BigDecimal repTauxBesoinFac;
    private BigDecimal repSousCommission;
    private String repInterlocuteur;
    @ExistingAffId
    @NotNull(message = "Veuillez choisir le type de la répartition")
    private Long affId;
    @ExistingCesId
    private Long cesId;
    @ExistingParamCesLegId
    private Long paramCesLegalId;
    @ExistingTypeId
    @NotNull(message = "Veuillez choisir le type de la répartition")
    private Long typId;
}
