package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CommissionStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;
import com.pixel.synchronre.statsmodule.model.dtos.VStatSituationFinParReaCed;
import com.pixel.synchronre.statsmodule.model.repositories.AffaireStatsRepository;
import com.pixel.synchronre.statsmodule.model.repositories.VStatSituationFinReaCedRepository;
import com.pixel.synchronre.statsmodule.model.repositories.VStatSituationNoteCredRepository;
import com.pixel.synchronre.statsmodule.model.repositories.V_StatStuationFinCedRepository;
import com.pixel.synchronre.sychronremodule.model.views.VStatSituationNoteCred;
import com.pixel.synchronre.sychronremodule.model.views.V_StatStuationFinCed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class ServiceStatsImpl implements IServiceStatistiques
{
    private final IServiceStatsAffaire affaireStatsService;
    private final AffaireStatsRepository statRepo;
    private final IServiceCritereStats critereStatsService;
    private final VStatSituationFinReaCedRepository situationReaCedRepo;
    private final V_StatStuationFinCedRepository situationCedRepo;
    private final VStatSituationNoteCredRepository situationNoteCredRepo;


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
    public CommissionStats calculerCommissionStats(CritereStat criteres)
    {
        criteres = critereStatsService.initCriteres(criteres);
        CommissionStats commissionStats = statRepo.getCommissionStats(criteres.getExercices(), criteres.getCedIds(),
                criteres.getCesIds(), criteres.getCouIds(), criteres.getDevCodes(),
                criteres.getDateEffet(), criteres.getDateEcheance());
        return commissionStats;
    }

    @Override
    public List<VStatSituationFinParReaCed> getSituationParCedanteReassureur(Long exeCode, Long cedId, Long cesId, String statutEnvoie, String statutEncaissement) {
        statutEnvoie = statutEnvoie == null || statutEnvoie.trim().equals("") ? null : statutEnvoie;
        statutEncaissement = statutEncaissement == null || statutEncaissement.trim().equals("") ? null : statutEncaissement;
        return situationReaCedRepo.getSituationParCedanteReassureur(exeCode,cedId,cesId,statutEnvoie,statutEncaissement);
    }

    @Override
    public List<V_StatStuationFinCed> getSituationParCedante(Long exeCode, Long cedId, String statutEnvoie, String statutEncaissement) {
        statutEnvoie = statutEnvoie == null || statutEnvoie.trim().equals("") ? null : statutEnvoie;
        statutEncaissement = statutEncaissement == null || statutEncaissement.trim().equals("") ? null : statutEncaissement;
        return situationCedRepo.getSituationParCedante(exeCode,cedId,statutEnvoie,statutEncaissement);
    }

    @Override
    public List<VStatSituationNoteCred> getSituationNoteCredit(Long exeCode, Long cedId, Long cesId) {
        return situationNoteCredRepo.getSituationNoteCredit(exeCode,cedId,cesId);
    }
}