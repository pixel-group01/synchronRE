package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class CompteCessionnaire
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "COMPTE_CES_ID_GEN", sequenceName = "COMPTE_CES_ID_GEN")
    private Long compteCesId;
    @Column(precision = 50, scale = 20)
    private BigDecimal taux;
    @Column(precision = 50, scale = 20)
    private BigDecimal prime;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "compte_ced_id")
    private CompteCedante compteCedante;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ces_id")
    private Cessionnaire cessionnaire;

    public CompteCessionnaire(BigDecimal taux, BigDecimal prime, CompteCedante compteCedante, Cessionnaire cessionnaire)
    {
        this.taux = taux;
        this.prime = prime;
        this.compteCedante = compteCedante;
        this.cessionnaire = cessionnaire;
    }

    public CompteCessionnaire(CompteCedante compteCedante, Cessionnaire cessionnaire) {
        this.compteCedante = compteCedante;
        this.cessionnaire = cessionnaire;
    }
}