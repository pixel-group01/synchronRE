package com.pixel.synchronre.authmodule.model.dtos.appuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ConcordantUserIdAndEmail @ConcordantPassword
public class ChangePasswordDTO
{
    @ExistingUserId
    private Long userId;
    private String email;

    private String oldPassword;
    @NotBlank(message = "Le mot de passe ne peut être nul")
    @Length(message = "Le mot de passe doit contenir au moins 4 caractères", min = 4)
    @NotNull(message = "Le mot de passe ne peut être nul")
    private String newPassword;
    private String confirmPassword;
}
