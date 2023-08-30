package com.pixel.synchronre.sychronremodule.model.dto.statistiques;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AffaireStats
{
    private Long nbrAffaire;
    private BigDecimal mtTotalCapitalInitial;
    private BigDecimal mtTotalSmpLci;

    public AffaireStats(Long nbrAffaire, BigDecimal mtTotalCapitalInitial, BigDecimal mtTotalSmpLci) {
        this.nbrAffaire = nbrAffaire;
        this.mtTotalCapitalInitial = mtTotalCapitalInitial;
        this.mtTotalSmpLci = mtTotalSmpLci;
    }

    private List<DetailsAffaireStat> detailsAffaireParCedantes;
    private List<DetailsAffaireStat> detailsAffaireParCessionnaires;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class DetailsAffaireStat
    {
        private Long id;
        private String libelle;
        private Long nbrAffaires;
        private BigDecimal tauxAffaires;

        private BigDecimal mtCapitalInitial;
        private BigDecimal tauxCapitalInitial;

        private BigDecimal mtSmpLci;
        private BigDecimal tauxSmpLci;
    }

}
