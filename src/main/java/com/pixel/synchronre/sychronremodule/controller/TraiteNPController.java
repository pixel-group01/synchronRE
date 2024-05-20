package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.CreateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTraiteNP;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/traite-non-proportionnel")
public class TraiteNPController
{
    private final IServiceTraiteNP traiteNPService;

    @PostMapping(path = "/create")
    TraiteNPResp create(@Valid @RequestBody CreateTraiteNPReq dto) throws UnknownHostException {
        return traiteNPService.create(dto);
    }

    @PutMapping(path = "/update")
    TraiteNPResp update(@Valid @RequestBody UpdateTraiteNPReq dto) throws UnknownHostException {
        return traiteNPService.update(dto);
    }

    @GetMapping(path = "/search")
    Page<TraiteNPResp> create(@RequestParam(defaultValue = "") String key,
                              @RequestParam(required = false) Long fncId,
                              @RequestParam(required = false) Long userId,
                              @RequestParam(required = false) Long cedId,
                              @RequestParam(required = false) List<String> staCodes, Long exeCode,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size)
    {
        return traiteNPService.search(key, fncId, userId, cedId, staCodes, exeCode, PageRequest.of(page, size));
    }

    @GetMapping(path = "/edit/{traiId}")
    UpdateTraiteNPReq edit(@PathVariable Long traiId){
        return traiteNPService.edit(traiId);
    }

}
