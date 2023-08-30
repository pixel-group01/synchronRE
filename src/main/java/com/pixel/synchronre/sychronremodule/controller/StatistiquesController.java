package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.statistiques.AffaireStats;
import com.pixel.synchronre.sychronremodule.model.dto.statistiques.CritereStat;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceStatistiques;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/statistiques")
public class StatistiquesController
{
    private final IServiceStatistiques statistiquesService;

    @PostMapping(path = "/affaires")
    AffaireStats getStatistiquesAffaires(@RequestBody(required = false) CritereStat criteres)
    {
        return statistiquesService.calculerAffaireStats(criteres);
    }
}
