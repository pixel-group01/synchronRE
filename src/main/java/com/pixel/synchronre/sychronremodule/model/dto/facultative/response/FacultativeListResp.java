package com.pixel.synchronre.sychronremodule.model.dto.facultative.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FacultativeListResp
{
    private Long affId;
    private String affCode;
    private String affAssure;
    private String affActivite;
    private LocalDate affDateEffet;
    private LocalDate affDateEcheance;
    private String facNumeroPolice;
    private BigDecimal facCapitaux;
    private BigDecimal facSmpLci;
    private BigDecimal facPrime;
    private String affStatutCreation;
    private String devCode;
    private Long cedanteId;
    private String statutCode;
    private String staLibelle;
    private Long couId;
    private String couLibelle;
    private Long branId;
    private String branLibelle;
    private String cedNomFiliale;
    private String cedSigleFiliale;
    private Long exeCode;
    private boolean placementTermine;

    public FacultativeListResp(Long affId, String affCode, String affAssure, String affActivite, LocalDate affDateEffet, LocalDate affDateEcheance, String facNumeroPolice, BigDecimal facCapitaux, BigDecimal facSmpLci, BigDecimal facPrime, String affStatutCreation, String devCode, Long cedanteId, String statutCode, String staLibelle, String couLibelle, String cedNomFiliale, String cedSigleFiliale, Long exeCode) {
        this.affId = affId;
        this.affCode = affCode;
        this.affAssure = affAssure;
        this.affActivite = affActivite;
        this.affDateEffet = affDateEffet;
        this.affDateEcheance = affDateEcheance;
        this.facNumeroPolice = facNumeroPolice;
        this.facCapitaux = facCapitaux;
        this.facSmpLci = facSmpLci;
        this.facPrime = facPrime;
        this.affStatutCreation = affStatutCreation;
        this.devCode = devCode;
        this.cedanteId = cedanteId;
        this.statutCode = statutCode;
        this.staLibelle = staLibelle;
        this.couLibelle = couLibelle;
        this.cedNomFiliale = cedNomFiliale;
        this.cedSigleFiliale = cedSigleFiliale;
        this.exeCode = exeCode;
    }
}
