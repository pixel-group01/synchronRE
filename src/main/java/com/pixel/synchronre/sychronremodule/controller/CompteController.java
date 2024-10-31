package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteTraiteDto;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCompte;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/comptes")
public class CompteController {
    private final IserviceCompte compteService;

    @GetMapping(path = "/traites/{traiteNpId}")
    CompteTraiteDto getCompteTraite(@PathVariable Long traiteNpId){
        return compteService.getCompteTraite(traiteNpId);
    }
}
