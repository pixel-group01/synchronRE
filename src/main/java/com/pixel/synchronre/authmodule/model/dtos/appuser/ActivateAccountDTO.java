package com.pixel.synchronre.authmodule.model.dtos.appuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ValidToken(message = "activationToken::Lien invalide") @ConcordantPassword //@ConcordantUserIdAndEmail
public class ActivateAccountDTO
{
    @NotBlank(message = "L'email ne peut être nul")
    @Email(message = "L'email  n'est pas valide")
    private String email;
    @NotBlank(message = "Le mot de passe ne peut être nul")
    @Length(message = "Le mot de passe doit contenir au moins 4 caractères", min = 4)
    @NotNull(message = "Le mot de passe ne peut être nul")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=/\\*\\-_<>?!:,;])(?=.*[a-zA-Z\\d@#$%^&+=/\\*\\-_<>?!:,;]).{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, des minuscules, des majuscules, des chiffres et des caractères spéciaux.")
    private String password;
    private String confirmPassword;
    @ValidToken @NoneExpiredToken @NoneAlreadyUsedToken
    private String activationToken;
}