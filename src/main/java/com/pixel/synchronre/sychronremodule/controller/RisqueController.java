package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.association.response.ActivitesResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.CreateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.UpdateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceRisque;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/risques")
public class RisqueController
{
    private final IServiceRisque risqueService;

    @PostMapping(path = "/create")
    RisqueCouvertResp create(@Valid @RequestBody CreateRisqueCouvertReq dto) throws UnknownHostException {
        return risqueService.create(dto);
    }

    @PutMapping(path = "/update")
    RisqueCouvertResp update(@Valid @RequestBody UpdateRisqueCouvertReq dto) throws UnknownHostException {
        return risqueService.update(dto);
    }

    @DeleteMapping(path = "/delete/{risqueId}")
    boolean delete(@PathVariable Long risqueId) throws UnknownHostException {
        return risqueService.delete(risqueId);
    }

    @GetMapping(path = "/search")
    Page<RisqueCouvertResp> search(@RequestParam(required = false) Long traiteNpId,
                                    @RequestParam(defaultValue = "") String key,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size)
    {
        return risqueService.search(traiteNpId, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/edit/{risqueId}")
    UpdateRisqueCouvertReq getEditDto(@PathVariable(required = true) Long risqueId)
    {
        return risqueService.getEditDto(risqueId);
    }

    @GetMapping(path = "/list/{traiteNpId}")
    List<RisqueCouvertResp> getRisqueList(@PathVariable Long traiteNpId)
    {
        return risqueService.getRisqueList(traiteNpId);
    }

    @GetMapping(path = "/couvertureParent/{traiteNpId}")
    List<RisqueCouvertResp> getCouvertureParent(@PathVariable Long traiteNpId)
    {
        return risqueService.getCouvertureParent(traiteNpId);
    }

    @GetMapping(path = "/activites/{risqueId}")
    List<ActivitesResp> getActivites(@PathVariable Long risqueId)
    {
        return risqueService.getActivites(risqueId);
    }
}
