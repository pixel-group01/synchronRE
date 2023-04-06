package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
@DiscriminatorValue("FACULTATIVE")
public class RepartitionFac extends Repartition{
  private Long repCapitaux;
  private float repTaux;
  private String repMode;
}
