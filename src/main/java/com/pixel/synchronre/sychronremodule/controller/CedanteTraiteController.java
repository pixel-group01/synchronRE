package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCedanteTraite;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/traite/cedantes")
public class CedanteTraiteController
{
    private final IServiceCedanteTraite cedanteTraiteService;

    @GetMapping(path = "/save")
    void save(CedanteTraiteReq dto)
    {
        cedanteTraiteService.save(dto);
    }
}
