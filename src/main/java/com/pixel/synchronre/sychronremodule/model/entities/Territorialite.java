package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Territorialite
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TERR_ID_GEN")
    @SequenceGenerator(name = "TERR_ID_GEN", sequenceName = "TERR_ID_GEN")
    private Long terrId;
    private String terrLibelle;
    @ManyToOne @JoinColumn(name = "org_pays_id")
    private OrganisationPays orgPays;
    @ManyToOne
    @JoinColumn(name = "traite_np_reference")
    private TraiteNonProportionnel traiteNonProportionnel;
    @ManyToOne
    @JoinColumn(name = "STA_CODE")
    protected Statut statut;
    @ManyToOne @JoinColumn(name = "user_creator")
    protected AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    protected AppFunction fonCreator;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;
}