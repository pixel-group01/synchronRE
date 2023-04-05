package com.pixel.synchronre.sychronRe.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
@DiscriminatorValue("FACULTATIVE")
public class Facultative extends Affaire{
  private String facNumeroPolice;
  private Long facCapitaux;
  private Long facSmpLci;
  private float facPrime;
}
