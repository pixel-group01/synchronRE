package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.banque.request.CreateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.UpdateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceBanque;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController @ResponseStatus(HttpStatus.OK)
@RequestMapping("/banques")
@RequiredArgsConstructor

public class BanqueController {

    private final IserviceBanque banqueService;


    @PostMapping(path = "/create")
    public BanqueDetailsResp createBanque(@RequestBody @Valid CreateBanqueReq dto) throws UnknownHostException {
        return banqueService.createBanque(dto);
    }


    @PutMapping(path = "/update")
    public BanqueDetailsResp updateBanque(@RequestBody @Valid UpdateBanqueReq dto) throws UnknownHostException {
        return banqueService.updateBanque(dto);
    }


    @GetMapping(path = "/list")
    public Page<BanqueListResp> searchBanques(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size) throws UnknownHostException {
        return banqueService.searchBanque(key, PageRequest.of(page, size));
    }

}
