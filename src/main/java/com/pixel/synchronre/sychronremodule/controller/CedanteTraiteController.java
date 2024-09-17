package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
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
    public Page<CedanteTraiteResp> search(@RequestParam Long traiNpId,
                                          @RequestParam(defaultValue = "") String key,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size)
    {
        return cedanteTraiteService.search(traiNpId, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/edit")
    public CedanteTraiteReq getEditDto(
            @RequestParam(required = false) Long cedanteTraiteId,
            @RequestParam(required = false) Long traiteNpId,
            @RequestParam(required = false) Long cedId)
    {
        return cedanteTraiteService.getEditDto(cedanteTraiteId,traiteNpId, cedId);
    }

    @PostMapping(path = "/edit")
    public CedanteTraiteReq getEditDto(@RequestBody CedanteTraiteReq dto)
    {
        return cedanteTraiteService.getEditDto(dto);
    }

    @GetMapping(path = "/list/{traiteNpId}")
    List <CedanteTraiteResp> list(@PathVariable Long traiteNpId){
        return cedanteTraiteService.getCedanteTraitelist(traiteNpId);
    }

    @GetMapping(path = "/a-saisir/{traiteNpId}")
    List <ReadCedanteDTO> getListCedanteAsaisir(@PathVariable Long traiteNpId){
        return cedanteTraiteService.getListCedanteAsaisirSurTraite(traiteNpId);
    }

}
