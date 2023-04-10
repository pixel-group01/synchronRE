package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
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

    public Statut(String staCode) {
      this.staCode = staCode;
    }
}
