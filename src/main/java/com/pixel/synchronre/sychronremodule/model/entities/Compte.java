package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.sychronremodule.model.enums.PERIODICITE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "COMPTE_ID_GEN", sequenceName = "COMPTE_ID_GEN")
    private Long compteId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "tranche_id")
    private Tranche tranche;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "periode_id")
    private Periode periode;

    public Compte(Tranche tranche, Periode periode) {
        this.tranche = tranche;
        this.periode = periode;
    }

    public Compte(Long compteId) {
        this.compteId = compteId;
    }
}
