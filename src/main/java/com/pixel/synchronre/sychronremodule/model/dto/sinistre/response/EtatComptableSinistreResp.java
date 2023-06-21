package com.pixel.synchronre.sychronremodule.model.dto.sinistre.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EtatComptableSinistreResp
{
    private Long sinId;
    private String sinCode;
    private BigDecimal sinMontant100;
    private BigDecimal sinMontantHonoraire;
    private LocalDate sinDateSurvenance;
    private LocalDate sinDateDeclaration;
    private String sinCommentaire;
    private String affCode;
    private String affAssure;
    private String affActivite;
    private BigDecimal affCapitalInitial;

    private BigDecimal dejaRegle;
    private BigDecimal resteARegler;
    private BigDecimal tauxDeReglement;

    private BigDecimal mtDejaReverse;
    private BigDecimal mtEnAttenteDeReversement;
    private List<DetailsEtatComptableSinistre> detailsEtatComptableSinistres;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor //Getter, Setter and constuctors
    public class DetailsEtatComptableSinistre
    {
        private Long cesId;
        private String cesNom;
        private String cesSigle;
        private BigDecimal mtTotalARegler;
        private BigDecimal mtDejaRegle; //Commission du réassureur propriétaire (NelsonRE)
        private BigDecimal mtResteARegler; //Montant du au cessionnaire
        private BigDecimal tauxDeReglement;
    }
}
