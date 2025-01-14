package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class Reglement {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REG_ID_GEN")
  @SequenceGenerator(name = "REG_ID_GEN", sequenceName = "REG_ID_GEN")
  private Long regId;
  private String regReference;
  private LocalDate regDate;
  @Column(precision = 1000, scale = 980)
  private BigDecimal regMontant;
  private String regMontantLettre;
  @Column(precision = 1000, scale = 980)
  private BigDecimal regMontantNetteCommissionRea;
  @Column(precision = 1000, scale = 980)
  private BigDecimal regCommission;
  @Column(precision = 1000, scale = 980)
  private BigDecimal regCommissionCourt;
  @Column(precision = 1000, scale = 980)
  private BigDecimal regCommissionCed;
  private boolean regStatut;
  private String regMode;
  @ManyToOne @JoinColumn(name = "type_id") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  private Type typeReglement;
  @ManyToOne @JoinColumn(name = "aff_id")
  private Affaire affaire;
  @ManyToOne @JoinColumn(name = "sin_id")
  private Sinistre sinistre;
  @ManyToOne @JoinColumn(name = "ces_id") @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  private Cessionnaire cessionnaire;
  @ManyToOne @JoinColumn(name = "user_id")
  private AppUser appUser;
  @ManyToOne @JoinColumn(name = "function_id")
  private AppFunction functionCreator;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Override
  public String toString() {
    return regId + "_" + regReference + "_" + regMontant + "_" + typeReglement;
  }

  public Reglement(Long regId) {
    this.regId = regId;
  }

  public Long getSinId()
  {
    if(sinistre != null ) return sinistre.getSinId();
    return null;
  }

  public Long getAffId()
  {
    if(affaire != null && affaire.getAffId() != null) return affaire.getAffId();
    if(sinistre != null && sinistre.getAffaire() != null && sinistre.getAffaire().getAffId() != null) return sinistre.getAffaire().getAffId();
    return null;
  }

  public String getAffCode()
  {
    if(affaire != null) return affaire.getAffCode();
    if(sinistre != null && sinistre.getAffaire() != null) return sinistre.getAffaire().getAffCode();
    return null;
  }


  public String getAffAssure()
  {
    if(affaire != null) return affaire.getAffAssure();
    if(sinistre != null && sinistre.getAffaire() != null) return sinistre.getAffaire().getAffAssure();
    return null;
  }

  public String getAffActivite()
  {
    if(affaire != null) return affaire.getAffActivite();
    if(sinistre != null && sinistre.getAffaire() != null) return sinistre.getAffaire().getAffActivite();
    return null;
  }

  public LocalDate getAffDateEffet()
  {
    if(affaire != null) return affaire.getAffDateEffet();
    if(sinistre != null && sinistre.getAffaire() != null) return sinistre.getAffaire().getAffDateEffet();
    return null;
  }

  public LocalDate getAffDateEcheance()
  {
    if(affaire != null) return affaire.getAffDateEcheance();
    if(sinistre != null && sinistre.getAffaire() != null) return sinistre.getAffaire().getAffDateEcheance();
    return null;
  }

  public Long getCesId()
  {
    if(cessionnaire != null ) return cessionnaire.getCesId();
    return null;
  }
}
