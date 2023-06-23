package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.EtatComptableSinistreResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSinistre;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

@RestController @RequiredArgsConstructor
@RequestMapping(path ="/sinistres")
public class SinistreController
{
    private final IServiceSinistre sinService;

    @PostMapping(path = "/create")
    public SinistreDetailsResp createReglement(@RequestBody @Valid CreateSinistreReq dto) throws UnknownHostException {
        return sinService.createSinistre(dto);
    }

    @PutMapping(path = "/update")
    public SinistreDetailsResp updateReglement(@RequestBody @Valid UpdateSinistreReq dto) throws UnknownHostException {
        return sinService.updateSinistre(dto);
    }

    @GetMapping(path = "/list")
    public Page<SinistreDetailsResp> searchReglement(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return sinService.searchSinistre(key, new ArrayList<>(), PageRequest.of(page, size));
    }

    @GetMapping(path = "/etat-comptable/{sinId}")
    public EtatComptableSinistreResp getEtatComptable(@PathVariable Long sinId) throws UnknownHostException {
        return sinService.getEtatComptable(sinId);
    }

    @PutMapping(path = "/transmettre/{sinId}")
    public void transmettreSinistreAuCourtier(@PathVariable Long sinId) throws UnknownHostException
    {
        sinService.transmettreSinistreAuCourtier(sinId);
    }
}
