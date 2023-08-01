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
    private Long cedId;
    private Long visibilityId; // Id de la cédante. Dans un autre projet ça peut désigner l'ID d'une autre entité
    private Long cesId; // Id du cessionnaire //Seulement valable dans le cadre du projet SynchronRE
}
