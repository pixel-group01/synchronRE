package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class OrganisationPays {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_PAYS_ID_GEN")
    @SequenceGenerator(name = "ORG_PAYS_ID_GEN", sequenceName = "ORG_PAYS_ID_GEN", allocationSize = 1)
    private Long orgPayId;
    @ManyToOne
    @JoinColumn(name = "PAY_CODE")
    private Pays pays;
    @ManyToOne
    @JoinColumn(name = "ORGAN_CODE")
    private Organisation organisation;
    @ManyToOne
    @JoinColumn(name = "STA_CODE")
    private Statut statut;
    @ManyToOne @JoinColumn(name = "user_creator")
    private AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    private AppFunction fonCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public OrganisationPays(Pays pays, Organisation organisation, Statut statut, AppUser userCreator, AppFunction fonCreator) {
        this.pays = pays;
        this.organisation = organisation;
        this.statut = statut;
        this.userCreator = userCreator;
        this.fonCreator = fonCreator;
    }
}
