package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionReq;
import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionResp;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceLimiteSouscription;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTranche;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/limitesouscription")
public class LimiteSouscriptionController
{
    private final IServiceLimiteSouscription limiteSouscriptionService;

    @PostMapping(path = "/save")
    LimiteSouscriptionResp create(@Valid @RequestBody LimiteSouscriptionReq dto){
        return limiteSouscriptionService.save(dto);
    }

    @DeleteMapping(path = "/delete/{limiteSouscriptionId}")
    boolean delete(@PathVariable Long limiteSouscriptionId) {
        return limiteSouscriptionService.delete(limiteSouscriptionId);
    }

    @GetMapping(path = "/search")
    Page<LimiteSouscriptionResp> create(@RequestParam(required = false) Long traiId,
                               @RequestParam(defaultValue = "") String key,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size)
    {
        return limiteSouscriptionService.search(traiId, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/edit/{limiteSouscriptionId}")
    LimiteSouscriptionReq edit(@PathVariable Long limiteSouscriptionId){
        return limiteSouscriptionService.edit(limiteSouscriptionId);
    }

}
