package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.constants.RepStatutGroup;
import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculationRepartitionRespDto;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCalculRepartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/repartitions")
@Validated @ResponseStatus(HttpStatus.OK)
public class RepartitionFacController
{
    private final IserviceRepartition repService;
    private final ParamCessionLegaleRepository pclRepo;
    private final RepartitionRepository repRepo;
    private final IserviceCalculRepartition calculRepartitionService;

    @DeleteMapping(path = "/delete-placement/{repId}")
    public void deletePlacement(@PathVariable Long repId) throws UnknownHostException
    {
        repService.deletePlacement(repId);
    }

    @PostMapping(path = "/save")
    public CalculationRepartitionRespDto saveRep(@RequestBody CalculationRepartitionRespDto dto) throws UnknownHostException {
        return this.calculRepartitionService.saveRep(dto);
    }

    @PostMapping(path = "/create-placement")
    public RepartitionDetailsResp createPladRep(@Valid @RequestBody CreatePlaRepartitionReq dto) throws UnknownHostException {
        return repService.createPlaRepartition(dto);
    }

    @GetMapping(path = "/list-placement-saisie/{affId}")
    public Page<RepartitionListResp> searchPlacementSaisie(@PathVariable(required = false) Long affId,
                                                @RequestParam(defaultValue = "", required = false) String key,
                                               @RequestParam(defaultValue = "0", required = false) int page,
                                               @RequestParam(defaultValue = "1000", required = false) int size)
    {
        return repService.searchRepartition(key, affId, "REP_PLA", RepStatutGroup.tabSaisie,PageRequest.of(page, size));
    }

    @GetMapping(path = "/list-placement-en-attente-de-validation/{affId}")
    public Page<RepartitionListResp> searchPlacementValideEnAttenteDeValidation(@PathVariable(required = false) Long affId,
                                                     @RequestParam(defaultValue = "", required = false) String key,
                                                     @RequestParam(defaultValue = "0", required = false) int page,
                                                     @RequestParam(defaultValue = "1000", required = false) int size)
    {
        return repService.searchRepartition(key, affId, "REP_PLA", RepStatutGroup.tabAttenteValidation,PageRequest.of(page, size));
    }

    @GetMapping(path = "/list-placement-valide/{affId}")
    public Page<RepartitionListResp> searchPlacementValide(@PathVariable(required = false) Long affId,
                                                           @RequestParam(defaultValue = "", required = false) String key,
                                                           @RequestParam(defaultValue = "0", required = false) int page,
                                                           @RequestParam(defaultValue = "1000", required = false) int size)
    {
        return repService.searchRepartition(key, affId, "REP_PLA", RepStatutGroup.tabValide,PageRequest.of(page, size));
    }

    @GetMapping(path = "/ces-leg-param/{affId}")
    public List<ParamCessionLegaleListResp> getCesLegParam(@PathVariable @ExistingAffId Long affId)
    {
        return repService.getCesLegParam(affId);
    }

    @GetMapping(path = "/calculate/by-capital/{affId}/{capital}")
    public CalculRepartitionResp calculRepartitionRespByCapital(@PathVariable @ExistingAffId  Long affId, @PathVariable BigDecimal capital,
                                                                @RequestParam(required = false) BigDecimal tauxCmsRea,
                                                                @RequestParam(required = false) BigDecimal tauxCmsCourtage,
                                                                @RequestParam(required = false) Long repIdToUpdate)
    {
        return calculRepartitionService.calculateRepByCapital(affId, capital,tauxCmsRea,tauxCmsCourtage, repIdToUpdate);
    }

    @PostMapping(path = "/calculate")
    public CalculationRepartitionRespDto calculRepartitionRespByCapital(@RequestBody CalculationRepartitionRespDto dto)
    {
        return calculRepartitionService.calculateRepByDto(dto);
    }

    @GetMapping(path = "/calculate/{affId}")
    public CalculationRepartitionRespDto calculRepartitionRespByCapital(@PathVariable Long affId)
    {
        return calculRepartitionService.calculateRepByAffId(affId);
    }

    @PutMapping(path = "/transmettre-placement-pour-validation/{plaId}")
    void transmettrePourValadation(@PathVariable Long plaId) throws UnknownHostException {
        repService.transmettrePlacementPourValidation(plaId);
    }

    @PutMapping(path = "/transmettre-placements-pour-validation")
    void transmettrePourValadation(@RequestParam List<Long> plaIds) throws UnknownHostException {
        repService.transmettrePlacementPourValidation(plaIds);
    }

    @PutMapping(path = "/valider-placement/{plaId}")
    void validerPlacement(@PathVariable Long plaId) throws UnknownHostException {
        repService.validerPlacement(plaId);
    }

    @PutMapping(path = "/valider-placements")
    void validerPlacements(@RequestParam List<Long> plaIds) {
        repService.validerPlacement(plaIds);
    }

    @PutMapping(path = "/retourner-placement/{plaId}")
    void retournerPlacement(@PathVariable Long plaId, @RequestBody String motif) throws UnknownHostException {
        repService.retournerPlacement(plaId, motif);
    }

    @PutMapping(path = "/envoyer-note-cession-fac/{plaId}")
    boolean envoyerNoteCession(@PathVariable Long plaId) throws Exception {
        repService.transmettreNoteDeCession(plaId);
        return true;
    }

    @PutMapping(path = "/envoyer-notes-cession-fac")
    void envoyerNoteCession(@RequestParam List<Long> plaIds)
    {
        repService.transmettreNoteDeCession(plaIds);
    }

    @PutMapping(path = "/refuser-placement/{plaId}")
    void refuserPlacement(@PathVariable Long plaId, @RequestBody String message) throws UnknownHostException {
        repService.refuserPlacement(plaId, message);
    }

    @PutMapping(path = "/annuler-placement/{plaId}")
    void annulerPlacement(@PathVariable Long plaId) throws UnknownHostException {
        repService.annulerPlacement(plaId);
    }

    @PutMapping(path = "/accepter-placement/{plaId}")
    void accepterPlacement(@PathVariable Long plaId) throws UnknownHostException {
        repService.accepterPlacement(plaId);
    }
}
