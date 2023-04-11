package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.ExistingCesId;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.CoherentCapitalAndTaux;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepCap;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepTau;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepTauBesoinFac;
import com.pixel.synchronre.typemodule.model.dtos.ExistingTypeId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau @CoherentCapitalAndTaux
public class UpdateRepartitionReq
{
    private Long repId;
    private Float repCapital;
    private Float repTaux;
    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    @Max(value = 100)
    @SeuilRepTauBesoinFac
    private Float repTauxBesoinFac;
    private Float repSousCommission;
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
