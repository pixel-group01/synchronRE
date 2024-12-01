package com.pixel.synchronre.authmodule.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("ROLE_TO_FNC")
public class RoleToFncAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;
    @ManyToOne @JoinColumn(name = "FNC_ID")
    private AppFunction function;

    public RoleToFncAss(Long assId, int assStatus, LocalDate startsAt, LocalDate endsAt, AppRole role, AppFunction function) {
        super(assId, assStatus, startsAt, endsAt);
        this.role = role;
        this.function = function;
    }

    @Override
    public String toString() {
        return assId + "_" + role + "_" + function;
    }
}
