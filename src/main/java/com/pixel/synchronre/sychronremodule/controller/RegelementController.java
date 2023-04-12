package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdatePaiementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.PaiementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
@RequiredArgsConstructor
public class RegelementController {
    private final IserviceReglement regService;

    @PostMapping(path = "/paiements/create")
    public PaiementDetailsResp createPaiement(@RequestBody @Valid CreatePaiementReq dto) throws UnknownHostException {
        return regService.createPaiement(dto);
    }

    @PutMapping(path = "/paiements/update")
    public PaiementDetailsResp updatePaiement(@RequestBody @Valid UpdatePaiementReq dto) throws UnknownHostException {
        return regService.updatePaiement(dto);
    }

    @GetMapping(path = "/paiements/list")
    public Page<ReglementListResp> searchPaiements(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return regService.searchReglement(key, PageRequest.of(page, size));
    }
}
