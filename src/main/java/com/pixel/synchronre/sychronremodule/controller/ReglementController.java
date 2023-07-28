package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.SinMapper;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.REG_AFF_GROUP;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.REG_SIN_GROUP;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.EtatComptableSinistreResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController @Validated
@RequiredArgsConstructor
@RequestMapping(path ="/{typeReg}")
@ResponseStatus(HttpStatus.OK)
public class ReglementController
{
    private final IserviceReglement regService;
    private final TypeRepo typeRepo;
    private final RepartitionRepository repRepo;
    private final FacultativeMapper affMapper;
    private final SinMapper sinMapper;

    @PostMapping(path = "/affaire/create")
    public ReglementDetailsResp createReglementAffaire(@PathVariable String typeReg, @RequestBody @Validated({REG_AFF_GROUP.class}) CreateReglementReq dto) throws UnknownHostException {
        return regService.createReglementAffaire(typeReg,dto);
    }

    @PutMapping(path = "/affaire/update")
    public ReglementDetailsResp updateReglementAffaire(@RequestBody @Validated({REG_AFF_GROUP.class}) UpdateReglementReq dto) throws UnknownHostException {
        return regService.updateReglement(dto);
    }

    @PostMapping(path = "/sinistre/create")
    public ReglementDetailsResp createReglementSinistre(@PathVariable String typeReg, @RequestBody @Validated({REG_SIN_GROUP.class}) CreateReglementReq dto) throws UnknownHostException {
        return regService.createReglementSinistre(typeReg,dto);
    }

    @PutMapping(path = "/sinistre/update")
    public ReglementDetailsResp updateReglementSinistre(@RequestBody @Validated({REG_SIN_GROUP.class}) UpdateReglementReq dto) throws UnknownHostException {
        return regService.updateReglement(dto);
    }

    @GetMapping(path = "/affaire/list/{affId}")
    public Page<ReglementListResp> searchReglementAffaire(@RequestParam(defaultValue = "") String key,
                                                   @PathVariable Long affId,
                                                   @PathVariable String typeReg,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) throws UnknownHostException
    {
        return regService.searchReglement(key, affId, null,typeReg, PageRequest.of(page, size));
    }

    @GetMapping(path = "/sinistre/list/{sinId}")
    public Page<ReglementListResp> searchReglementSinistre(@RequestParam(defaultValue = "") String key,
                                                   @PathVariable Long sinId,
                                                   @PathVariable String typeReg,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) throws UnknownHostException
    {
        return regService.searchReglement(key, null,sinId,typeReg, PageRequest.of(page, size));
    }

    @GetMapping(path ="/affaire/details")
    public EtatComptableAffaire.DetailsEtatComptable getDetailEtatComptableAffaire(@RequestParam Long affId, @RequestParam Long cesId)
    {
        Long plaId = repRepo.getPlacementIdByAffIdAndCesId(affId, cesId).orElseThrow(()->new AppException("Placement introuvable"));
        return affMapper.getDetailsEtatComptable(plaId);
    }

    @GetMapping(path ="/sinistre/details")
    public EtatComptableSinistreResp.DetailsEtatComptableSinistre getDetailEtatComptableSinistre(@RequestParam Long sinId, @RequestParam Long cesId)
    {
        return sinMapper. getDetailsEtatComptableSinistre(sinId, cesId);
    }
}