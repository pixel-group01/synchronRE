package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
@Entity
public class Interlocuteur {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INT_ID_GEN")
  @SequenceGenerator(name = "INT_ID_GEN", sequenceName = "INT_ID_GEN")
  private Long intId;
  private String intNom;
  private String intPrenom;
  private String intTel;
  private String intEmail;
  @ManyToOne @JoinColumn(name = "int_ces_Id")
  private Cessionnaire cessionnaire;
  @ManyToOne @JoinColumn(name = "int_user_creator")
  private AppUser intUserCreator;
  @ManyToOne @JoinColumn(name = "int_fon_creator")
  private AppFunction intFonCreator;
  @ManyToOne
  private Statut statut;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;


}
