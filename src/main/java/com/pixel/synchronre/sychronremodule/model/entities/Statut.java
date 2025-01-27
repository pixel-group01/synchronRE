package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
public class Statut {
  @Id
  private String staCode;
  private String staLibelle;
  private String staLibelleLong;
  @Enumerated(EnumType.STRING)
  private TypeStatut staType;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Override
  public String toString()
  {
    return staCode + '_' + staLibelle;
  }

  public Statut(String staCode) {
      this.staCode = staCode;
    }
}
