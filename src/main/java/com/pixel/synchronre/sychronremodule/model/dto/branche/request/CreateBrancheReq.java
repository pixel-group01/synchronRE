package com.pixel.synchronre.sychronremodule.model.dto.branche.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateBrancheReq
{
    @NotBlank(message = "Veuillez saisir le nom de la branche")
    @NotNull(message = "Veuillez saisir le nom de la branche")
    private String branLibelle;
    @NotBlank(message = "Veuillez saisir l'abréviation de la branche")
    @NotNull(message = "Veuillez saisir l'abréviation de la branche")
    private String branLibelleAbrege;
}
