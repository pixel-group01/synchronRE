package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.CreateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.UpdateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.CreateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.UpdateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.ICedanteService;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceParamCessionLegale;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
@RequestMapping(path = "/paramsCession")
public class ParamCessionLegaleController
{
    private final IserviceParamCessionLegale paramCessionService;

    @PostMapping(path = "/create")
    public ParamCessionLegaleDetailsResp createParamCession(@RequestBody @Valid CreateParamCessionLegaleReq dto) throws UnknownHostException {
        return paramCessionService.createParamCessionLegale(dto);
    }

    @PutMapping(path = "/update")
    public ParamCessionLegaleDetailsResp updateParamCession(@RequestBody @Valid UpdateParamCessionLegaleReq dto) throws UnknownHostException {
        return paramCessionService.updateParamCessionLegale(dto);
    }

    @GetMapping(path = "/list")
    public Page<ParamCessionLegaleListResp> searchParamsCessions(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return paramCessionService.searchParamCessionLegale(key, PageRequest.of(page, size));
    }
}
