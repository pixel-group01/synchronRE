package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Entity
public class Branche {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long branId;
  private String branLibelle;
  private String branLibelleAbrege;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
  @ManyToOne
  @JoinColumn(name = "branStatut")
  private Statut statut;

  public Branche(Long branId) {
    this.branId = branId;
  }
}
