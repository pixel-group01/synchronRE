package com.pixel.synchronre.sychronremodule.model.dto.repartition.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RepartitionTraiteNPResp
{
    private Long repId;
    private BigDecimal repPrime;
    private BigDecimal repTaux;
    private BigDecimal repTauxCourtierPlaceur; //TODO A Valider
    private BigDecimal repTauxCourtier; //Taux NelsonRe
    private Long cesId;
    private String cesNom;
    private String cesSigle;
    private String cesEmail;
    private String cesTelephone;
    private Long traiNPId;
    private String traiReference;
    private String traiNumero;
    private String traiLibelle;
    private boolean isAperiteur;
    private boolean repStatut;
    private String repStaCode;
    private BigDecimal tauxRestant;
    private BigDecimal tauxDejaReparti;

    public RepartitionTraiteNPResp(Long repId, BigDecimal repPrime, BigDecimal repTaux, BigDecimal repTauxCourtierPlaceur,
                                   BigDecimal repTauxCourtier, Long cesId, String cesNom, String cesSigle, String cesEmail,
                                   String cesTelephone, Long traiNPId, String traiReference, String traiNumero,
                                   String traiLibelle, boolean isAperiteur, boolean repStatut, String repStaCode)
    {
        this.repId = repId;
        this.repPrime = repPrime;
        this.repTaux = repTaux;
        this.repTauxCourtierPlaceur = repTauxCourtierPlaceur;
        this.repTauxCourtier = repTauxCourtier;
        this.cesId = cesId;
        this.cesNom = cesNom;
        this.cesSigle = cesSigle;
        this.cesEmail = cesEmail;
        this.cesTelephone = cesTelephone;
        this.traiNPId = traiNPId;
        this.traiReference = traiReference;
        this.traiNumero = traiNumero;
        this.traiLibelle = traiLibelle;
        this.isAperiteur = isAperiteur;
        this.repStatut = repStatut;
        this.repStaCode = repStaCode;
    }
}
