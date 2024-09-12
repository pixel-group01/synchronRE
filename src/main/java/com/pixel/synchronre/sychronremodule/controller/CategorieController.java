package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCategorie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategorieController
{
    private final IServiceCategorie categorieService;

    @PostMapping(path = "/save")
    CategorieResp create(@Valid @RequestBody CategorieReq dto) {
        return categorieService.save(dto);
    }

    @DeleteMapping(path = "/delete/{categorieId}")
    boolean delete(@PathVariable Long categorieId) {
        return categorieService.delete(categorieId);
    }

    @GetMapping(path = "/search")
    Page<CategorieResp> search(@RequestParam(required = false) Long traiteNpId,
                                    @RequestParam(defaultValue = "") String key,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size)
    {
        return categorieService.search(traiteNpId, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/list/{traiteNpId}")
    List<CategorieResp> getCategorieList(@PathVariable Long traiteNpId)
    {
        return categorieService.getCategorieList(traiteNpId);
    }
}
