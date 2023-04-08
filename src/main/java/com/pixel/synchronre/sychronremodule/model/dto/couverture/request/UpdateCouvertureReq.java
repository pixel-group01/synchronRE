package com.pixel.synchronre.sychronremodule.model.dto.couverture.request;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesTel;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.ExistingCouId;

import com.pixel.synchronre.sychronremodule.model.dto.couverture.validator.UniqueCouLibelleAbrege;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;



@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueCouLibelleAbrege(message ="couLibelleAbrege::Cette Abréviation est déjà utilisée")
public class UpdateCouvertureReq
{
    @ExistingCouId
    private Long couId;
    @NotBlank(message = "Veuillez saisir le libellé de la couverture")
    @NotNull(message = "Veuillez saisir le libellé de la couverture")
    private String couLibelle;
    @NotBlank(message = "Veuillez saisir l'abréviation de la couverture")
    @NotNull(message = "Veuillez saisir l'abréviation de la couverture")
    private String couLibelleAbrege;
    private Long branId;
}
