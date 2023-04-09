package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.CreateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.UpdateCedanteDTO;
import com.pixel.synchronre.sychronremodule.service.interfac.ICedanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/cedentes")
public class CedanteController
{
    private final ICedanteService cedService;
    private final CedRepo cedRepo;
    
    @PostMapping(path = "/create")
    public ReadCedanteDTO createCedente(@RequestBody CreateCedanteDTO dto) throws UnknownHostException {
        return cedService.createCedente(dto);
    }

    @PutMapping(path = "/update")
    public ReadCedanteDTO updateCedente(@RequestBody UpdateCedanteDTO dto) throws UnknownHostException {
        return cedService.updateCedente(dto);
    }

    @GetMapping(path = "/list")
    public Page<ReadCedanteDTO> searchCedente(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return cedService.searchCedente(key, PageRequest.of(page, size));
    }
}
