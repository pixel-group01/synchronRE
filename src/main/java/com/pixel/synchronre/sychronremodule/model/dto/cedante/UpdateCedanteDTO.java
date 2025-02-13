package com.pixel.synchronre.sychronremodule.model.dto.cedante;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.ExistingCedId;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.UniqueCedEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.UniqueCedTel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueCedEmail(message = "Adresse mail déjà attribuée")
@UniqueCedTel(message = "Numéro de téléphone déjà attribué")
public class UpdateCedanteDTO
{
    @ExistingCedId
    private Long cedId;
    @NotBlank(message = "Veuillez saisir le nom de la cedente")
    @NotNull(message = "Veuillez saisir le nom de la cedente")
    @Length(message = "Le nom de la cedente doit contenir au moins deux caractères", min = 2)
    private String cedNomFiliale;
    @NotBlank(message = "Veuillez saisir le sigle de la cedente")
    @NotNull(message = "Veuillez saisir le sigle de la cedente")
    private String cedSigleFiliale;
    @NotBlank(message = "Veuillez saisir le numéro de téléphone de la cedente")
    @NotNull(message = "Veuillez saisir le numéro de téléphone de la cedente")
    private String cedTel;
    @NotBlank(message = "Veuillez saisir l'adresse mail de la cedente")
    @NotNull(message = "Veuillez saisir l'adresse mail de la cedente")
    @Email(message = "Veuillez saisir une adresse mail valide")
    private String cedEmail;
    private String cedAdressePostale;
    private String cedFax;
    private String cedSituationGeo;
    private String cedInterlocuteur;
    private String cedStatut;
    @NotBlank(message = "Veuillez selectionner le numéro de compte")
    @NotNull(message = "Veuillez selectionner le numéro de compte")
    private String banNumCompte;
}
