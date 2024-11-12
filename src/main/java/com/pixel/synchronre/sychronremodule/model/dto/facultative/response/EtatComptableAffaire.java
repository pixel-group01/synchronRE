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
    private BigDecimal primeNetteCmsCedante; //Prime net de la commission cedante (prime totale sur l'affaire - commission cedante)

    private BigDecimal mtTotalPrimeBruteCes; //
    private BigDecimal mtTotalPrimeCessionnaireNetteComCed;
    private BigDecimal mtTotalPrimeNetteCes; //Montant du aux cessionnaires
    private BigDecimal dejaRegle;
    private BigDecimal resteARegler;
    private BigDecimal tauxDeReglement;
    private BigDecimal dejaReverse;
    private BigDecimal mtAttenteReversement;
    private BigDecimal resteAReverser;
    private BigDecimal tauxDeReversement;
    private BigDecimal reserveCourtier;

    private List<DetailsEtatComptable> detailsEtatComptables;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class DetailsEtatComptable
    {
        private Long cesId;
        private String cesNom;
        private String cesSigle;
        private BigDecimal mtCapital;
        private BigDecimal mtSousCms;
        private BigDecimal mtCmsCedante;
        private BigDecimal mtCmsCourtage; //Commission du réassureur propriétaire (NelsonRE)
        private BigDecimal tauxSousCms;
        private BigDecimal tauxCmsCedante;
        private BigDecimal tauxCmsCourtage;
        private BigDecimal mtPrimeBruteCes;
        private BigDecimal mtPrimeNetteCes; //Montant du au cessionnaire
        private BigDecimal dejaReverse;
        private BigDecimal resteAReverser;
        private BigDecimal tauxDeReversement;
    }
}
