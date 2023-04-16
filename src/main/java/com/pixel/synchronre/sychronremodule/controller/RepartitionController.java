package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.*;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/repartitions")
@Validated
public class RepartitionController
{
    private final IserviceRepartition repService;
    private final ParamCessionLegaleRepository pclRepo;

    @PostMapping(path = "/create")
    public RepartitionDetailsResp createRep(@Valid @RequestBody CreateRepartitionReq dto) throws UnknownHostException {
        return repService.createRepartition(dto);
    }

    @PostMapping(path = "/create-cession-legale-repartition")
    public List<RepartitionDetailsResp> createCesLegRep(@Valid @RequestBody List<CreateCesLegReq> dtos) throws UnknownHostException {
        return repService.createCesLegRepartitions(dtos);
    }

    @PostMapping(path = "/create-part-cedante-repartition")
    public RepartitionDetailsResp createPartCedRep(@Valid @RequestBody CreatePartCedRepartitionReq dto) throws UnknownHostException {
        return repService.createPartCedRepartition(dto);
    }

    @PostMapping(path = "/create-cedante-legale-repartition")
    public RepartitionDetailsResp createCedLegRep(@Valid @RequestBody CreateCedLegRepartitionReq dto) throws UnknownHostException {
        return repService.createCedLegRepartition(dto);
    }

    @PostMapping(path = "/create-placement")
    public RepartitionDetailsResp createPladRep(@Valid @RequestBody CreatePlaRepartitionReq dto) throws UnknownHostException {
        return repService.createPlaRepartition(dto);
    }



    @PutMapping(path = "/update")
    public RepartitionDetailsResp updateRep(@Valid @RequestBody UpdateRepartitionReq dto) throws UnknownHostException {
        return repService.updateRepartition(dto);
    }

    @GetMapping(path = "/list")
    public Page<RepartitionListResp> searchRep(@RequestParam(defaultValue = "", required = false) String key,
                                               @RequestParam(defaultValue = "0", required = false) int page,
                                               @RequestParam(defaultValue = "10", required = false) int size)
    {
        return repService.searchRepartition(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/ces-leg-param/{affId}")
    public List<ParamCessionLegaleListResp> getCesLegParam(@PathVariable @ExistingAffId Long affId)
    {
        return pclRepo.findByAffId(affId);
    }

    @GetMapping(path = "/calculate/by-taux/{affId}/{taux}")
    public CalculRepartitionResp calculRepartitionRespByTaux(@PathVariable @ExistingAffId Long affId, @PathVariable BigDecimal taux)
    {
        return repService.calculateRepByTaux(affId, taux);
    }

    @GetMapping(path = "/calculate/by-taux-besoin/{affId}/{tauxBesoin}")
    public CalculRepartitionResp calculRepartitionRespByTauxBesoin(@PathVariable @ExistingAffId  Long affId, @PathVariable BigDecimal tauxBesoin)
    {
        return repService.calculateRepByTauxBesoinFac(affId, tauxBesoin);
    }

    @GetMapping(path = "/calculate/by-capital/{affId}/{capital}")
    public CalculRepartitionResp calculRepartitionRespByCapital(@PathVariable @ExistingAffId  Long affId, @PathVariable BigDecimal capital)
    {
        return repService.calculateRepByCapital(affId, capital);
    }
}