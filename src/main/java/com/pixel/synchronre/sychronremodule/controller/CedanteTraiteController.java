package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCedanteTraite;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/traite/cedantes")
public class CedanteTraiteController
{
    private final IServiceCedanteTraite cedanteTraiteService;

    @PostMapping(path = "/save")
    public CedanteTraiteResp save(@RequestBody CedanteTraiteReq dto)
    {
        return cedanteTraiteService.save(dto);
    }

    @DeleteMapping(path = "/delete/{cedanteTraiteId}")
    public void delete(@PathVariable Long cedanteTraiteId)
    {
        cedanteTraiteService.removeCedanteOnTraite(cedanteTraiteId);
    }

    @GetMapping(path = "/search")
    public Page<CedanteTraiteResp> search(@RequestParam Long traiId,
                                          @RequestParam(defaultValue = "") String key,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size)
    {
        return cedanteTraiteService.search(traiId, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/list/{traiteNpId}")
    List <CedanteTraiteResp> list(@PathVariable Long traiteNpId){
        return cedanteTraiteService.getCedanteTraitelist(traiteNpId);
    }

}
