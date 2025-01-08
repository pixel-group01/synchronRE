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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @ResponseStatus(HttpStatus.OK)
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
    public Page<PaysListResp> searchPays(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1000") int size) throws UnknownHostException {
        return paysService.searchPays(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/organisations")
    public List<PaysListResp> getPaysByOrgCode(@RequestParam(required =  false) List<String> orgCodes) {
        return paysService.getPaysByOrgCodes(orgCodes);
    }
}
