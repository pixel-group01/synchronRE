package com.pixel.synchronre.authmodule.model.entities;

import lombok.*;

import jakarta.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("PRV_TO_ROLE")
public class PrvToRoleAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "PRV_ID")
    private AppPrivilege privilege;
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;
}
