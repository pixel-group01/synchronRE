package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Cessionnaire {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CES_ID_GEN")
  @SequenceGenerator(name = "CES_ID_GEN", sequenceName = "CES_ID_GEN")
  private Long cesId;
  private String cesNom;
  private String cesSigle;
  private String cesEmail;
  private String cesTelephone;
  private String cesCellulaire;
  private String cesAdressePostale;
  private String cesSituationGeo;
  private BigDecimal repTauxComCourtage = new BigDecimal(5);
  @JoinColumn(name = "TYP_ID") @ManyToOne
  private Type type; //Cessionnaire ou Courtier
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
  @ManyToOne @JoinColumn(name = "cesStatut")
  private Statut statut;

    public Cessionnaire(Long cesId)
    {
      this.cesId = cesId;
    }

  @Override
  public String toString() {
    return cesId +"_"+ cesSigle;
  }
}
