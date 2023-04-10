package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
@DiscriminatorValue("FACULTATIVE")
public class Facultative extends Affaire
{
  private String facNumeroPolice;
  private float facCapitaux;
  private float facSmpLci;
  private float facPrime;


  public Facultative(Affaire aff, String facNumeroPolice, float facCapitaux, float facSmpLci, float facPrime)
  {
    this.setFacNumeroPolice(facNumeroPolice);
    this.setFacCapitaux(facCapitaux);
    this.setFacSmpLci(facSmpLci);
    this.setFacPrime(facPrime);
    BeanUtils.copyProperties(aff, this);
  }
}
