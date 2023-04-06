package com.pixel.synchronre.authmodule.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("ROLE_TO_FNC")
public class RoleToFncAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;
    @ManyToOne @JoinColumn(name = "FNC_ID")
    private AppFunction function;
}
