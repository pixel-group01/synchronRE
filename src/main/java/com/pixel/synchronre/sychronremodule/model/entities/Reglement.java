package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Data @Builder
@Entity
public class Reglement {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REG_ID_GEN")
  @SequenceGenerator(name = "REG_ID_GEN", sequenceName = "REG_ID_GEN")
  private Long regId;
  private String regReference;
  private LocalDate regDate;
  private BigDecimal regMontant;
  private BigDecimal regCommission;
  private boolean regStatut;
  @ManyToOne @JoinColumn(name = "type_id")
  private Type typeReglement;
  @ManyToOne @JoinColumn(name = "aff_id")
  private Affaire affaire;
  @ManyToOne @JoinColumn(name = "sin_id")
  private Sinistre sinistre;
  @ManyToOne @JoinColumn(name = "ces_id")
  private Cessionnaire cessionnaire;
  @ManyToOne @JoinColumn(name = "user_id")
  private AppUser appUser;
  @ManyToOne @JoinColumn(name = "function_id")
  private AppFunction functionCreator;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

}
