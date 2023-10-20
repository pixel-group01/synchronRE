package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;

import java.util.List;

public interface IServiceStatsAffaire
{
    List<AffaireStats.DetailsAffaireStat> calculerDetailsAffaireStatsParCedantes(CritereStat criteres);

    List<AffaireStats.DetailsAffaireStat> calculerDetailsAffaireStatsParCessionnaires(CritereStat criteres);

    AffaireStats.DetailsAffaireStat calculerDetailsAffaireStatsParCedantes(CritereStat criteres, Long cedId);

    AffaireStats.DetailsAffaireStat calculerDetailsAffaireStatsParCessionnaires(CritereStat criteres, Long cesId);

    AffaireStats.DetailsAffaireStat calculerDetailsAffaireStatsParCedante(CritereStat criteres, Long cedId);

    AffaireStats.DetailsAffaireStat calculerDetailsAffaireStatsParCessionnaire(CritereStat criteres, Long cesId);
}
