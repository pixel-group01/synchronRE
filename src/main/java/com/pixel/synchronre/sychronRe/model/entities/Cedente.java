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
public class Cedente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cedId;
  private String cedLibelleFiliale;
  private String cedLibelleFilialeAbrege;
  private String cedTelephone;
  private String cedEmail;
  private String cedAdressePostale;
  private String cedFax;
  private String cedSituationGeo;
  @ManyToOne @JoinColumn(name = "ced_ban_id")
  private Banque banque;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
