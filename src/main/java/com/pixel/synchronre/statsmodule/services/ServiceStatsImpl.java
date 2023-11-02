package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;
import com.pixel.synchronre.statsmodule.model.repositories.AffaireStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class ServiceStatsImpl implements IServiceStatistiques
{
    private final IServiceStatsAffaire affaireStatsService;
    private final AffaireStatsRepository statRepo;
    private final IServiceCritereStats critereStatsService;

    @Override
    public AffaireStats calculerAffaireStats(CritereStat criteres)
    {
        criteres = critereStatsService.initCriteres(criteres);
        AffaireStats affaireStats = statRepo.getAffaireStats(criteres.getExercices(),
                criteres.getCesIds(), criteres.getStatutCreation(), criteres.getStaCodes(),
                criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());

        affaireStats.setDetailsAffaireParCedantes(affaireStatsService.calculerDetailsAffaireStatsParCedantes(criteres));
        affaireStats.setDetailsAffaireParCessionnaires(affaireStatsService.calculerDetailsAffaireStatsParCessionnaires(criteres));
        return affaireStats;
    }

    @Override
    public List<Long> calculerAffaireStats2(CritereStat criteres)
    {
        criteres = critereStatsService.initCriteres(criteres);
        List<Long> affaireStats = statRepo.getAffaireStats2(criteres.getExercices(), criteres.getCedIds(),
                criteres.getCesIds(), criteres.getStatutCreation(), criteres.getStaCodes(),
                criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());

        return affaireStats;
    }
}