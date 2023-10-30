package com.pixel.synchronre.statsmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommissionStats
{
    private BigDecimal mtTotalCommissionRea;

    private BigDecimal mtTotalCommissionCourtage;
    private BigDecimal mtTotalCommissionCourtageDejaEncaisse;
    private BigDecimal mtTotalCommissionCourtageRestantAEncaisse;

    private BigDecimal mtTotalCommissionCedante;
    private BigDecimal mtTotalCommissionCedanteDejaEncaisse;
    private BigDecimal mtTotalCommissionCedanteRestantAEncaisse;

    private List<DetailCommissionStats> detailCommissionStatsParCedantes;
    private List<DetailCommissionStats> detailCommissionStatsParCessionnaires;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class DetailCommissionStats
    {
        private Long id; //cedId ou cesId
        private String libelle; //cedLibelle ou cesLibelle
        private BigDecimal mtCommissionCourtage;
        private BigDecimal tauxCommissionCourtage;

        private BigDecimal mtCommissionCourtageDejaEncaisse;
        private BigDecimal tauxCommissionCourtageDejaEncaisse;

        private BigDecimal mtCommissionCourtageRestantAEncaisse;
        private BigDecimal tauxCommissionCourtageRestantAEncaisse;
    }
}