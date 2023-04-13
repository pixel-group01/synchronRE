package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.enums.TypeReglement;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/reglements")
public class RegelementController {
    private final IserviceReglement regService;

    @PostMapping(path = "/{typeReg}/create")
    public ReglementDetailsResp createReglement(@PathVariable String typeReg, @RequestBody @Valid CreateReglementReq dto) throws UnknownHostException {
        return regService.createReglement(TypeReglement.valueOf(typeReg),dto);
    }

    @PutMapping(path = "/{typeReg}/update")
    public ReglementDetailsResp updateReglement(@RequestBody @Valid UpdateReglementReq dto) throws UnknownHostException {
        return regService.updateReglement(dto);
    }

    @GetMapping(path = "/{typeReg}/list")
    public Page<ReglementListResp> searchReglement(@RequestParam(defaultValue = "") String key, @PathVariable String typeReg, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return regService.searchReglement(key, TypeReglement.valueOf(typeReg), PageRequest.of(page, size));
    }

}
