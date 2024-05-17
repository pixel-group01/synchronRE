package com.pixel.synchronre.sychronremodule.model.dto.souslimite.request;


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
    @ExistingRisqueId @NotNull(message = "Veuillez sélectionner le risque couvert")
    private Long risqueCouvertId;
    private Long traiteNonProportionnelId;
    @ExistingTrancheId @NotNull(message = "Veuillez sélectionner la tranche")
    private Long trancheId;
    private String statutCode;
}
/*

public class CreateSousLimiteReq
{

    private BigDecimal sousLimMontant;

    private Long risqueCouvertId;
    @ExistingTNPId @NotNull(message = "L'ID du traité ne peut être null")
    private Long traiteNonProportionnelId;
    @ExistingTrancheId @NotNull(message = "Veuillez sélectionner la tranche")
    private Long trancheId;
    private String statutCode;
 */
