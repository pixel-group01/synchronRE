package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @Entity
public class Pays {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAY_ID_GEN")
  @SequenceGenerator(name = "PAY_ID_GEN", sequenceName = "PAY_ID_GEN", allocationSize = 10)
  private Long paysId;
  private String paysCode;
  private String paysIndicatif;
  private String paysNom;
  @ManyToOne
  @JoinColumn(name = "payStatut")
  private Statut statut;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public Pays(Long paysId) {
    this.paysId = paysId;
  }

}
