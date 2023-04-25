package com.pixel.synchronre.sychronremodule.controller;



import com.pixel.synchronre.sychronremodule.model.dto.pays.request.CreatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.UpdatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IservicePays;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pays")
public class PaysController {

    private final IservicePays paysService;

    @PostMapping(path = "/create")
    public PaysDetailsResp createPays(@RequestBody @Valid CreatePaysReq dto) throws UnknownHostException {
        return paysService.createPays(dto);
    }

    @PutMapping(path = "/update")
    public PaysDetailsResp updatePays(@RequestBody @Valid UpdatePaysReq dto) throws UnknownHostException {
        return paysService.updatePays(dto);
    }

    @GetMapping(path = "/list")
    public Page<PaysListResp> searchPays(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return paysService.searchPays(key, PageRequest.of(page, size));
    }
}
