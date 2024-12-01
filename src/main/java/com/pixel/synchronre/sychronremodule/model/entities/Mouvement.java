package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class Mouvement {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "MVT_ID_GEN", sequenceName = "MVT_ID_GEN")
  private Long mvtId;
  @ManyToOne @JoinColumn(name = "mvt_sta_code")
  private Statut statut;
  private String mvtObservation;
  @ManyToOne @JoinColumn(name = "mvt_user_id")
  private AppUser mvtUser;
  @ManyToOne @JoinColumn(name = "mvt_function_id")
  private AppFunction mvtFunction;
  @ManyToOne @JoinColumn(name = "AFF_ID")
  private Affaire affaire;
  @ManyToOne @JoinColumn(name = "SIN_ID")
  private Sinistre sinistre;
  @ManyToOne @JoinColumn(name = "PLA_ID")
  private Repartition placement;
  @ManyToOne @JoinColumn(name = "traite_np_id")
  private TraiteNonProportionnel traiteNonProportionnel;
  @CreationTimestamp
  private LocalDateTime mvtDate;

  @Override
  public String toString() {
    return String.valueOf(mvtId) ;
  }
}
