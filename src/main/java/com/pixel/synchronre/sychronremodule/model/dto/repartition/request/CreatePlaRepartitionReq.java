package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.ExistingCesId;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.validator.ExistingIntId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.CoherentTauxCrtAndScms;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepCap;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepTau;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau  /*@CoherentCapitalAndTaux*/ @CoherentTauxCrtAndScms(message = "Le taux de la commission de courtage ne peut exéder celui de la commission de réassurance")
public class CreatePlaRepartitionReq
{
    private Long repId;
    @NotNull(message = "Veuillez saisir le capital")
    @PositiveOrZero(message = "Le capital doit être un nombre positif")
    private BigDecimal repCapital;

    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    private BigDecimal repTaux;

    @ExistingIntId @NotNull(message = "Veuillez selectionner l'interlocuteur principal")
    private Long interlocuteurPrincipalId;

    private List<Long> autreInterlocuteurIds;

    @NotNull(message = "Veuillez saisir la sous commission")
    @PositiveOrZero(message = "La sous commission doit être un nombre positif")
    private BigDecimal repSousCommission; //TODO A Valider

    @NotNull(message = "Veuillez saisir le taux de commission de courtage")
    @PositiveOrZero(message = "Le taux de commission de courtage doit être un nombre positif")
    private BigDecimal repTauxComCourt;

    @NotNull(message = "Veuillez selectionner le cessionnaire")
    @ExistingCesId
    private Long cesId;

    @ExistingAffId
    private Long affId;

    protected BigDecimal affCoursDevise;
}