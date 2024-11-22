package com.pixel.synchronre.sychronremodule.model.entities;

import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
public class CompteDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "COMPTE_DET_ID_GEN", sequenceName = "COMPTE_DET_ID_GEN")
    private Long compteDetId;
    private BigDecimal debit;
    private BigDecimal credit;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "type_id")
    private Type typeCompteDet;
    String typeCode;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "compte_ced_id")
    private CompteCedante compteCedante;
}