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
public class Mouvement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mvtId;
  @ManyToOne @JoinColumn(name = "mvt_sta_code")
  private Statut statut;
  private String mvtObservation;
  @CreationTimestamp
  private LocalDateTime mvtDate;
}
