package com.pixel.synchronre.sychronremodule.model.dto.cedente;

import com.pixel.synchronre.sychronremodule.model.dto.cedente.validator.UniqueCedEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.validator.UniqueCedTel;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesTel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateCedenteDTO
{
    @NotBlank(message = "Veuillez saisir le nom de la cedente")
    @NotNull(message = "Veuillez saisir le nom de la cedente")
    @Length(message = "Le nom de la cedente doit contenir au moins deux caractères", min = 2)
    private String cedNomFiliale;
    @NotBlank(message = "Veuillez saisir le sigle de la cedente")
    @NotNull(message = "Veuillez saisir le sigle de la cedente")
    private String cedSigleFiliale;
    @NotBlank(message = "Veuillez saisir le numéro de téléphone de la cedente")
    @NotNull(message = "Veuillez saisir le numéro de téléphone de la cedente")
    @UniqueCedTel
    private String cedTel;
    @NotBlank(message = "Veuillez saisir l'adresse mail de la cedente")
    @NotNull(message = "Veuillez saisir l'adresse mail de la cedente")
    @Email(message = "Veuillez saisir une adresse mail valide")
    @UniqueCedEmail
    private String cedEmail;
    private String cedAdressePostale;
    private String cedFax;
    private String cedSituationGeo;
}
