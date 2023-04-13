package com.pixel.synchronre.sychronremodule.model.enums;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
public enum TypeReglement
{
  paiement("Paiement reçu"),
  reversement("Reversement");
  public String typRegLibelle;
}
