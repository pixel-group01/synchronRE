package com.pixel.synchronre.sychronremodule.model.dto.statut.request;

import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesTel;
import com.pixel.synchronre.sychronremodule.model.dto.statut.validator.UniqueStaCode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateStatutReq
{

    @NotBlank(message = "Veuillez saisir le code du statut")
    @NotNull(message = "Veuillez saisir le code du statut")
    @Length(message = "Le code du statut doit contenir au moins deux caractères", min = 2)
    @UniqueStaCode
    private String staCode;

    @NotBlank(message = "Veuillez saisir le libellé du statut")
    @NotNull(message = "Veuillez saisir le libellé du statut")
    @Length(message = "Le libellé du statut doit contenir au moins trois caractères", min = 3)
    private String staLibelle;

    @NotBlank(message = "Veuillez saisir le libellé long du statut")
    @NotNull(message = "Veuillez saisir le libellé long du statut")
    @Length(message = "Le libellé du statut doit contenir au moins trois caractères", min = 3)
    private String staLibelleLong;

//    @NotBlank(message = "Veuillez saisir le type du statut")
//    @NotNull(message = "Veuillez saisir le type du statut")
//    @Length(min = 3, max = 4, message = "type du statut doit être comprise entre 3 et 4 caractères")
//    private String staType;
}
