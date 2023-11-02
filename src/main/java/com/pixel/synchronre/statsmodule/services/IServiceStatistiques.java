package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;

import java.util.List;

public interface IServiceStatistiques
{
    AffaireStats calculerAffaireStats(CritereStat criteres);

    List<Long> calculerAffaireStats2(CritereStat criteres);
}