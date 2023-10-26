package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MvtMessage;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceMouvement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
@RequestMapping(path = "/mouvements")
public class MouvementController
{
    private final MouvementRepository mvtRepo;
    private final IServiceMouvement mvtService;
    @GetMapping(path = "/affaire/{affId}")
    Page<MouvementListResp> findByAffaire(@PathVariable Long affId, @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "1000") int size)
    {
        return mvtService.findMouvementById(affId,null,PageRequest.of(page, size));
    }

    @GetMapping(path = "/affaire/message-retour/{affId}")
    MvtMessage getMessageRetourAffaire(@PathVariable Long affId)
    {
        String message = mvtRepo.getMessageRetourForAffaire(affId);
        return new MvtMessage(message == null ? "" : message);
    }

    @GetMapping(path = "/placement/message-retour/{plaId}")
    MvtMessage getMessageRetourPlacement(@PathVariable Long plaId)
    {
        String message = mvtRepo.getMessageRetourForPlacement(plaId);
        return new MvtMessage(message == null ? "" : message);
    }

    @GetMapping(path = "/placement/message-refus/{plaId}")
    MvtMessage getMessageRefusPlacement(@PathVariable Long plaId)
    {
        String message = mvtRepo.getMessageRefusForPlacement(plaId);
        return new MvtMessage(message == null ? "" : message);
    }

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

    @GetMapping(path = "/sinistre/get-histo/{sinId}")
    Page<MouvementListResp> findBySinistre(@PathVariable Long sinId, @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "1000") int size)
    {
        return mvtRepo.findMouvementById(null,sinId, PageRequest.of(page, size));
    }
}
