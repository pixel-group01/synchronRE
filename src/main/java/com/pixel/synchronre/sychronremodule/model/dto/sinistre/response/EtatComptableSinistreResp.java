package com.pixel.synchronre.sychronremodule.model.dto.sinistre.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EtatComptableSinistreResp
{
    private Long sinId;
    private BigDecimal sinMontant100;
    private LocalDate sinDateSurvenance;
    private LocalDate  sinDateDeclaration;
    private String sinCommentaire;
    private String affCode;
    private String affAssure;
    private String affActivite;
    private BigDecimal affCapitalInitial;

    private BigDecimal dejaRegle;
    private BigDecimal restARegle;
    private BigDecimal tauxDeReglement;
    private List<DetailsEtatComptableSinistre> detailsEtatComptableSinistres;

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
