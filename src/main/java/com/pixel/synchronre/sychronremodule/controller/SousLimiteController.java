package com.pixel.synchronre.sychronremodule.controller;


import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.CreateSousLimiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp;
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
                              @RequestParam(required = false) Long fncId,
                              @RequestParam(required = false) Long userId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size)
    {
        return iServiceSousLimite.search(key, fncId, userId, PageRequest.of(page, size));
    }

}
