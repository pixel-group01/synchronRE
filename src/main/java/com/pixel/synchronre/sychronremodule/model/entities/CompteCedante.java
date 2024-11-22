package com.pixel.synchronre.sychronremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class CompteCedante
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "COMPTE_CED_ID_GEN", sequenceName = "COMPTE_CED_ID_GEN")
    private Long compteCedId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "compte_id")
    private Compte compte;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ced_id")
    private Cedante cedante;
}