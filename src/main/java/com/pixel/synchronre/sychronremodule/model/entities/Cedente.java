package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
public class Cedente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cedId;
  private String cedNomFiliale;
  private String cedSigleFiliale;
  private String cedTel;
  private String cedEmail;
  private String cedAdressePostale;
  private String cedFax;
  private String cedSituationGeo;
  @ManyToOne
  private Statut cedStatut;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
