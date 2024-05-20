package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionTraiteNPResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRepartitionTraiteNP;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/repartitions/traite-non-proportionnel")
@Validated
@ResponseStatus(HttpStatus.OK)
public class RepartitionTraiteNPController
{
    private final IServiceRepartitionTraiteNP repartitionTraiteNPService;
    @PostMapping(path = "/save")
    public RepartitionTraiteNPResp save(@Valid @RequestBody PlacementTraiteNPReq dto)
    {
        return repartitionTraiteNPService.save(dto);
    }

    @GetMapping(path = "/search")
    public Page<RepartitionTraiteNPResp> save(@RequestParam(name = "traiteNPId") Long traiteNPId,
                                              @RequestParam(name = "key", defaultValue = "")String key,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "10") int size)
    {
        return repartitionTraiteNPService.search(traiteNPId, key, PageRequest.of(page, size));
    }
}
