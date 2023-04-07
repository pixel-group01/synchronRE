package com.pixel.synchronre.sychronremodule.model.dto.branche.request;

import com.pixel.synchronre.sychronremodule.model.dto.branche.validator.ExistingBranId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateBrancheReq
{
    @ExistingBranId
    private Long branId;
    @NotBlank(message = "Veuillez saisir le nom de la banque")
    @NotNull(message = "Veuillez saisir le nom de la banque")
    private String branLibelle;
    @NotBlank(message = "Veuillez saisir l'abréviation de la banque")
    @NotNull(message = "Veuillez saisir l'abréviation de la banque")
    private String branLibelleAbrege;
}
