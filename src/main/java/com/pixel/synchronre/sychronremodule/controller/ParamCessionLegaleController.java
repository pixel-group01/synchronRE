package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.CreateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.UpdateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceParamCessionLegale;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
@RequestMapping(path = "/paramsCession")
public class ParamCessionLegaleController
{
    private final IserviceParamCessionLegale paramCessionService;
    private final TypeRepo typeRepo;

    @PostMapping(path = "/create")
    public ParamCessionLegaleDetailsResp createParamCession(@RequestBody @Valid CreateParamCessionLegaleReq dto) throws UnknownHostException {
        return paramCessionService.createParamCessionLegale(dto);
    }

    @PutMapping(path = "/update")
    public ParamCessionLegaleDetailsResp updateParamCession(@RequestBody @Valid UpdateParamCessionLegaleReq dto) throws UnknownHostException {
        return paramCessionService.updateParamCessionLegale(dto);
    }

    @GetMapping(path = "/list")
    public Page<ParamCessionLegaleListResp> searchParamsCessions(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return paramCessionService.searchParamCessionLegale(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/types")
    public List<ReadTypeDTO> getTypeCessionsLegales(){
        return typeRepo.findByTypeGroup(TypeGroup.TYPE_PCL);
    }
}