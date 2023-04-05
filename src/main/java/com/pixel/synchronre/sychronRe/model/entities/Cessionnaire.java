package com.pixel.synchronre.sychronRe.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
public class Cessionnaire {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cesId;
  private String cesLibelle;
  private String ceslibAbrege;
  private String cesEmail;
  private String cesTelephone;
  private String cesCellulaire;
  private String cesAdressePostale;
  private String cesSituationGeo;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
