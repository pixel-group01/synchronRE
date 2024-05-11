package com.pixel.synchronre.sychronremodule.model.dto.risquecouvert;

import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.ExistingCouId;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NotNull(message = "Aucune donnée parvenue")
public class CreateRisqueCouvertReq
{
    @ExistingCouId @NotNull(message = "Veuillez selectionner la couverture")
    private Long couId;
    private String description;
    @ExistingTNPId @NotNull(message = "L'ID du traité ne peut être nul")
    private Long traiteNPId;
}