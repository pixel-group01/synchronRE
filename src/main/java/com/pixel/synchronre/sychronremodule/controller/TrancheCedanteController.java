package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.TrancheCedanteReq;
import com.pixel.synchronre.sychronremodule.model.views.CedanteTraite;
import com.pixel.synchronre.sychronremodule.service.interfac.ITrancheCedanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/tranches/cedantes")
public class TrancheCedanteController
{
    private final ITrancheCedanteService trancheCedanteService;

    @PostMapping(path = "/edit")
    public TrancheCedanteReq getEditDto(@RequestBody TrancheCedanteReq dto)
    {
        return trancheCedanteService.getEditDto(dto, 0);
    }

    @PostMapping(path = "/save")
    public TrancheCedanteReq save(@RequestBody TrancheCedanteReq dto)
    {
        return trancheCedanteService.save(dto);
    }

    @GetMapping(path = "/traites")
    public List<CedanteTraite> getAllCedanteTraite()
    {
        List<CedanteTraite> cedanteTraites = trancheCedanteService.getAllCedanteTraites();
        return cedanteTraites;
    }
}