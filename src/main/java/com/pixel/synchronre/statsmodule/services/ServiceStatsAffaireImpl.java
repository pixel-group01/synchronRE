package com.pixel.synchronre.statsmodule.services;

import com.pixel.synchronre.statsmodule.model.repositories.AffaireStatsRepository;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Service @RequiredArgsConstructor
public class ServiceStatsAffaireImpl implements IServiceStatsAffaire
{
    private final IServiceCritereStats critereStatsService;
    private final AffaireStatsRepository statRepo;
    private final CedRepo cedRepo;
    private final CessionnaireRepository cesRepo;
    private final BigDecimal CENT = new BigDecimal(100);

    @Override
    public List<AffaireStats.DetailsAffaireStatParCedante> calculerDetailsAffaireStatsParCedantes(CritereStat criteres)
    {
        CritereStat criteresInit = critereStatsService.initCriteres(criteres);
        List<AffaireStats.DetailsAffaireStatParCedante> detailsParCedante = criteres.getCedIds().stream()
                .map(cedId->calculerDetailsAffaireStatsParCedante(criteresInit, cedId))
                .sorted(Comparator.comparing(AffaireStats.DetailsAffaireStatParCedante::getNbrAffaires).reversed())
                .collect(Collectors.toList());
        return detailsParCedante;
    }

    @Override
    public List<AffaireStats.DetailsAffaireStatParCessionnaire> calculerDetailsAffaireStatsParCessionnaires(CritereStat criteres)
    {
        CritereStat criteresInit  = critereStatsService.initCriteres(criteres);
        List<AffaireStats.DetailsAffaireStatParCessionnaire> detailsParCessionnaire = criteres.getCesIds().stream()
                .map(cesId->calculerDetailsAffaireStatsParCessionnaire(criteresInit, cesId))
                .sorted(Comparator.comparing(AffaireStats.DetailsAffaireStatParCessionnaire::getNbrAffaires).reversed())
                .collect(Collectors.toList());
        return detailsParCessionnaire;
    }

    @Override
    public AffaireStats.DetailsAffaireStatParCedante calculerDetailsAffaireStatsParCedantes(CritereStat criteres, Long cedId)
    {
        criteres = critereStatsService.initCriteres(criteres);
        AffaireStats affaireStatsGlobal = statRepo.getAffaireStats(criteres.getExercices(), criteres.getCedIds(), criteres.getStatutCreation(), criteres.getStaCodes(), criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());
        AffaireStats affaireStatsPourLaCedante = statRepo.getAffaireStats(criteres.getExercices(), cedId == null ? criteres.getCedIds() : Collections.singletonList(cedId), criteres.getStatutCreation(), criteres.getStaCodes(), criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());

        AffaireStats.DetailsAffaireStatParCedante detailsAffaireStat = new AffaireStats.DetailsAffaireStatParCedante();
        detailsAffaireStat.setId(cedId);
        detailsAffaireStat.setLibelle( cedRepo.getCedNameById(cedId));
        detailsAffaireStat.setNbrAffaires(affaireStatsPourLaCedante.getNbrAffaires());
        detailsAffaireStat.setMtCapitalInitial(affaireStatsPourLaCedante.getMtTotalCapitalInitial() == null  || affaireStatsPourLaCedante.getMtTotalCapitalInitial().compareTo(ZERO) == 0 ? ZERO : affaireStatsPourLaCedante.getMtTotalCapitalInitial().setScale(0));
        detailsAffaireStat.setMtSmpLci(affaireStatsPourLaCedante.getMtTotalSmpLci() == null  || affaireStatsPourLaCedante.getMtTotalSmpLci().compareTo(ZERO) == 0 ? ZERO : affaireStatsPourLaCedante.getMtTotalSmpLci().setScale(0));

        if(affaireStatsGlobal.getNbrAffaires() == 0) return detailsAffaireStat;
        BigDecimal tauxAffaire =  BigDecimal.valueOf(affaireStatsPourLaCedante.getNbrAffaires()).multiply(CENT).divide(BigDecimal.valueOf(affaireStatsGlobal.getNbrAffaires()),4, RoundingMode.HALF_UP) ;

        BigDecimal tauxCapitalInitial = affaireStatsPourLaCedante.getMtTotalCapitalInitial() == null ? ZERO : affaireStatsPourLaCedante.getMtTotalCapitalInitial().divide(affaireStatsGlobal.getMtTotalCapitalInitial(), 4, RoundingMode.HALF_UP).multiply(CENT);
        BigDecimal tauxSmpLci = affaireStatsPourLaCedante.getMtTotalSmpLci() == null ? ZERO : affaireStatsPourLaCedante.getMtTotalSmpLci().divide(affaireStatsGlobal.getMtTotalSmpLci(), 4, RoundingMode.HALF_UP).multiply(CENT);

        detailsAffaireStat.setTauxAffaires(tauxAffaire);
        detailsAffaireStat.setTauxCapitalInitial(tauxCapitalInitial);
        detailsAffaireStat.setTauxSmpLci(tauxSmpLci);
        return detailsAffaireStat;
    }

    @Override
    public AffaireStats.DetailsAffaireStatParCessionnaire calculerDetailsAffaireStatsParCessionnaires(CritereStat criteres, Long cesId)
    {
        criteres = critereStatsService.initCriteres(criteres);
        AffaireStats affaireStatsGlobal = statRepo.getAffaireStats(criteres.getExercices(), criteres.getCedIds(), criteres.getStatutCreation(), criteres.getStaCodes(), criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());
        AffaireStats affaireStatsPourLeCessionnaire = statRepo.getAffaireStatsParCessionnaires(criteres.getExercices(), cesId == null ? criteres.getCesIds() : Collections.singletonList(cesId), criteres.getStatutCreation(), criteres.getStaCodes(), criteres.getCouIds(), criteres.getDevCodes(), criteres.getDateEffet(), criteres.getDateEcheance());

        AffaireStats.DetailsAffaireStatParCessionnaire detailsAffaireStat = new AffaireStats.DetailsAffaireStatParCessionnaire();
        detailsAffaireStat.setId(cesId);
        detailsAffaireStat.setLibelle(cesRepo.getCesNameById(cesId));
        detailsAffaireStat.setNbrAffaires(affaireStatsPourLeCessionnaire.getNbrAffaires());
        detailsAffaireStat.setMtSmpLciAccepte(affaireStatsPourLeCessionnaire.getMtTotalSmpLciAccpte() == null || affaireStatsPourLeCessionnaire.getMtTotalSmpLciAccpte().compareTo(ZERO) == 0 ? ZERO : affaireStatsPourLeCessionnaire.getMtTotalSmpLciAccpte().setScale(0));
        BigDecimal mtTotalSmpLci = affaireStatsGlobal.getMtTotalSmpLci();
        if(mtTotalSmpLci == null || mtTotalSmpLci.compareTo(ZERO) == 0) return detailsAffaireStat;
        BigDecimal tauxSmpLciAccepte = detailsAffaireStat.getMtSmpLciAccepte() == null ? ZERO : detailsAffaireStat.getMtSmpLciAccepte().multiply(CENT).divide(mtTotalSmpLci, 4, RoundingMode.HALF_UP);
        detailsAffaireStat.setTauxSmpLciAccepte(tauxSmpLciAccepte);

        return detailsAffaireStat;
    }

    @Override
    public AffaireStats.DetailsAffaireStatParCedante calculerDetailsAffaireStatsParCedante(CritereStat criteres, Long cedId)
    {
        return this.calculerDetailsAffaireStatsParCedantes(criteres, cedId);
    }

    @Override
    public AffaireStats.DetailsAffaireStatParCessionnaire calculerDetailsAffaireStatsParCessionnaire(CritereStat criteres, Long cesId)
    {
        return this.calculerDetailsAffaireStatsParCessionnaires(criteres, cesId);
    }
}