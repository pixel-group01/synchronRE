package com.pixel.synchronre.authmodule.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ValidToken(message = "passwordReinitialisationToken::Lien invalide") @ConcordantPassword
public class ReinitialisePasswordDTO
{
    @ExistingEmail
    private String email;
    private String newPassword;
    private String confirmNewPassword;
    @ValidToken @NoneExpiredToken @NoneAlreadyUsedToken
    private String passwordReinitialisationToken;
}
