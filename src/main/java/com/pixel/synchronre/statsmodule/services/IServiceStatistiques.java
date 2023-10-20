package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;

public interface IServiceStatistiques
{
    AffaireStats calculerAffaireStats(CritereStat criteres);
}