package com.pixel.synchronre.sychronremodule.controller;


import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.CreateSousLimiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSousLimite;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/sous-limite")
public class SousLimiteController {

    private final IServiceSousLimite iServiceSousLimite;

    @PostMapping(path = "/create")
    SousLimiteDetailsResp create(@Valid @RequestBody CreateSousLimiteReq dto) throws UnknownHostException {
        return iServiceSousLimite.create(dto);
    }

    @PutMapping(path = "/update")
    SousLimiteDetailsResp update(@Valid @RequestBody UpdateSousLimite dto) throws UnknownHostException {
        return iServiceSousLimite.update(dto);
    }

    @GetMapping(path = "/search")
    Page<SousLimiteDetailsResp> create(@RequestParam(defaultValue = "") String key,
                              @RequestParam(required = true) Long traiteNpId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size)
    {
        return iServiceSousLimite.search(key, traiteNpId, PageRequest.of(page, size));
    }

    @GetMapping(path = "/edit/{sousLimiteSouscriptionId}")
    UpdateSousLimite edit(@PathVariable Long sousLimiteSouscriptionId){
        return iServiceSousLimite.edit(sousLimiteSouscriptionId);
    }

    @DeleteMapping(path = "/delete/{sousLimiteSouscriptionId}")
    public void delete(@PathVariable Long sousLimiteSouscriptionId)
    {
        iServiceSousLimite.delete(sousLimiteSouscriptionId);
    }

}
