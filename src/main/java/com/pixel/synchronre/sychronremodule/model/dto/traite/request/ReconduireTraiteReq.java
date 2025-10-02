package com.pixel.synchronre.sychronremodule.model.dto.traite.request;

import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.ExistingTNPId;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.UniqueTraiteNpRef;
import com.pixel.synchronre.sychronremodule.model.dto.traite.validator.UniqueTraiteNumero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReconduireTraiteReq
{
    @ExistingTNPId @NotNull(message = "Veuillez selectionner le traité à renouveler")
    private Long traiteNpId;
    @NotNull(message = "La référence unique est obligatoire")
    @NotBlank(message = "La référence unique est obligatoire")
    @UniqueTraiteNpRef
    @NotNull(message = "Veuillez saisir la référence du traité")
    private String traiReference;
    @UniqueTraiteNumero
    @NotNull(message = "Veuillez saisir le numéro du traité")
    @NotBlank(message = "Veuillez saisir le numéro du traité")
    private String traiNumero;
}
