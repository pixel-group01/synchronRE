package com.pixel.synchronre.statsmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrimeStats
{
    private BigDecimal mtTotalDuParLesCedantes;
    private BigDecimal mtTotalDejaPayeParLesCedantes;
    private BigDecimal mtTotalRestantAPayerParLesCedantes;
    private BigDecimal mtTotalDuAuxCessionnaires;
    private BigDecimal mtTotalDejaReverseAuxCessionnaires;
    private BigDecimal mtTotalEnAttenteDeReversementAuxCessionnaires;
    private BigDecimal mtTotalRestantAReverseAuxCessionnaires;

    private List<DetailsPrimeStats> detailsPrimeStatsParCedantes;
    private List<DetailsPrimeParCessionnairesStats> detailsPrimeStatsParCessionnaires;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class DetailsPrimeStats
    {
        private Long cedId;
        private String cedLibelle;

        private BigDecimal mtDuParLaCedante;
        private BigDecimal tauxDuParLaCedante;

        private BigDecimal mtDejaPayeParLaCedante;
        private BigDecimal tauxDejaPayeParLaCedante;

        private BigDecimal mtRestantAPayerParLaCedante;
        private BigDecimal tauxRestantAPayerParLaCedante;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class DetailsPrimeParCessionnairesStats
    {
        private Long cesId;
        private String cesLibelle;

        private BigDecimal mtDuAuCessionnaire;
        private BigDecimal tauxDuAuCessionnaire;

        private BigDecimal mtDejaPayeAuCessionnaire;
        private BigDecimal tauxDejaPayeAuCessionnaire;

        private BigDecimal mtRestantAPayerAuCessionnaire;
        private BigDecimal tauxRestantAPayerAuCessionnaire;
    }
}
