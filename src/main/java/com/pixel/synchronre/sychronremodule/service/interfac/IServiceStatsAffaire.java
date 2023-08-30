package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.statistiques.AffaireStats;
import com.pixel.synchronre.sychronremodule.model.dto.statistiques.CritereStat;

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
