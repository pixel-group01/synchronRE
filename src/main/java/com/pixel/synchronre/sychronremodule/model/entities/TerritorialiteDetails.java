package com.pixel.synchronre.sychronremodule.model.entities;

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
public class TerritorialiteDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TERR_DET_ID_GEN")
    @SequenceGenerator(name = "TERR_DET_ID_GEN", sequenceName = "TERR_DET_ID_GEN")
    private Long terrDetId;
    @ManyToOne @JoinColumn(name = "org_pays_id")
    private Organisation organisation;
    @ManyToOne @JoinColumn(name = "pays_code")
    private Pays pays;
    @ManyToOne @JoinColumn(name = "terr_id")
    private Territorialite territorialite;
    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    public TerritorialiteDetails(Organisation organisation, Pays pays, Territorialite territorialite) {

        this.organisation = organisation;
        this.pays = pays;
        this.territorialite = territorialite;
    }
}