package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Mouvement {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "MVT_ID_GEN", sequenceName = "MVT_ID_GEN")
  private Long mvtId;
  @ManyToOne @JoinColumn(name = "mvt_sta_code")
  private Statut statut;
  private String mvtObservation;
  @ManyToOne @JoinColumn(name = "AFF_ID")
  private Affaire affaire;
  @CreationTimestamp
  private LocalDateTime mvtDate;
}
