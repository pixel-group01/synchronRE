package com.pixel.synchronre.authmodule.model.dtos.appuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter @NoArgsConstructor
public class AuthResponseDTO
{
    private String accessToken;
    private String refreshToken;
}
