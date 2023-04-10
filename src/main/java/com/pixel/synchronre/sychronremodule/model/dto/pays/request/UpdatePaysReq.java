package com.pixel.synchronre.sychronremodule.model.dto.pays.request;

import com.pixel.synchronre.sychronremodule.model.dto.banque.validator.UniqueBanCode;
import com.pixel.synchronre.sychronremodule.model.dto.pays.validator.ExistingPaysId;
import com.pixel.synchronre.sychronremodule.model.dto.pays.validator.UniquePaysCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@UniquePaysCode(message ="paysCode::Le code de ce pays est déjà attribué")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdatePaysReq
{
    @ExistingPaysId
    private Long paysId;
    @NotBlank(message = "Veuillez saisir le code du pays")
    @NotNull(message = "Veuillez saisir le code du pays")
    private String paysCode;
    @NotBlank(message = "Veuillez saisir le numéro indicatif du pays")
    @NotNull(message = "Veuillez saisir le numéro indicatif du pays")
    private String paysIndicatif;
    @NotBlank(message = "Veuillez saisir le libellé du pays")
    @NotNull(message = "Veuillez saisir le le libellé du pays")
    private String paysNom;
}
