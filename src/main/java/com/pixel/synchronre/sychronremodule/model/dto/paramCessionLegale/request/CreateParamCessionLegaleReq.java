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
    @NotNull(message = "Veuillez saisir le capital de la cession légale")
    private Long paramCesLegCapital;
    @NotNull(message = "Veuillez saisir le taux de la cession légale")
    private float paramCesLegTaux;
    @NotNull(message = "Veuillez saisir le pays")
    private Long paysId;
    @NotNull(message = "Veuillez  choisir la cédante")
    private Long cedId;

}