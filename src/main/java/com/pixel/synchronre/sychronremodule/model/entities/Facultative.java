package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
@DiscriminatorValue("FACULTATIVE")
public class Facultative extends Affaire
{
  private String facNumeroPolice;
  //private float facCapitaux;
  private BigDecimal facSmpLci;
  private BigDecimal facPrime;


  public Facultative(Affaire aff, String facNumeroPolice, BigDecimal facSmpLci, BigDecimal facPrime)
  {
    this.setFacNumeroPolice(facNumeroPolice);
    //this.setFacCapitaux(facCapitaux);
    this.setFacSmpLci(facSmpLci);
    this.setFacPrime(facPrime);
    BeanUtils.copyProperties(aff, this);
  }
}
