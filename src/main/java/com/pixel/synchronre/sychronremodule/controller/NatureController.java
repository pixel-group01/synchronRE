package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.nature.response.NatureListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Nature;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceNature;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/natures")
@RequiredArgsConstructor
public class NatureController
{
    private final IserviceNature natureService;

    @GetMapping(path = "/liste")
    public List<NatureListResp> getListeNatures(@RequestParam(defaultValue = "") String forme)
    {
        return natureService.getListeNatures(forme);
    }
}
