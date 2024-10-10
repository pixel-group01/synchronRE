package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class CompteCedante {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "COMPTE_CED_ID_GEN", sequenceName = "COMPTE_CED_ID_GEN")
    private Long compteCedId;
    @Column(precision = 50, scale = 20)
    private BigDecimal primeOrigine;
    @Column(precision = 50, scale = 20)
    private BigDecimal primeAjustement;
    @Column(precision = 50, scale = 20)
    private BigDecimal sinistrePaye;
    @Column(precision = 50, scale = 20)
    private BigDecimal depotSapConstitue;
    @Column(precision = 50, scale = 20)
    private BigDecimal depotSapLibere;
    @Column(precision = 50, scale = 20)
    private BigDecimal interetDepotLibere;
    @Column(precision = 50, scale = 20)
    private BigDecimal sousTotal;
    @Column(precision = 50, scale = 20)
    private BigDecimal soldeCedante;
    @Column(precision = 50, scale = 20)
    private BigDecimal soldeReassureur;
    @Column(precision = 50, scale = 20)
    private BigDecimal totalMouvement;
    @Column(precision = 50, scale = 20)
    private BigDecimal totalPrimeCessionnaire;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "compte_id")
    private Compte compte;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ced_id")
    private Cedante cedante;
}