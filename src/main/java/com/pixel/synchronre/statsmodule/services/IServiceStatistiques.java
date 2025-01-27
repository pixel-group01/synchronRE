package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CommissionStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;
import com.pixel.synchronre.statsmodule.model.views.VStatSituationFinParReaCed;
import com.pixel.synchronre.statsmodule.model.views.VStatSituationNoteCred;
import com.pixel.synchronre.statsmodule.model.views.VStatStuationFinCed;

import java.util.List;

public interface IServiceStatistiques
{
    AffaireStats calculerAffaireStats(CritereStat criteres);

    CommissionStats calculerCommissionStats(CritereStat criteres);

    List<VStatSituationFinParReaCed> getSituationParCedanteReassureur(Long exeCode, Long cedId, Long cesId, String statutEnvoie, String statutEncaissement);

    List<VStatStuationFinCed> getSituationParCedante(Long exeCode, Long cedId, String statutEnvoie, String statutEncaissement);

    List<VStatSituationNoteCred> getSituationNoteCredit(Long exeCode, Long cedId, Long cesId);
}