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
public class Couverture {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "COU_ID_GEN", sequenceName = "COU_ID_GEN")
  private Long couId;
  private String couLibelle;
  private String couLibelleAbrege;
  @ManyToOne @JoinColumn(name = "branId")
  private Branche branche;
  @ManyToOne @JoinColumn(name = "branStatut")
  private Statut statut;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

    public Couverture(Long couvertureId) {
      this.couId = couvertureId;
    }
}
