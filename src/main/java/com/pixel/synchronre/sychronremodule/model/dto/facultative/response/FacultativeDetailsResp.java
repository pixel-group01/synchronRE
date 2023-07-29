package com.pixel.synchronre.sychronremodule.model.dto.facultative.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FacultativeDetailsResp
{
    private Long affId;
    private String affCode;
    private String affAssure;
    private String affActivite;
    private LocalDate affDateEffet;
    private LocalDate affDateEcheance;
    private String facNumeroPolice;
    private BigDecimal affCapitalInitial;
    private Long facSmpLci;
    private BigDecimal facPrime;
    private String affStatutCreation;
    private Long cedenteId;
    private String devCode;
    private String cedNomFiliale;
    private String cedSigleFiliale;
    private String statutCode;
    protected Long couvertureId;
    private String couLibelle;
    private BigDecimal restARepartir;
    private BigDecimal capitalDejaReparti;

    private Long branId;
    private String branLibelle;
    private Long couId;
    private Long exeCode;
    private boolean placementTermine;

    private EtatComptableAffaire etatComptable;

    //staLibelle
    //couId
    //branId

}
