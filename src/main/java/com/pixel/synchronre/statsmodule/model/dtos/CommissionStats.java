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

    public CommissionStats(BigDecimal mtTotalCommissionRea, BigDecimal mtTotalCommissionCourtage, BigDecimal mtTotalCommissionCedante) {
        this.mtTotalCommissionRea = mtTotalCommissionRea;
        this.mtTotalCommissionCourtage = mtTotalCommissionCourtage;
        this.mtTotalCommissionCedante = mtTotalCommissionCedante;
    }

    public CommissionStats(BigDecimal mtTotalCommissionRea, BigDecimal mtTotalCommissionCourtage, BigDecimal mtTotalCommissionCourtageDejaEncaisse, BigDecimal mtTotalCommissionCourtageRestantAEncaisse, BigDecimal mtTotalCommissionCedante, BigDecimal mtTotalCommissionCedanteDejaEncaisse, BigDecimal mtTotalCommissionCedanteRestantAEncaisse) {
        this.mtTotalCommissionRea = mtTotalCommissionRea;
        this.mtTotalCommissionCourtage = mtTotalCommissionCourtage;
        this.mtTotalCommissionCourtageDejaEncaisse = mtTotalCommissionCourtageDejaEncaisse;
        this.mtTotalCommissionCourtageRestantAEncaisse = mtTotalCommissionCourtageRestantAEncaisse;
        this.mtTotalCommissionCedante = mtTotalCommissionCedante;
        this.mtTotalCommissionCedanteDejaEncaisse = mtTotalCommissionCedanteDejaEncaisse;
        this.mtTotalCommissionCedanteRestantAEncaisse = mtTotalCommissionCedanteRestantAEncaisse;
    }

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