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
    @ManyToOne @JoinColumn(name = "tranche_ID")
    private Tranche tranche;
    @ManyToOne @JoinColumn(name = "categorie_cedante_id")
    private CategorieCedante categorieCedante;
}
