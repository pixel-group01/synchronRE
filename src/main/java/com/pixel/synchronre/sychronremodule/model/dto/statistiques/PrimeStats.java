package com.pixel.synchronre.sychronremodule.model.dto.statistiques;

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
}
