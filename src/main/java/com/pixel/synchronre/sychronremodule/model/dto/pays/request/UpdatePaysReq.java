package com.pixel.synchronre.sychronremodule.model.dto.pays.request;

import com.pixel.synchronre.sychronremodule.model.dto.pays.validator.ExistingPaysCode;
import com.pixel.synchronre.sychronremodule.model.dto.pays.validator.UniquePaysCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdatePaysReq
{

    @NotBlank(message = "Veuillez saisir le code du pays")
    @NotNull(message = "Veuillez saisir le code du pays")
    @ExistingPaysCode
    private String paysCode;
    @NotBlank(message = "Veuillez saisir le numéro indicatif du pays")
    @NotNull(message = "Veuillez saisir le numéro indicatif du pays")
    private String paysIndicatif;
    @NotBlank(message = "Veuillez saisir le libellé du pays")
    @NotNull(message = "Veuillez saisir le le libellé du pays")
    private String paysNom;
}
