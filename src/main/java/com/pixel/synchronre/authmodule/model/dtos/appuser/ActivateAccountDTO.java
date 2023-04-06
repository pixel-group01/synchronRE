package com.pixel.synchronre.authmodule.model.dtos.appuser;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ValidToken(message = "activationToken::Lien invalide") @ConcordantPassword @ConcordantUserIdAndEmail
public class ActivateAccountDTO
{
    @ExistingUserId
    private Long userId;
    @NotBlank(message = "L'email ne peut être nul")
    @Email(message = "L'email  n'est pas valide")
    private String email;
    @NotBlank(message = "Le mot de passe ne peut être nul")
    @Length(message = "Le mot de passe doit contenir au moins 4 caractères", min = 4)
    @NotNull(message = "Le mot de passe ne peut être nul")
    private String password;
    private String confirmPassword;
    @ValidToken @NoneExpiredToken @NoneAlreadyUsedToken
    private String activationToken;
}
