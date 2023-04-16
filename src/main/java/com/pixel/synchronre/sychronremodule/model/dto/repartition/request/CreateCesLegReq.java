package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.*;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau @CoherentCapitalAndTaux @LimitedNumberOfCesLeg
public class CreateCesLegReq
{
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