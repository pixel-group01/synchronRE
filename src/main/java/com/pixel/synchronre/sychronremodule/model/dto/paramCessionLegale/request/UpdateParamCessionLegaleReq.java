package com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request;

import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.validator.ExistingParamCesLegId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateParamCessionLegaleReq
{
    @ExistingParamCesLegId
    private Long paramCesLegId;
    @NotBlank(message = "Veuillez saisir le libéllé du paramètre de la cession légale")
    @NotNull(message = "Veuillez saisir le libéllé du paramètre de la cession légale")
    private String paramCesLegLibelle;
    @NotNull(message = "Veuillez saisir le capital de la cession légale")
    private BigDecimal paramCesLegCapital;
    @NotNull(message = "Veuillez saisir le taux de la cession légale")
    private BigDecimal paramCesLegTaux;
    @NotNull(message = "Veuillez saisir le pays")
    private Long paysId;
}
