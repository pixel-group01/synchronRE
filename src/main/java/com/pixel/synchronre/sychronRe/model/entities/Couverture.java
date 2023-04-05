package com.pixel.synchronre.sychronRe.model.entities;

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
public class Couverture {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long couId;
  private String couLibelle;
  private String couLibelleAbrege;
  @ManyToOne @JoinColumn(name = "branId")
  private Branche branche;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
