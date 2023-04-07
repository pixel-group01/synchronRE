package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Banque {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long banId;
  private String banCode;
  private String banLibelle;
  private String banLibelleAbrege;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
  @ManyToOne
  @JoinColumn(name = "banStatut")
  private Statut statut;
}
