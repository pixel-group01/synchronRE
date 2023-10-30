package com.pixel.synchronre.statsmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

public class SinistreStats
{
    private Long nbrSinistres;
    private BigDecimal mtTotalSinistres;
    private BigDecimal mtTotalHonoraires;
    private BigDecimal mtChargeTotaleSinistre; //(mtTotalSinistres + mtTotalHonoraires)
    private BigDecimal mtTotalPlacements;
    private BigDecimal mtTotalHorsPlacements;
    private BigDecimal mtTotalDejaPaye;
    private BigDecimal mtTotalRestantAPaye;

    private List<SinistreStats.DetailsSinistresStat> detailsSinistresParAffaires;
    private List<SinistreStats.DetailsSinistresStat> detailsSinistresParCedantes;
    private List<SinistreStats.DetailsSinistresStat> detailsSinistresParCessionnaires;


    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class DetailsSinistresStat
    {
        private Long id;
        private String libelle;
        private Long nbrSinistres;
        private BigDecimal tauxSinistres;
        private BigDecimal mtTotalSinistres;
        private BigDecimal mtTotalHonoraires;
        private BigDecimal mtChargeTotaleSinistre; //(mtTotalSinistres + mtTotalHonoraires)
        private BigDecimal mtTotalPlacements;
        private BigDecimal mtTotalHorsPlacements;
        private BigDecimal mtTotalDejaPaye;
        private BigDecimal mtTotalRestantAPaye;
    }
}
