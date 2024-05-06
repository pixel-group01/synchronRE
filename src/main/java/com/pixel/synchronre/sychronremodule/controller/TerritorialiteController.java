package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.territorialite.CreateTerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.UpdateTerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.CreateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
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
    TerritorialiteResp create(@Valid @RequestBody CreateTerritorialiteReq dto) throws UnknownHostException {
        return territorialiteService.create(dto);
    }

    @PutMapping(path = "/update")
    TerritorialiteResp update(@Valid @RequestBody UpdateTerritorialiteReq dto) throws UnknownHostException {
        return territorialiteService.update(dto);
    }

    @GetMapping(path = "/search")
    Page<TerritorialiteResp> create(@RequestParam(defaultValue = "") String key,
                              @RequestParam(required = false) Long fncId,
                              @RequestParam(required = false) Long userId,
                              @RequestParam(required = false) Long cedId,
                              @RequestParam(required = false) List<String> staCodes, Long exeCode,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size)
    {
        return territorialiteService.search(key, fncId, userId, cedId, staCodes, exeCode, PageRequest.of(page, size));
    }

}
