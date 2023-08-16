package com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateParamCessionLegaleReq
{

    @NotBlank(message = "Veuillez saisir le libéllé du paramètre de la cession légale")
    @NotNull(message = "Veuillez saisir le libéllé du paramètre de la cession légale")
    private String paramCesLegLibelle;
    @NotNull(message = "Veuillez saisir le taux de la cession légale")
    private BigDecimal paramCesLegTaux;
    @NotNull(message = "Veuillez selectionner le type de la cession légale")
    private Long typeId;
    @NotNull(message = "Veuillez saisir le pays")
    private String paysCode;

}
