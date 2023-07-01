package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sharedmodule.enums.TypeStatut;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
public class Cedante {
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
  private String cedInterlocuteur;
  //@ManyToOne() @JoinColumn(name = "ced_ces_id")
  //private Cessionnaire cessionnaire;
  @ManyToOne @JoinColumn(name = "ced_pays_code")
  private Pays pays;
  @ManyToOne @JoinColumn(name = "ced_user_creator")
  private AppUser cedUserCreator;
  @ManyToOne @JoinColumn(name = "ced_fon_creator")
  private AppFunction cedFonCreator;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
  @ManyToOne
  private Statut cedStatut;

  public Cedante(Long cedId) {
    this.cedId = cedId;
  }
}
