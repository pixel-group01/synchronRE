package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCategorie;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceTerritorialite;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategorieController
{
    private final IServiceCategorie categorieService;

    @PostMapping(path = "/save")
    CategorieResp create(@Valid @RequestBody CategorieReq dto) throws UnknownHostException {
        return categorieService.save(dto);
    }

    @DeleteMapping(path = "/delete/{categorieId}")
    boolean update(@PathVariable Long categorieId) throws UnknownHostException {
        return categorieService.delete(categorieId);
    }

    @GetMapping(path = "/search")
    Page<CategorieResp> create(@RequestParam(required = false) Long traiId,
                                    @RequestParam(defaultValue = "") String key,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size)
    {
        return categorieService.search(traiId, key, PageRequest.of(page, size));
    }

}
