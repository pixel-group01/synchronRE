package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.CreateCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.ReadCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.UpdateCedenteDTO;
import com.pixel.synchronre.sychronremodule.service.interfac.ICedenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/cedentes")
public class CedenteController 
{
    private final ICedenteService cedService;
    private final CedRepo cedRepo;
    
    @PostMapping(path = "/create")
    public ReadCedenteDTO createCedente(@RequestBody CreateCedenteDTO dto) throws UnknownHostException {
        return cedService.createCedente(dto);
    }

    @PutMapping(path = "/update")
    public ReadCedenteDTO updateCedente(@RequestBody UpdateCedenteDTO dto) throws UnknownHostException {
        return cedService.updateCedente(dto);
    }

    @GetMapping(path = "/list")
    public Page<ReadCedenteDTO> searchCedente(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return cedService.searchCedente(key, PageRequest.of(page, size));
    }
}
