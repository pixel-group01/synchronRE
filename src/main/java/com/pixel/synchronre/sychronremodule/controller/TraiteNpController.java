package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.CreateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTraiteNP;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/traite-non-proportionnel")
public class TraiteNpController
{
    private final IServiceTraiteNP traiteNpService;

    @PostMapping(path = "/create")
    TraiteNPResp create(@Valid @RequestBody CreateTraiteNPReq dto)
    {
        return traiteNpService.create(dto);
    }

    @PutMapping(path = "/update")
    TraiteNPResp update(@Valid @RequestBody UpdateTraiteNPReq dto){
        return traiteNpService.update(dto);
    }

    @GetMapping(path = "/list")
    List<TraiteNPResp> list(@RequestParam(required = false) Long cedId,
                      @RequestParam(required = false) List<String> staCodes,
                      Long exeCode)
    {
        return traiteNpService.list(cedId, staCodes, exeCode);
    }

    //Tab saisie par le souscripteur : affiche les traites saisies par le souscripteur
    @GetMapping(path = "/saisi-by-souscripteur")
    public Page<TraiteNPResp> searchTraiteBySouscripteur(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(required = false) Long fncId,
                                                         @RequestParam(required = false) Long userId,
                                                         @RequestParam(required = false) Long cedId,
                                                         @RequestParam(required = false) List<String> staCodes, Long exeCode,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size){
        return traiteNpService.searchSaisieSouscripteur(key, fncId, userId, cedId, staCodes, exeCode, PageRequest.of(page, size));
    }
    //Tab en cours de validation par le souscripteur : affiche les traites saisies par le souscripteur
    @GetMapping(path = "/en-cours-de-validation")
    public Page<TraiteNPResp> enCoursValidation(@RequestParam(defaultValue = "") String key,
                                                         @RequestParam(required = false) Long fncId,
                                                         @RequestParam(required = false) Long userId,
                                                         @RequestParam(required = false) Long cedId,
                                                         @RequestParam(required = false) List<String> staCodes, Long exeCode,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size){
        return traiteNpService.enCoursDeValidation(key, fncId, userId, cedId, staCodes, exeCode, PageRequest.of(page, size));
    }

    //Tab en cours de regelement par le comptable : affiche les traites en cours de relement par le comptable
    @GetMapping(path = "/en-cours-de-reglement")
    public Page<TraiteNPResp> enCoursDeReglements(@RequestParam(defaultValue = "") String key,
                                                @RequestParam(required = false) Long fncId,
                                                @RequestParam(required = false) Long userId,
                                                @RequestParam(required = false) Long cedId,
                                                @RequestParam(required = false) List<String> staCodes, Long exeCode,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size){
        return traiteNpService.enCoursDeReglement(key, fncId, userId, cedId, staCodes, exeCode, PageRequest.of(page, size));
    }
    //Tab en cours de regelement par le comptable : affiche les traites en cours de relement par le comptable
    @GetMapping(path = "/solde")
    public Page<TraiteNPResp> solde(@RequestParam(defaultValue = "") String key,
                                                 @RequestParam(required = false) Long fncId,
                                                 @RequestParam(required = false) Long userId,
                                                 @RequestParam(required = false) Long cedId,
                                                 @RequestParam(required = false) List<String> staCodes, Long exeCode,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size){
        return traiteNpService.solde(key, fncId, userId, cedId, staCodes, exeCode, PageRequest.of(page, size));
    }

    //Tab des traites archive : affiche les traites archivés
    @GetMapping(path = "/archives")
    public Page<TraiteNPResp> archive(@RequestParam(defaultValue = "") String key,
                                    @RequestParam(required = false) Long fncId,
                                    @RequestParam(required = false) Long userId,
                                    @RequestParam(required = false) Long cedId,
                                    @RequestParam(required = false) List<String> staCodes, Long exeCode,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size){
        return traiteNpService.archive(key, fncId, userId, cedId, staCodes, exeCode, PageRequest.of(page, size));
    }

    @PutMapping(path = "/retourner-au-souscripteur")
    public boolean retournerAuSouscripteur(@Valid @RequestBody MvtReq dto)
    {
        traiteNpService.retournerAuSouscripteur(dto);
        return true;
    }

    @PutMapping(path = "/retourner-au-validateur")
    public boolean retournerAuValidateur(@Valid @RequestBody MvtReq dto)
    {
        traiteNpService.retournerAuValidateur(dto);
        return true;
    }

    @GetMapping(path = "/search")
    Page<TraiteNPResp> create(@RequestParam(defaultValue = "") String key,
                              @RequestParam(required = false) Long fncId,
                              @RequestParam(required = false) Long userId,
                              @RequestParam(required = false) Long cedId,
                              @RequestParam(required = false) List<String> staCodes, Long exeCode,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size)
    {
        return traiteNpService.search(key, fncId, userId, cedId, staCodes, exeCode, PageRequest.of(page, size));
    }

    @GetMapping(path = "/edit/{traiId}")
    UpdateTraiteNPReq edit(@PathVariable Long traiId){
        return traiteNpService.edit(traiId);
    }

    @GetMapping(path = "/details/{traiteNpId}")
    public TraiteNPResp getDetails(@PathVariable Long traiteNpId)
    {
        return traiteNpService.getTraiteDetails(traiteNpId);
    }

    @PutMapping(path = "/transmettre-pour-validation/{traiteNpId}")
    public boolean transmettrePourValidation(@PathVariable Long traiteNpId, @RequestParam(defaultValue = "10") int size)
    {
        traiteNpService.transmettreTraiteAuValidateur(traiteNpId, size);
        return true;
    }
    @PutMapping(path = "/valider/{traiteNpId}")
    public boolean valider(@PathVariable Long traiteNpId) throws Exception
    {
        traiteNpService.valider(traiteNpId);
        return true;
    }

}
