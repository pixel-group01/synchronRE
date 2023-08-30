package com.pixel.synchronre.sychronremodule.service.implementation;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.*;
import com.pixel.synchronre.sychronremodule.model.dto.statistiques.CritereStat;
import com.pixel.synchronre.sychronremodule.model.entities.Exercice;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCritereStats;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

@Service @RequiredArgsConstructor
public class ServiceCritereStatsImpl implements IServiceCritereStats
{
    private final CedRepo cedRepo;
    private final CessionnaireRepository cesRepo;
    private final DeviseRepository devRepo;
    private final IserviceExercie exoService;
    private final StatutRepository statutRepo;
    private final CouvertureRepository couRepo;

    @Override
    public CritereStat initCriteres() {
        return this.initCriteres(null);
    }

    @Override
    public CritereStat initCriteres(CritereStat criteres)
    {
        Exercice exoCourant = exoService.getExerciceCourant();
        if(exoCourant == null) throw new AppException("L'exercice courant n'est pas défini");
        CritereStat criteresParDefaut = new CritereStat();
        criteresParDefaut.setExercices(Collections.singletonList(exoCourant.getExeCode()));
        criteresParDefaut.setCedIds(cedRepo.findAllIds());
        criteresParDefaut.setCesIds(cesRepo.findAllIds());
        criteresParDefaut.setStaCodes(statutRepo.findActiveStaCodes());
        criteresParDefaut.setCouIds(couRepo.findAllIds());
        criteresParDefaut.setDevCodes(devRepo.findAllDevCodes());
        criteresParDefaut.setDateEffet(LocalDate.of(exoCourant.getExeCode().intValue(), 01, 01));
        criteresParDefaut.setDateEcheance(LocalDate.of(exoCourant.getExeCode().intValue(), 12, 31));

        if(criteres == null) return criteresParDefaut;
        criteres.setExercices(criteres.getExercices()==null || criteres.getExercices().isEmpty() ? criteresParDefaut.getExercices() : criteres.getExercices());
        criteres.setCedIds(criteres.getCedIds()==null || criteres.getCesIds().isEmpty() ? criteresParDefaut.getCedIds() : criteres.getCedIds());
        criteres.setCesIds(criteres.getCesIds()==null || criteres.getCesIds().isEmpty() ? criteresParDefaut.getCesIds() : criteres.getCesIds());
        criteres.setStaCodes(criteres.getStaCodes()==null || criteres.getStaCodes().isEmpty() ? criteresParDefaut.getStaCodes() : criteres.getStaCodes());
        criteres.setCouIds(criteres.getCouIds()==null || criteres.getCouIds().isEmpty() ? criteresParDefaut.getCouIds() : criteres.getCouIds());
        criteres.setDevCodes(criteres.getDevCodes()==null || criteres.getDevCodes().isEmpty() ? criteresParDefaut.getDevCodes() : criteres.getDevCodes());
        criteres.setDateEffet(criteres.getDateEffet()==null ? criteresParDefaut.getDateEffet() : criteres.getDateEffet());
        criteres.setDateEcheance(criteres.getDateEcheance()==null  ? criteresParDefaut.getDateEcheance() : criteres.getDateEcheance());

        return criteres;
    }
}
