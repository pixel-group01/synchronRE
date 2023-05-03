package com.pixel.synchronre.sychronremodule.model.dto.repartition.request;

import com.pixel.synchronre.sharedmodule.groups.CREATE_GROUP;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.CoherentCapitalAndTaux;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepCap;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepTau;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.validator.SeuilRepTauBesoinFac;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@SeuilRepCap @SeuilRepTau @CoherentCapitalAndTaux
public class CreateCedLegRepartitionReq
{
    //@NotEmpty(message = "Veuillez saisir les informations de cession(s) légale(s)")
    private List<CreateCesLegReq> cesLegDtos;

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

    @NotNull(message = "Veuillez saisir la sous commission")
    @PositiveOrZero(message = "La sous commission doit être un nombre positif")
    private BigDecimal repSousCommission; //TODO A Valider
    @ExistingAffId
    private Long affId;

    public CreateCedLegRepartitionReq(BigDecimal repCapital, BigDecimal repTaux, BigDecimal repSousCommission, Long affId) {
        this.repCapital = repCapital;
        this.repTaux = repTaux;
        this.repSousCommission = repSousCommission;
        this.affId = affId;
    }


}
