package com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateParamCessionLegaleReq
{

    @NotBlank(message = "Veuillez saisir le libéllé du paramètre de la cession légale")
    @NotNull(message = "Veuillez saisir le libéllé du paramètre de la cession légale")
    private String paramCesLegLibelle;
    @NotBlank(message = "Veuillez saisir le capital de la cession légale")
    @NotNull(message = "Veuillez saisir le capital de la cession légale")
    private Long paramCesLegCapital;
    @NotBlank(message = "Veuillez saisir le taux de la cession légale")
    @NotNull(message = "Veuillez saisir le taux de la cession légale")
    private float paramCesLegTaux;
    @NotBlank(message = "Veuillez saisir le pays")
    @NotNull(message = "Veuillez saisir le pays")
    private String paysCode;
    @NotBlank(message = "Veuillez choisir la cédante")
    @NotNull(message = "Veuillez  choisir la cédante")
    private Long cedId;

}
