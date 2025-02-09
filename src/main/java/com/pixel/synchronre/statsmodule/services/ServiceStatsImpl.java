package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.*;
import com.pixel.synchronre.statsmodule.model.repositories.*;
import com.pixel.synchronre.statsmodule.model.views.VStatSituationFinParReaCed;
import com.pixel.synchronre.statsmodule.model.views.VStatSituationNoteCred;
import com.pixel.synchronre.statsmodule.model.views.VStatStuationFinCed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final VStatStuationFinCedRepository situationCedRepo;
    private final VStatSituationNoteCredRepository situationNoteCredRepo;
    private final ChiffreAffaireRepo caRepo;
    private final StatsChiffreAffaireMapper statsChiffreAffaireMapper;


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
    public Page<VStatSituationFinParReaCed> getSituationParCedanteReassureur(Long exeCode, Long cedId, Long cesId, String statutEnvoie, String statutEncaissement, Pageable pageable) {
        statutEnvoie = statutEnvoie == null || statutEnvoie.trim().equals("") ? null : statutEnvoie;
        statutEncaissement = statutEncaissement == null || statutEncaissement.trim().equals("") ? null : statutEncaissement;
        return situationReaCedRepo.getSituationParCedanteReassureur(exeCode,cedId,cesId,statutEnvoie,statutEncaissement, pageable);
    }

    @Override
    public Page<VStatStuationFinCed> getSituationParCedante(Long exeCode, Long cedId, String statutEnvoie, String statutEncaissement, Pageable pageable) {
        statutEnvoie = statutEnvoie == null || statutEnvoie.trim().equals("") ? null : statutEnvoie;
        statutEncaissement = statutEncaissement == null || statutEncaissement.trim().equals("") ? null : statutEncaissement;
        return situationCedRepo.getSituationParCedante(exeCode,cedId,statutEnvoie,statutEncaissement, pageable);
    }

    @Override
    public Page<VStatSituationNoteCred> getSituationNoteCredit(Long exeCode, Long cedId, Long cesId, Pageable pageable) {
        return situationNoteCredRepo.getSituationNoteCredit(exeCode,cedId,cesId, pageable);
    }

    @Override
    public Page<StatChiffreAffaireParPeriodeDTO> getStatsChiffreAffaire(Long exeCode, Long cedId, Long cesId, LocalDate debut, LocalDate fin, Pageable pageable) {
        Page<Object[]>  statObjsPage = caRepo.getStatsChiffreAffaire(exeCode, cedId, cesId, debut, fin, pageable);
        List<Object[]>  statObjs = statObjsPage.getContent();
        List<StatChiffreAffaireParPeriodeDTO> stats = statsChiffreAffaireMapper.mapToStatsChiffreAffaireDTOList(statObjs);
        return new PageImpl<>(stats, pageable, statObjsPage.getTotalElements());
    }
}