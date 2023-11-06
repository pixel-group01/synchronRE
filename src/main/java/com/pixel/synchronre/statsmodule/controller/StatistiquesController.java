package com.pixel.synchronre.statsmodule.controller;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CommissionStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;
import com.pixel.synchronre.statsmodule.services.IServiceStatistiques;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/statistiques")
public class StatistiquesController
{
    private final IServiceStatistiques statistiquesService;


    @PostMapping(path = "/affaires")
    AffaireStats getStatistiquesAffaires(@RequestBody(required = false) CritereStat criteres)
    {
        return statistiquesService.calculerAffaireStats(criteres);
    }

    @PostMapping(path = "/commissions")
    CommissionStats getStatistiquesCommissions(@RequestBody(required = false) CritereStat criteres)
    {
        return statistiquesService.calculerCommissionStats(criteres);
    }
}
