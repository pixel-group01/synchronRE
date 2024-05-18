package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RisqueCouvertDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RISQUE_DET_ID_GEN")
    @SequenceGenerator(name = "RISQUE_DET_ID_GEN", sequenceName = "RISQUE_DET_ID_GEN")
    private Long risqueDetailsId;
    @ManyToOne @JoinColumn(name = "risque_id")
    private RisqueCouvert risqueCouvert;
    @ManyToOne @JoinColumn(name = "couverture_id")
    private Couverture couverture;

    public RisqueCouvertDetails(RisqueCouvert risqueCouvert, Couverture couverture) {
        this.risqueCouvert = risqueCouvert;
        this.couverture = couverture;
    }

    @Override
    public String toString() {
        return risqueDetailsId + "-" + risqueCouvert + "-" + couverture;
    }
}