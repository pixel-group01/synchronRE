package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class Pays {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long PaysId;
  private String paysCode;
  private String paysIndicatif;
  private String paysNom;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Pays(Long paysId) {
    PaysId = paysId;
  }
}
