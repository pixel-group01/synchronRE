package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.UpdateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceSinistre;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController @RequiredArgsConstructor
@RequestMapping(path ="/{sinistre}")
public class SinistreController
{
    private final IServiceSinistre sinService;

    @PostMapping(path = "/create")
    public SinistreDetailsResp createReglement(@RequestBody @Valid CreateSinistreReq dto) throws UnknownHostException {
        return sinService.createSinistre(dto);
    }

    @PutMapping(path = "/update")
    public SinistreDetailsResp updateReglement(@RequestBody @Valid UpdateSinistreReq dto) throws UnknownHostException {
        return sinService.updateSinistre(dto);
    }

    @GetMapping(path = "/list")
    public Page<SinistreDetailsResp> searchReglement(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return sinService.searchSinistre(key,PageRequest.of(page, size));
    }
}
