package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/mouvements") @ResponseStatus(HttpStatus.OK)
public class MvtController
{
    private MouvementRepository mvtRepo;

    @GetMapping(path = "/affaire/get-message-retour/{affId}")
    String getMessageRetourForAffaire(@PathVariable Long affId)
    {
        return mvtRepo.getMessageRetourForAffaire(affId);
    }

    @GetMapping(path = "/placement/get-message-retour/{plaId}")
    String getMessageRetourForPlacement(@PathVariable Long plaId)
    {
        return mvtRepo.getMessageRetourForPlacement(plaId);
    }
}
