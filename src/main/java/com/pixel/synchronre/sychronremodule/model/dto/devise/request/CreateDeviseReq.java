package com.pixel.synchronre.sychronremodule.model.dto.devise.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateDeviseReq
{
    @NotBlank(message = "Veuillez saisir le code de la devise")
    @NotNull(message = "Veuillez saisir le code de la devise")
    @Length(message = "Le code de la devise doit contenir au moins deux caractères", min = 2)
    private String devCode;
    @NotBlank(message = "Veuillez saisir la désignation de la devise")
    @NotNull(message = "Veuillez saisir le désignation de la devise")
    private String devLibelle;
    @NotBlank(message = "Veuillez saisir l'abréviation de la devise")
    @NotNull(message = "Veuillez saisir l'abréviation de la devise")
    private String devLibelleAbrege;

}
