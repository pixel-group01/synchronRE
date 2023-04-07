package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
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
  @ManyToOne @JoinColumn(name = "ced_pays_code")
  private Pays pays;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
  @ManyToOne
  private Statut cedStatut;
}
