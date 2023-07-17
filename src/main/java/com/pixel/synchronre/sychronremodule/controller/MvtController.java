package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MvtMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/mouvements") @ResponseStatus(HttpStatus.OK)
public class MvtController
{
    private final MouvementRepository mvtRepo;

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

    @GetMapping(path = "/sinistre/get-message-retour-comptable/{sinId}")
    MvtMessage getMessageRetourComptaForSinistre(@PathVariable Long sinId)
    {
        String  msg = mvtRepo.getMvtMessage(null, null, sinId, "RET-COMPTA");
        return new MvtMessage(msg);
    }

    @GetMapping(path = "/sinistre/get-message-retour-validateur/{sinId}")
    MvtMessage getMessageRetourValidateurForSinistre(@PathVariable Long sinId)
    {
        String msg =  mvtRepo.getMvtMessage(null, null, sinId, "RET-VAL");
        return new MvtMessage(msg);
    }

    @GetMapping(path = "/sinistre/get-message-retour-souscripteur/{sinId}")
    MvtMessage getMessageRetourSouscripteurForSinistre(@PathVariable Long sinId)
    {
        String msg = mvtRepo.getMvtMessage(null, null, sinId, "RET");
        return new MvtMessage(msg);
    }

}
