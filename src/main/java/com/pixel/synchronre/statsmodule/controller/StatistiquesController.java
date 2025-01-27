package com.pixel.synchronre.statsmodule.controller;

import com.pixel.synchronre.statsmodule.model.dtos.AffaireStats;
import com.pixel.synchronre.statsmodule.model.dtos.CommissionStats;
import com.pixel.synchronre.statsmodule.model.dtos.CritereStat;
import com.pixel.synchronre.statsmodule.model.views.VStatSituationFinParReaCed;
import com.pixel.synchronre.statsmodule.services.IServiceStatistiques;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp;
import com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseListResp;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceListResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp;
import com.pixel.synchronre.statsmodule.model.views.VStatSituationNoteCred;
import com.pixel.synchronre.statsmodule.model.views.VStatStuationFinCed;
import com.pixel.synchronre.sychronremodule.service.interfac.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/statistiques")
public class StatistiquesController
{
    private final IServiceStatistiques statistiquesService;
    private final IserviceExercie exoService;
    private final ICedanteService cedService;
    private final IserviceCessionnaire cessionnaireService;
    private final IserviceCouverture couvertureService;
    private final IServiceDevise deviseService;
    private final IServiceStatut statutService;



    @GetMapping(path = "/exercices")
    public List<ExerciceListResp> searchExercice(@RequestParam(defaultValue = "") String key) throws UnknownHostException {
        return exoService.searchExercice(key);
    }

    @GetMapping(path = "/cessionnaires")
    public Page<CessionnaireListResp> searchCessionnaires(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size) throws UnknownHostException {
        return cessionnaireService.searchCessionnaire(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/cedantes")
    public Page<ReadCedanteDTO> searchCedente(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size) throws UnknownHostException {
        return cedService.searchCedente(key, PageRequest.of(page, size));
    }
    @GetMapping(path = "/statuts")
    public Page<StatutListResp> searchStatuts(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return statutService.searchStatut(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/couvertures")
    public Page<CouvertureListResp> searchCouvertures(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size) throws UnknownHostException {
        return couvertureService.searchCouverture(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/devises")
    public List<DeviseListResp> searchDevises(@RequestParam(defaultValue = "") String key) throws UnknownHostException {
        return deviseService.searchDevise(key);
    }

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

    @GetMapping(path = "/affaires-fac/situation-par-cedante-reassureur")
    public List<VStatSituationFinParReaCed> getSituationParCedanteReassureur(@RequestParam(required = false) Long exeCode,
                                                                             @RequestParam(required = false) Long cedId,
                                                                             @RequestParam(required = false) Long cesId,
                                                                             @RequestParam(required = false) String statutEnvoie,
                                                                             @RequestParam(required = false) String statutEncaissement)
    {
        return statistiquesService.getSituationParCedanteReassureur(exeCode,cedId,cesId,statutEnvoie,statutEncaissement);
    }

    @GetMapping(path = "/affaires-fac/situation-par-cedante")
    public List<VStatStuationFinCed> getSituationParCedante(@RequestParam(required = false) Long exeCode,
                                                            @RequestParam(required = false) Long cedId,
                                                            @RequestParam(required = false) String statutEnvoie,
                                                            @RequestParam(required = false) String statutEncaissement)
    {
        return statistiquesService.getSituationParCedante(exeCode,cedId,statutEnvoie,statutEncaissement);
    }

    @GetMapping(path = "/affaires-fac/situation-note-de-credit")
    public List<VStatSituationNoteCred> getSituationNoteCredit(@RequestParam(required = false) Long exeCode,
                                                               @RequestParam(required = false) Long cedId,
                                                               @RequestParam(required = false) Long cesId)
    {
        return statistiquesService.getSituationNoteCredit(exeCode,cedId,cesId);
    }

}
