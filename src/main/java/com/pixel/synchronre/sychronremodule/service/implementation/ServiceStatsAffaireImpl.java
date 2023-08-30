package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sychronremodule.model.dao.AffaireStatsRepository;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dto.statistiques.AffaireStats;
import com.pixel.synchronre.sychronremodule.model.dto.statistiques.CritereStat;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCritereStats;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceStatsAffaire;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ServiceStatsAffaireImpl implements IServiceStatsAffaire
{
    private final IServiceCritereStats critereStatsService;
    private final AffaireStatsRepository statRepo;
    private final CedRepo cedRepo;
    private final CessionnaireRepository cesRepo;
    private final BigDecimal CENT = new BigDecimal(100);

    @Override
    public List<AffaireStats.DetailsAffaireStat> calculerDetailsAffaireStatsParCedantes(CritereStat criteres)
    {
        CritereStat criteresInit = critereStatsService.initCriteres(criteres);
        List<AffaireStats.DetailsAffaireStat> detailsParCedante = criteres.getCedIds().stream()
                .map(cedId->calculerDetailsAffaireStatsParCedante(criteresInit, cedId))
                .sorted(Comparator.comparing(AffaireStats.DetailsAffaireStat::getNbrAffaires).reversed())
                .collect(Collectors.toList());
        return detailsParCedante;
    }

    @Override
    public List<AffaireStats.DetailsAffaireStat> calculerDetailsAffaireStatsParCessionnaires(CritereStat criteres)
    {
        CritereStat criteresInit  = critereStatsService.initCriteres(criteres);
        List<AffaireStats.DetailsAffaireStat> detailsParCessionnaire = criteres.getCesIds().stream()
                .map(cesId->calculerDetailsAffaireStatsParCessionnaire(criteresInit, cesId))
                .sorted(Comparator.comparing(AffaireStats.DetailsAffaireStat::getNbrAffaires).reversed())
                .collect(Collectors.toList());
        return detailsParCessionnaire;
    }

    @Override
    public AffaireStats.DetailsAffaireStat calculerDetailsAffaireStatsParCedantes(CritereStat criteres, Long cedId)
    {
        criteres = critereStatsService.initCriteres(criteres);
        AffaireStats affaireStatsGlobal = statRepo.getAffaireStats(criteres.getExercices(), criteres.getCedIds() , criteres.getCesIds(), criteres.getStatutCreation(), criteres.getStaCodes(), criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());
        AffaireStats affaireStatsPourLaCedante = statRepo.getAffaireStats(criteres.getExercices(), cedId == null ? criteres.getCedIds() : Collections.singletonList(cedId), criteres.getCesIds(), criteres.getStatutCreation(), criteres.getStaCodes(), criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());

        AffaireStats.DetailsAffaireStat detailsAffaireStat = new AffaireStats.DetailsAffaireStat();
        detailsAffaireStat.setId(cedId);
        detailsAffaireStat.setLibelle( cedRepo.getCedNameById(cedId));
        detailsAffaireStat.setNbrAffaires(affaireStatsPourLaCedante.getNbrAffaire());
        detailsAffaireStat.setMtCapitalInitial(affaireStatsPourLaCedante.getMtTotalCapitalInitial());
        detailsAffaireStat.setMtSmpLci(affaireStatsPourLaCedante.getMtTotalSmpLci());

        if(affaireStatsGlobal.getNbrAffaire().equals(0)) return detailsAffaireStat;
        double tauxAffaire =  affaireStatsPourLaCedante.getNbrAffaire() / affaireStatsGlobal.getNbrAffaire();

        BigDecimal tauxCapitalInitial = affaireStatsPourLaCedante.getMtTotalCapitalInitial() == null ? BigDecimal.ZERO : affaireStatsPourLaCedante.getMtTotalCapitalInitial().divide(affaireStatsGlobal.getMtTotalCapitalInitial(), 4, RoundingMode.HALF_UP).multiply(CENT);
        BigDecimal tauxSmpLci = affaireStatsPourLaCedante.getMtTotalSmpLci() == null ? BigDecimal.ZERO : affaireStatsPourLaCedante.getMtTotalSmpLci().divide(affaireStatsGlobal.getMtTotalSmpLci(), 4, RoundingMode.HALF_UP).multiply(CENT);

        detailsAffaireStat.setTauxAffaires(new BigDecimal(tauxAffaire).multiply(CENT));
        detailsAffaireStat.setTauxCapitalInitial(tauxCapitalInitial);
        detailsAffaireStat.setTauxSmpLci(tauxSmpLci);
        return detailsAffaireStat;
    }

    @Override
    public AffaireStats.DetailsAffaireStat calculerDetailsAffaireStatsParCessionnaires(CritereStat criteres, Long cesId)
    {
        criteres = critereStatsService.initCriteres(criteres);
        AffaireStats affaireStatsGlobal = statRepo.getAffaireStats(criteres.getExercices(), criteres.getCedIds() , criteres.getCesIds(), criteres.getStatutCreation(), criteres.getStaCodes(), criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());
        AffaireStats affaireStatsPourLeCessionnaire = statRepo.getAffaireStatsParCessionnaires(criteres.getExercices(), criteres.getCedIds(), cesId == null ? criteres.getCesIds() : Collections.singletonList(cesId), criteres.getStatutCreation(), criteres.getStaCodes(), criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());

        AffaireStats.DetailsAffaireStat detailsAffaireStat = new AffaireStats.DetailsAffaireStat();
        detailsAffaireStat.setId(cesId);
        detailsAffaireStat.setLibelle(cesRepo.getCesNameById(cesId));
        detailsAffaireStat.setNbrAffaires(affaireStatsPourLeCessionnaire.getNbrAffaire());
        detailsAffaireStat.setMtCapitalInitial(null);
        detailsAffaireStat.setMtSmpLci(null);

        if(affaireStatsGlobal.getNbrAffaire().equals(0)) return detailsAffaireStat;
        double tauxAffaire =  affaireStatsPourLeCessionnaire.getNbrAffaire() / affaireStatsGlobal.getNbrAffaire();

        detailsAffaireStat.setTauxAffaires(new BigDecimal(tauxAffaire).multiply(CENT));
        return detailsAffaireStat;
    }

    @Override
    public AffaireStats.DetailsAffaireStat calculerDetailsAffaireStatsParCedante(CritereStat criteres, Long cedId)
    {
        return this.calculerDetailsAffaireStatsParCedantes(criteres, cedId);
    }

    @Override
    public AffaireStats.DetailsAffaireStat calculerDetailsAffaireStatsParCessionnaire(CritereStat criteres, Long cesId)
    {
        return this.calculerDetailsAffaireStatsParCessionnaires(criteres, cesId);
    }
}