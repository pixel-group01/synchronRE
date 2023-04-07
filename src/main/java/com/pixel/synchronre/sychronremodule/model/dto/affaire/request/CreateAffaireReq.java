package com.pixel.synchronre.sychronremodule.model.dto.affaire.request;

import com.pixel.synchronre.sychronremodule.model.dto.affaire.validator.UniqueAffCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateAffaireReq
{

    @NotBlank(message = "Veuillez saisir le code du statut")
    @NotNull(message = "Veuillez saisir le code du statut")
    @Length(message = "Le code du statut doit contenir au moins deux caractères", min = 2)
    @UniqueAffCode
    private String staCode;

    @NotBlank(message = "Veuillez saisir le libellé du statut")
    @NotNull(message = "Veuillez saisir le libellé du statut")
    @Length(message = "Le libellé du statut doit contenir au moins trois caractères", min = 3)
    private String staLibelle;

    @NotBlank(message = "Veuillez saisir le libellé long du statut")
    @NotNull(message = "Veuillez saisir le libellé long du statut")
    @Length(message = "Le libellé du statut doit contenir au moins trois caractères", min = 3)
    private String staLibelleLong;

    @NotBlank(message = "Veuillez saisir le type du statut")
    @NotNull(message = "Veuillez saisir le type du statut")
    private String staType;
}
