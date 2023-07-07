package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.EtatComptableSinistreResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSinistre;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

@RestController @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
@RequestMapping(path ="/sinistres")
public class SinistreController
{
    private final IServiceSinistre sinService;

    //Tab saisie par la cedante : affiche les sinistres saisies par la cedante
    @GetMapping(path = "/saisi-by-cedante")
    public Page<SinistreDetailsResp> searchSinistreByCedante(@RequestParam(defaultValue = "") String key,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size){
        return sinService.searchSinistreSaisiByCedante(key, new ArrayList<>(), PageRequest.of(page, size));
    }

                                                            @PostMapping(path = "/create")
    public SinistreDetailsResp createSinistre(@RequestBody @Valid CreateSinistreReq dto) throws UnknownHostException {
        return sinService.createSinistre(dto);
    }

    @PutMapping(path = "/update")
    public SinistreDetailsResp updateSinistre(@RequestBody @Valid UpdateSinistreReq dto) throws UnknownHostException {
        return sinService.updateSinistre(dto);
    }

    @GetMapping(path = "/list")
    public Page<SinistreDetailsResp> searchSinistre(@RequestParam(defaultValue = "") String key,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size)
            throws UnknownHostException {
        return sinService.searchSinistre(key, new ArrayList<>(), PageRequest.of(page, size));
    }


    @GetMapping(path = "/etat-comptable/{sinId}")
    public EtatComptableSinistreResp getEtatComptableSinistre(@PathVariable Long sinId) throws UnknownHostException {
        return sinService.getEtatComptable(sinId);
    }

    @PutMapping(path = "/transmettre/{sinId}")
    public void transmettreSinistreAuCourtier(@PathVariable Long sinId) throws UnknownHostException
    {
        sinService.transmettreSinistreAuCourtier(sinId);
    }
}
