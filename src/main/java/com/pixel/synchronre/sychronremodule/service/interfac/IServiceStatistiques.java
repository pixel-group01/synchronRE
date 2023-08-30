package com.pixel.synchronre.sychronremodule.service.interfac;

import com.pixel.synchronre.sychronremodule.model.dto.statistiques.AffaireStats;
import com.pixel.synchronre.sychronremodule.model.dto.statistiques.CritereStat;

public interface IServiceStatistiques
{
    AffaireStats calculerAffaireStats(CritereStat criteres);
}