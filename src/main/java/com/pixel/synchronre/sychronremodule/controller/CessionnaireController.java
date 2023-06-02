package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.CreateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.UpdateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCessionnaire;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.xml.xpath.XPath;
import java.net.UnknownHostException;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping("/cessionnaires")
public class CessionnaireController
{
    private final IserviceCessionnaire cessionnaireService;
    private final CessionnaireRepository cessRepo;

    @PostMapping(path = "/create")
    public CessionnaireDetailsResp createCessionnaire(@RequestBody @Valid CreateCessionnaireReq dto) throws UnknownHostException {
        return cessionnaireService.createCessionnaire(dto);
    }

    @PutMapping(path = "/update")
    public CessionnaireDetailsResp updateCessionnaire(@RequestBody @Valid UpdateCessionnaireReq dto) throws UnknownHostException {
        return cessionnaireService.updateCessionnaire(dto);
    }

    @GetMapping(path = "/list")
    public Page<CessionnaireListResp> searchCessionnaires(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        return cessionnaireService.searchCessionnaire(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/by-affaire/{affId}")
    public List<CessionnaireListResp> findCessionnairesByAffaire(Long affId)
    {
        return cessRepo.findByAffId(affId);
    }
}
