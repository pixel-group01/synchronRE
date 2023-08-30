package com.pixel.synchronre.sychronremodule.model.dto.statistiques;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CritereStat
{
    private List<Long> exercices;
    private List<Long> cedIds;
    private List<Long> cesIds;
    private String statutCreation;
    private List<String> staCodes;
    private List<Long> couIds;
    private List<String> devCodes;
    private LocalDate dateEffet;
    private LocalDate dateEcheance;
}
