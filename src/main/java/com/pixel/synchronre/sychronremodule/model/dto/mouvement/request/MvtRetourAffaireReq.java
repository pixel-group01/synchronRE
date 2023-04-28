package com.pixel.synchronre.sychronremodule.model.dto.mouvement.request;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MvtRetourAffaireReq
{
    @NotNull(message = "Veuillez saisir le motif du retour")
    @NotBlank(message = "Veuillez saisir le motif du retour")
    private String mvtObservation;
    @ExistingAffId
    private Long affId;
}
