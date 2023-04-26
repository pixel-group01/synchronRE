package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau  @CoherentCapitalAndTaux @CoherentTauxCrtAndScms(message = "Le taux de la commission de courtage ne peut exéder celui de la commission de réassurance")
public class UpdatePlaRepartitionReq
{
    @ExistingPlaId
    private Long plaId;
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

    @NotNull(message = "Veuillez saisir le taux de commission de courtage")
    @PositiveOrZero(message = "Le taux de commission de courtage doit être un nombre positif")
    private BigDecimal repTauxComCourt;

    @NotBlank(message = "Veuillez saisir le nom de l'interlocuteur")
    @NotNull(message = "Veuillez saisir le nom de l'interlocuteur")
    @Length(message = "Le nom de l'interlocuteur doit contenir au moins deux caractères", min = 2)
    private String repInterlocuteur;
}
