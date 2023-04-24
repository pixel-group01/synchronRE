package com.pixel.synchronre.sychronremodule.controller;


import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.CreateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.UpdateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceParamCessionLegale;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paramsCession")
public class ParamCessionLegaleController {

     private final IserviceParamCessionLegale paramCessionService;
     private final ParamCessionLegaleRepository pclRepo;

    @PostMapping(path = "/create")
    public ParamCessionLegaleDetailsResp createParamCession(@RequestBody @Valid CreateParamCessionLegaleReq dto) throws UnknownHostException {
        return paramCessionService.createParamCessionLegale(dto);
    }

    @PutMapping(path = "/update")
    public ParamCessionLegaleDetailsResp updateParamCession(@RequestBody @Valid UpdateParamCessionLegaleReq dto) throws UnknownHostException {
        return paramCessionService.updateParamCessionLegale(dto);
    }

    @GetMapping(path = "/list-by-affaire/{affId}")
    public List<ParamCessionLegaleListResp> searchParamsCessions(@PathVariable Long affId) throws UnknownHostException {
        return pclRepo.findByAffId(affId);
    }

   /* @GetMapping(path = "/list-by-pays/{payCode}")
    public List<ParamCessionLegaleListResp> searchParamsCessionsByPays(@PathVariable String payCode) throws UnknownHostException {
        return pclRepo.findByAffId(affId);
    }*/

}
