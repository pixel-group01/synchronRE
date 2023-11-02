package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;

import java.util.List;

public interface IServiceStatsAffaire
{
    List<AffaireStats.DetailsAffaireStatParCedante> calculerDetailsAffaireStatsParCedantes(CritereStat criteres);

    List<AffaireStats.DetailsAffaireStatParCessionnaire> calculerDetailsAffaireStatsParCessionnaires(CritereStat criteres);

    AffaireStats.DetailsAffaireStatParCedante calculerDetailsAffaireStatsParCedantes(CritereStat criteres, Long cedId);

    AffaireStats.DetailsAffaireStatParCessionnaire calculerDetailsAffaireStatsParCessionnaires(CritereStat criteres, Long cesId);

    AffaireStats.DetailsAffaireStatParCedante calculerDetailsAffaireStatsParCedante(CritereStat criteres, Long cedId);

    AffaireStats.DetailsAffaireStatParCessionnaire calculerDetailsAffaireStatsParCessionnaire(CritereStat criteres, Long cesId);
}
