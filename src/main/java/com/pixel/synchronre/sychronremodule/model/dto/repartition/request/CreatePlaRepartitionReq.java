package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.ExistingCesId;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.CoherentCapitalAndTaux;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepCap;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepTau;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepTauBesoinFac;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau  @CoherentCapitalAndTaux
public class CreatePlaRepartitionReq
{
    @NotNull(message = "Veuillez saisir le capital")
    @PositiveOrZero(message = "Le capital doit être un nombre positif")
    private BigDecimal repCapital;

    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    private BigDecimal repTaux;

    //@NotNull(message = "Veuillez saisir le taux")
    //@PositiveOrZero(message = "Le taux doit être un nombre positif")
    //@Max(value = 100)
    //@SeuilRepTauBesoinFac
    private BigDecimal repTauxBesoinFac;

    @NotNull(message = "Veuillez saisir la sous commission")
    @PositiveOrZero(message = "La sous commission doit être un nombre positif")
    private BigDecimal repSousCommission; //TODO A Valider

    @NotBlank(message = "Veuillez saisir le nom de l'interlocuteur")
    @NotNull(message = "Veuillez saisir le nom de l'interlocuteur")
    @Length(message = "Le nom de l'interlocuteur doit contenir au moins deux caractères", min = 2)
    private String repInterlocuteur;

    @NotNull(message = "Veuillez selectionner le cessionnaire")
    @ExistingCesId
    private Long cesId;

    @ExistingAffId
    private Long affId;
}
