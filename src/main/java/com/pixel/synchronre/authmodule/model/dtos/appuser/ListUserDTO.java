package com.pixel.synchronre.authmodule.model.dtos.appuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ListUserDTO
{
    private Long userId;
    private String firstName;
    private String lastName;
    private String cedName; // Id de la cédante. Dans un autre projet ça peut désigner l'ID d'une autre entité
    private String cedSigle;
    private String cesName; // Id du cessionnaire //Seulement valable dans le cadre du projet SynchronRE
    private String cesSigle;
    private String email;
    private String tel;
    private boolean active;
    private boolean notBlocked;
    private String currentFunctionName;
    private String statut;

    public ListUserDTO(Long userId, String firstName, String lastName, String cedName, String cedSigle, String cesName, String cesSigle, String email, String tel, boolean active, boolean notBlocked) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cedName = cedName;
        this.cedSigle = cedSigle;
        this.cesName = cesName;
        this.cesSigle = cesSigle;
        this.email = email;
        this.tel = tel;
        this.active = active;
        this.notBlocked = notBlocked;
    }
}
