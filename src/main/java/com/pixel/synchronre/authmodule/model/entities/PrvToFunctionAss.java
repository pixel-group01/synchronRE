package com.pixel.synchronre.authmodule.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("PRV_TO_FNC")
public class PrvToFunctionAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "PRV_ID")
    private AppPrivilege privilege;
    @ManyToOne @JoinColumn(name = "FNC_ID")
    private AppFunction function;

    @Override
    public String toString() {
        return assId + "_" + privilege + "_" + function;
    }
}
