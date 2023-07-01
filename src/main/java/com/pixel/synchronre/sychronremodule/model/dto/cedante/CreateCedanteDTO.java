package com.pixel.synchronre.sychronremodule.model.dto.cedante;

import com.pixel.synchronre.authmodule.model.dtos.appfunction.ExistingFncId;
import com.pixel.synchronre.authmodule.model.dtos.appuser.ExistingUserId;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.ExistingCedId;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.UniqueCedEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.validator.UniqueCedTel;
import com.pixel.synchronre.sychronremodule.model.dto.pays.validator.ExistingPaysId;
import com.pixel.synchronre.sychronremodule.model.dto.pays.validator.UniquePaysCode;
import com.pixel.synchronre.typemodule.model.dtos.ExistingTypeId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateCedanteDTO
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
    private String cedInterlocuteur;
    @UniquePaysCode
    private String paysCode;
    /*@ExistingUserId
    private Long cedUserCreator;
    @ExistingFncId
    private Long cedFonCreator;*/
}
