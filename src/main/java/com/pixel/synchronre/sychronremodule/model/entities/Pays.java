package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class Pays {
  @Id
  private String paysCode;
  private String paysIndicatif;
  private String paysNom;
  @ManyToOne @JoinColumn(name = "devise")
  private Devise devise;
  @ManyToOne
  @JoinColumn(name = "payStatut")
  private Statut statut;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Pays(String paysCode) {
    this.paysCode = paysCode;
  }

}
