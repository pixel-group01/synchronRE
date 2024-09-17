package com.pixel.synchronre.sychronremodule.model.dto.souslimite.request;


import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.ExistingCouId;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.ExistingRisqueId;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.validator.ExistingSousLimiteSouscriptionId;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.ExistingTrancheId;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class UpdateSousLimite
{
    @ExistingSousLimiteSouscriptionId
    private Long sousLimiteSouscriptionId;
    @NotNull(message = "Veuillez saisir le montant de la sous limite")
    private BigDecimal sousLimMontant;
    @ExistingCouId @NotNull(message = "Veuillez sélectionner l'activité")
    private Long couId;
    private Long traiteNonProportionnelId;
}


