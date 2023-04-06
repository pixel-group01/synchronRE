package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
@DiscriminatorValue("TRAITE")
public class RepartitionTraite extends Repartition{
    private String test;
}
