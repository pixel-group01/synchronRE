package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.validator.ExistingAffId;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.UpdateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.CalculRepartitionResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceRepartition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.UnknownHostException;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/repartitions")
@Validated
public class RepartitionController
{
    private final IserviceRepartition repService;

    @PostMapping(path = "/create")
    public RepartitionDetailsResp createRep(CreateRepartitionReq dto) throws UnknownHostException {
        return repService.createRepartition(dto);
    }

    @PutMapping(path = "/update")
    public RepartitionDetailsResp updateRep(UpdateRepartitionReq dto) throws UnknownHostException {
        return repService.updateRepartition(dto);
    }

    @GetMapping(path = "/list")
    public Page<RepartitionListResp> searchRep(@RequestParam(defaultValue = "", required = false) String key,
                                               @RequestParam(defaultValue = "0", required = false) int page,
                                               @RequestParam(defaultValue = "10", required = false) int size)
    {
        return repService.searchRepartition(key, PageRequest.of(page, size));
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