package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
@DiscriminatorValue("FACULTATIVE")
public class Facultative extends Affaire{
  private String facNumeroPolice;
  private Long facCapitaux;
  private Long facSmpLci;
  private float facPrime;
}
