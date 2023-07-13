package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau @CoherentCapitalAndTaux @ValidRepCedId
public class UpdateCedLegRepartitionReq
{
    @Valid
    private List<UpdateCesLegReq> updateCesLegReqs;
    private Long repId;
    @NotNull(message = "Veuillez saisir le capital")
    @PositiveOrZero(message = "Le capital doit être un nombre positif")
    private BigDecimal repCapital;

    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")
    private BigDecimal repTaux;

    @NotNull(message = "Veuillez saisir le taux")
    @PositiveOrZero(message = "Le taux doit être un nombre positif")//21236584
    @Max(value = 100)
    @SeuilRepTauBesoinFac
    private BigDecimal repTauxBesoinFac;
    private BigDecimal besoinFac;

    @ExistingAffId
    private Long affId;

    private String affCode;
    private String affAssure;
    private String affActivite;
    private BigDecimal affCapitalInitial;
}