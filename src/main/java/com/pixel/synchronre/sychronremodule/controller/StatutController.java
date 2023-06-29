package com.pixel.synchronre.sychronremodule.controller;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.UpdateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.StatutIservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;


@RestController @ResponseStatus(HttpStatus.OK)
@RequestMapping("/statuts")
@RequiredArgsConstructor
public class StatutController {
    private final StatutIservice statutService;
    
    @PostMapping(path = "/create")
    public StatutDetailsResp createStatut(@RequestBody @Valid CreateStatutReq dto) throws UnknownHostException {
        return statutService.createStatut(dto);
    }

    @PutMapping(path = "/update")
    public StatutDetailsResp updateStatut(@RequestBody @Valid UpdateStatutReq dto) throws UnknownHostException {
        return statutService.updateStatut(dto);
    }

    @GetMapping(path = "/list")
    public Page<StatutListResp> searchStatuts(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return statutService.searchStatut(key, PageRequest.of(page, size));
    }

}
