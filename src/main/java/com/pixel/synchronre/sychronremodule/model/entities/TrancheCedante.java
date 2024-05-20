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

@Table(name = "tranche_cedante")@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrancheCedante
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANCHE_CEDANTE_ID_GEN")
    @SequenceGenerator(name = "TRANCHE_CEDANTE_ID_GEN", sequenceName = "TRANCHE_CEDANTE_ID_GEN")
    private Long trancheCedanteId;
    private String trancheCedanteLibelle;
    @ManyToOne @JoinColumn(name = "tranche_ID")
    private Tranche tranche;
    @ManyToOne @JoinColumn(name = "categorie_cedante_id")
    private CategorieCedante categorieCedante;
    @ManyToOne @JoinColumn(name = "STA_CODE")
    private Statut statut;
    @ManyToOne @JoinColumn(name = "user_creator")
    private AppUser userCreator;
    @ManyToOne @JoinColumn(name = "fon_creator")
    private AppFunction fonCreator;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
