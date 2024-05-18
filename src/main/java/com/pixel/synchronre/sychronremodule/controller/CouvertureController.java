package com.pixel.synchronre.sychronremodule.controller;


import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.CreateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.UpdateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.CreateCouvertureReq;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.UpdateCouvertureReq;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureListResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCouverture;
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
@RequestMapping("/couvertures")
public class CouvertureController {

    private final IserviceCouverture couvertureService;

    @PostMapping(path = "/create")
    public CouvertureDetailsResp createCouverture(@RequestBody @Valid CreateCouvertureReq dto) throws UnknownHostException {
        return couvertureService.createCouverture(dto);
    }

    @PutMapping(path = "/update")
    public CouvertureDetailsResp updateCouverture(@RequestBody @Valid UpdateCouvertureReq dto) throws UnknownHostException {
        return couvertureService.updateCouverture(dto);
    }

    @GetMapping(path = "/list")
    public Page<CouvertureListResp> searchCouvertures(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size) {
        return couvertureService.searchCouverture(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/parents")
    public List<CouvertureListResp> getCouerturesParents() {
        return couvertureService.getCouerturesParents();
    }

    @GetMapping(path = "/filles/{couParentId}")
    List<CouvertureListResp>  filles(@PathVariable Long couParentId){
        return couvertureService.getCouerturesFilles(couParentId);

    }
}
