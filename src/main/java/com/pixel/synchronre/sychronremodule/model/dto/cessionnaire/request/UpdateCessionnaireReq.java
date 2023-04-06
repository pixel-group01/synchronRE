package com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.ExistingCesId;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesTel;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@UniqueCesEmail(message ="cesEmail::Cette adresse est déjà utilisée")
@UniqueCesTel(message ="cesTelephone::Ce numéro de téléphone est déjà utilisée")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateCessionnaireReq
{
    @ExistingCesId
    private Long cesId;
    @NotBlank(message = "Veuillez saisir le nom du cessionnaire")
    @NotNull(message = "Veuillez saisir le nom du cessionnaire")
    @Length(message = "Le nom du cessionnaire doit contenir au moins deux caractères", min = 2)
    private String cesNom;
    @NotBlank(message = "Veuillez saisir le sigle du cessionnaire")
    @NotNull(message = "Veuillez saisir le sigle du cessionnaire")
    private String cesSigle;
    @NotBlank(message = "Veuillez saisir l'adresse mail du cessionnaire")
    @NotNull(message = "Veuillez saisir l'adresse mail du cessionnaire")
    @Email(message = "Veuillez saisir une adresse mail valide")
    private String cesEmail;
    @NotBlank(message = "Veuillez saisir le numéro de téléphone du cessionnaire")
    @NotNull(message = "Veuillez saisir le numéro de téléphone du cessionnaire")
    @Length(min = 14, max = 20, message = "La longueur du numéro doit être comprise entre 14 et 20 caractères")
    private String cesTelephone;
    private String cesAdressePostale;
    private String cesSituationGeo;


}
