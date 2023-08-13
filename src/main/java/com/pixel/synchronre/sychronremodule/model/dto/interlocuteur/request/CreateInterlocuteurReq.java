package com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request;

import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.validator.UniqueIntEmail;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.validator.UniqueIntTel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateInterlocuteurReq
{
    @NotBlank(message = "Veuillez saisir le nom de l'interlocuteur")
    @NotNull(message = "Veuillez saisir le nom de l'interlocuteur")
    @Length(message = "Le nom de l'interlocuteur doit contenir au moins deux caractères", min = 2)
    private String intNom;
    @NotBlank(message = "Veuillez saisir le prenom de l'interlocuteur")
    @NotNull(message = "Veuillez saisir le prenom de l'interlocuteur")
    @Length(message = "Le nom de l'interlocuteur doit contenir au moins deux caractères", min = 2)
    private String intPrenom;
    @NotBlank(message = "Veuillez saisir le numéro de téléphone de l'interlocuteur")
    @NotNull(message = "Veuillez saisir le numéro de téléphone de l'interlocuteur")
    @UniqueIntTel
    private String intTel;
    @NotBlank(message = "Veuillez saisir l'adresse mail de l'interlocuteur")
    @NotNull(message = "Veuillez saisir l'adresse mail de l'interlocuteur")
    @Email(message = "Veuillez saisir une adresse mail valide")
    @UniqueIntEmail
    private String intEmail;
    private Long intCesId;
}
