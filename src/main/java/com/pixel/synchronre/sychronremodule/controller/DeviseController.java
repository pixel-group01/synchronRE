package com.pixel.synchronre.sychronremodule.controller;


import com.pixel.synchronre.sychronremodule.model.dto.banque.request.CreateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.UpdateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueListResp;
import com.pixel.synchronre.sychronremodule.model.dto.devise.request.CreateDeviseReq;
import com.pixel.synchronre.sychronremodule.model.dto.devise.request.UpdateDeviseReq;
import com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceDevise;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @ResponseStatus(HttpStatus.OK)
@RequestMapping("/devises")
@RequiredArgsConstructor
public class DeviseController {

    private final IServiceDevise deviseService;


    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public DeviseDetailsResp createDevise(@RequestBody @Valid CreateDeviseReq dto) throws UnknownHostException {
        return deviseService.createDevise(dto);
    }


    @PutMapping(path = "/update")
    @ResponseStatus()
    public DeviseDetailsResp updateDevise(@RequestBody @Valid UpdateDeviseReq dto) throws UnknownHostException {
        return deviseService.updateDevise(dto);
    }


    @GetMapping(path = "/list")
    public List<DeviseListResp> searchDevises(@RequestParam(defaultValue = "") String key) throws UnknownHostException {
        return deviseService.searchDevise(key);
    }
}