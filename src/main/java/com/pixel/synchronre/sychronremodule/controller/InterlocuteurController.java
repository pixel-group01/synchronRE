package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.InterlocuteurRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.CreateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.UpdateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.CreateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.UpdateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.ICedanteService;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceInterlocuteur;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
@RequestMapping(path = "/interlocuteurs")
public class InterlocuteurController
{
    private final IServiceInterlocuteur interlocuteurService;
    private final InterlocuteurRepository interRepo;
    
    @PostMapping(path = "/create")
    public InterlocuteurListResp createInterlocuteur(@RequestBody @Valid CreateInterlocuteurReq dto) throws UnknownHostException {
        return interlocuteurService.createInterlocuteur(dto);
    }

    @PutMapping(path = "/update")
    public InterlocuteurListResp updateInterlocuteur(@RequestBody @Valid UpdateInterlocuteurReq dto) throws UnknownHostException {
        return interlocuteurService.updateInterlocuteur(dto);
    }

    @GetMapping(path = "/list/{cesId}")
    public Page<InterlocuteurListResp> searchInterlocuteur(@RequestParam(defaultValue = "") String key, @PathVariable Long cesId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size) throws UnknownHostException {
        return interlocuteurService.searchInterlocuteur(key, cesId, PageRequest.of(page, size));
    }

    @GetMapping(path = "/for-placement/{plaId}")
    public Page<InterlocuteurListResp> searchInterlocuteursForPlacement(@RequestParam(defaultValue = "") String key, @PathVariable Long plaId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100000") int size) throws UnknownHostException {
        return interlocuteurService.searchInterlocuteurForPlacement(key, plaId, PageRequest.of(page, size));
    }

    @DeleteMapping(path = "/delete-interlocuteur/{intId}")
    public void deleteInterlocuteur(@PathVariable Long intId) throws UnknownHostException
    {
        interlocuteurService.deleteInterlocuteur(intId);
    }
}
