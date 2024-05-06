package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTerritorialite;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/territorialites")
public class TerritorialiteController
{
    private final IServiceTerritorialite territorialiteService;

    @PostMapping(path = "/create")
    TerritorialiteResp create(@Valid @RequestBody TerritorialiteReq dto) throws UnknownHostException {
        return territorialiteService.create(dto);
    }

    @PutMapping(path = "/update")
    TerritorialiteResp update(@Valid @RequestBody TerritorialiteReq dto) throws UnknownHostException {
        return territorialiteService.update(dto);
    }

    @GetMapping(path = "/search")
    Page<TerritorialiteResp> create(@RequestParam(defaultValue = "") String key,
                              @RequestParam(required = false) Long traiId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size)
    {
        return null;
    }

}
