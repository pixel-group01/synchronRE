package com.pixel.synchronre.statsmodule.model.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CritereStat
{
    private List<Long> exercices;
    private List<Long> cedIds;
    private List<Long> cesIds;
    private List<Long> affIds;
    private String statutCreation;
    private List<String> staCodes;
    private List<Long> couIds;
    private List<String> devCodes;
    private LocalDate dateEffet;
    private LocalDate dateEcheance;

    @Override
    public String toString() {
        return "CritereStat{" +
                "exercices=" + exercices +
                ", cedIds=" + cedIds +
                ", cesIds=" + cesIds +
                ", affIds=" + affIds +
                ", statutCreation='" + statutCreation + '\'' +
                ", staCodes=" + staCodes +
                ", couIds=" + couIds +
                ", devCodes=" + devCodes +
                ", dateEffet=" + dateEffet +
                ", dateEcheance=" + dateEcheance +
                '}';
    }
}
