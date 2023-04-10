package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.MouvementRepository;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.MouvementListResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/mouvements")
public class MouvementController
{
    private final MouvementRepository mvtRepo;
    @GetMapping(path = "/affaire/{affId}")
    List<MouvementListResp> findByAffaire(@PathVariable Long affId)
    {
        return mvtRepo.findByAffaire(affId);
    }
}
