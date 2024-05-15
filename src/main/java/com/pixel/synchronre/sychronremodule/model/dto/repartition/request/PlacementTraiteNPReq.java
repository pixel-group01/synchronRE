package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.ExistingCesId;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.validator.ExistingIntId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.CoherentTauxCrtAndScms;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepCap;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepTau;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepTau
public class PlacementTraiteNPReq
{
    private Long repId;
    @NotNull(message = "Veuillez saisir le capital")
    @PositiveOrZero(message = "La prime doit être un nombre positif")
    private BigDecimal repPrime;

    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    private BigDecimal repTaux;

    @NotNull(message = "Veuillez saisir la sous commission")
    @PositiveOrZero(message = "La sous commission doit être un nombre positif")
    private BigDecimal repTauxCourtierPlaceur; //TODO A Valider

    @NotNull(message = "Veuillez saisir le taux de commission de courtage")
    @PositiveOrZero(message = "Le taux de commission de courtage doit être un nombre positif")
    private BigDecimal repTauxCourtier; //Taux NelsonRe

    @NotNull(message = "Veuillez selectionner le cessionnaire")
    @ExistingCesId
    private Long cesId;

    @ExistingTNPId
    private Long traiNPId;
    private boolean isAperiteur;
}