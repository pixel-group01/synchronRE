package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionReq;
import com.pixel.synchronre.sychronremodule.model.dto.reconstitution.ReconstitutionResp;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTranche;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReconstitution;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/reconstitutions")
public class ReconstitutionController
{
    private final IserviceReconstitution reconstitutionService;

    @PostMapping(path = "/save")
    ReconstitutionResp create(@Valid @RequestBody ReconstitutionReq dto){
        return reconstitutionService.save(dto);
    }

    @DeleteMapping(path = "/delete/{reconstitutionId}")
    boolean delete(@PathVariable Long reconstitutionId) {
        return reconstitutionService.delete(reconstitutionId);
    }

    @GetMapping(path = "/search")
    Page<ReconstitutionResp> create(@RequestParam(required = false) Long traiteNPId,
                               @RequestParam(defaultValue = "") String key,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size)
    {
        return reconstitutionService.search(traiteNPId, key, PageRequest.of(page, size));
    }
}
