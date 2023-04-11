package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.sychronremodule.model.enums.TypeReglement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Data @Builder
@Entity
public class Reglement {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REG_ID_GEN")
  @SequenceGenerator(name = "REG_ID_GEN", sequenceName = "REG_ID_GEN")
  private Long regId;
  private String regCode;
  private String regReference;
  private LocalDate regDate;
  private float regMontant;
  private float regCommission;
  @Enumerated(EnumType.STRING)
  private TypeReglement typeReglement;
  @ManyToOne @JoinColumn(name = "aff_id")
  private Affaire affaire;
  @ManyToOne @JoinColumn(name = "user_id")
  private AppUser appUser;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
