package com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request;

import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.validator.ExistingIntId;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.validator.UniqueIntEmail;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.validator.UniqueIntTel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueIntEmail(message = "IntEmail::Adresse mail déjà attribuée")
@UniqueIntTel(message = "IntTel::Numéro de téléphone déjà attribué")
public class UpdateInterlocuteurReq
{
    @ExistingIntId
    private Long intId;
    @NotBlank(message = "Veuillez saisir le nom de l'interlocuteur")
    @NotNull(message = "Veuillez saisir le nom de l'interlocuteur")
    @Length(message = "Le nom de l'interlocuteur doit contenir au moins deux caractères", min = 2)
    private String IntNom;
    @NotBlank(message = "Veuillez saisir le prenom de l'interlocuteur")
    @NotNull(message = "Veuillez saisir le prenom de l'interlocuteur")
    @Length(message = "Le nom de l'interlocuteur doit contenir au moins deux caractères", min = 2)
    private String IntPrenom;
    @NotBlank(message = "Veuillez saisir le numéro de téléphone de l'interlocuteur")
    @NotNull(message = "Veuillez saisir le numéro de téléphone de l'interlocuteur")
    private String IntTel;
    @NotBlank(message = "Veuillez saisir l'adresse mail de l'interlocuteur")
    @NotNull(message = "Veuillez saisir l'adresse mail de l'interlocuteur")
    @Email(message = "Veuillez saisir une adresse mail valide")
    private String IntEmail;
    private Long intCesId;
}
