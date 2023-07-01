package com.pixel.synchronre.sychronremodule.model.dto.pays.request;

import com.pixel.synchronre.sychronremodule.model.dto.pays.validator.UniquePaysCode;
import com.pixel.synchronre.sychronremodule.model.entities.Devise;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreatePaysReq
{
    @NotBlank(message = "Veuillez saisir le code du pays")
    @NotNull(message = "Veuillez saisir le code du pays")
    @UniquePaysCode
    private String paysCode;
    @NotBlank(message = "Veuillez saisir le code indicatif du pays")
    @NotNull(message = "Veuillez saisir le code indicatif du pays")
    private String paysIndicatif;
    @NotBlank(message = "Veuillez saisir le libellé du pays")
    @NotNull(message = "Veuillez saisir le le libellé du pays")
    private String paysNom;
    @NotBlank(message = "Veuillez selectionnez la devise du pays")
    @NotNull(message = "Veuillez selectionnez la devise du pays")
    private String devCode;
}
