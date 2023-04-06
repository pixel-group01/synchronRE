package com.pixel.synchronre.authmodule.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateUserDTO
{
    @UniqueEmail(message = "Adresse mail déjà attribuée")
    private String email;
    @UniqueTel(message = "N° de téléphone déjà attribué")
    private String tel;
    private String firstName;
    private String lastName;
    private Long idMetier;
}
