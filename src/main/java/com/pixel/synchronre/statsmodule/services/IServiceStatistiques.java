package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CommissionStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;
import com.pixel.synchronre.statsmodule.model.dtos.StatChiffreAffaireParPeriodeDTO;
import com.pixel.synchronre.statsmodule.model.views.VStatSituationFinParReaCed;
import com.pixel.synchronre.statsmodule.model.views.VStatSituationNoteCred;
import com.pixel.synchronre.statsmodule.model.views.VStatStuationFinCed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IServiceStatistiques
{
    AffaireStats calculerAffaireStats(CritereStat criteres);

    CommissionStats calculerCommissionStats(CritereStat criteres);

    Page<VStatSituationFinParReaCed> getSituationParCedanteReassureur(Long exeCode, Long cedId, Long cesId, String statutEnvoie, String statutEncaissement, Pageable pageable);

    Page<VStatStuationFinCed> getSituationParCedante(Long exeCode, Long cedId, String statutEnvoie, String statutEncaissement, Pageable pageable);

    Page<VStatSituationNoteCred> getSituationNoteCredit(Long exeCode, Long cedId, Long cesId, Pageable pageable);

    Page<StatChiffreAffaireParPeriodeDTO> getStatsChiffreAffaire(Long exeCode, Long cedId, Long cesId, LocalDate debut, LocalDate fin, Pageable pageable);
}