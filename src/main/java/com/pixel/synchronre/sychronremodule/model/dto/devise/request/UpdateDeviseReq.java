package com.pixel.synchronre.sychronremodule.model.dto.devise.request;

import com.pixel.synchronre.sychronremodule.model.dto.devise.validator.ExistingDevCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateDeviseReq
{
    @ExistingDevCode
    private String devCode;
    @NotBlank(message = "Veuillez saisir la désignation de la devise")
    @NotNull(message = "Veuillez saisir le désignation de la devise")
    private String devLibelle;
    @NotBlank(message = "Veuillez saisir l'abréviation de la devise")
    @NotNull(message = "Veuillez saisir l'abréviation de la devise")
    private String devLibelleAbrege;
}
