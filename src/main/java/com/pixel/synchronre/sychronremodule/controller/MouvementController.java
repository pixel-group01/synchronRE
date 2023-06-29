package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MvtMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
@RequestMapping(path = "/mouvements")
public class MouvementController
{
    private final MouvementRepository mvtRepo;
    @GetMapping(path = "/affaire/{affId}")
    List<MouvementListResp> findByAffaire(@PathVariable Long affId)
    {
        return mvtRepo.findByAffaire(affId);
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
}
