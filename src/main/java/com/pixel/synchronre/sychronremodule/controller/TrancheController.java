package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTranche;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/tranches")
public class TrancheController
{
    private final IServiceTranche trancheService;

    @PostMapping(path = "/save")
    TrancheResp create(@Valid @RequestBody TrancheReq dto){
        return trancheService.save(dto);
    }

    @DeleteMapping(path = "/delete/{trancheId}")
    boolean delete(@PathVariable Long trancheId) {
        return trancheService.delete(trancheId);
    }

    @GetMapping(path = "/search")
    Page<TrancheResp> create(@RequestParam(required = false) Long traiId,
                               @RequestParam(defaultValue = "") String key,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size)
    {
        return trancheService.search(traiId, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/edit/{trancheId}")
    TrancheReq edit(@PathVariable Long trancheId){
        return trancheService.edit(trancheId);
    }

    @GetMapping(path = "/list/{traiteNpId}")
    List<TrancheResp> getTrancheList(@PathVariable Long traiteNpId)
    {
        return trancheService.getTrancheList(traiteNpId);
    }
}
