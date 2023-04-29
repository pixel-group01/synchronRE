package com.pixel.synchronre.sychronremodule.model.dto.facultative.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EtatComptableAffaire
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
    private String devCode;
    private Long cedenteId;
    private String statutCode;
    protected Long couvertureId;
    private BigDecimal restARepartir;
    private BigDecimal capitalDejaReparti;

    private BigDecimal mtTotalCmsCedante;
    private BigDecimal mtTotalCmsCourtage; //Commission du réassureur propriétaire (NelsonRE)
    private BigDecimal mtTotalPrimeNetteCes; //Montant du aux cessionnaires
    private BigDecimal dejaRegle;
    private BigDecimal resteARegler;
    private BigDecimal tauxDeReglement;
    private BigDecimal dejaReverse;
    private BigDecimal resteAReverser;
    private BigDecimal tauxDeReversement;
    private List<DetailsEtatComptable> detailsEtatComptables;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class DetailsEtatComptable
    {
        private Long cesId;
        private String cesNom;
        private String cesSigle;
        private BigDecimal mtCmsCedante;
        private BigDecimal mtCmsCourtage; //Commission du réassureur propriétaire (NelsonRE)
        private BigDecimal mtPrimeNetteCes; //Montant du au cessionnaire
        private BigDecimal dejaReverse;
        private BigDecimal resteAReverser;
        private BigDecimal tauxDeReversement;
    }
}
