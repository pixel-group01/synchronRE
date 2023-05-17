package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.RepartitionRepository;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.EtatComptableAffaire;
import com.pixel.synchronre.sychronremodule.model.dto.mapper.FacultativeMapper;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.REG_AFF_GROUP;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.REG_SIN_GROUP;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.UpdateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/{typeReg}")
@ResponseStatus(HttpStatus.OK)
public class ReglementController
{
    private final IserviceReglement regService;
    private final TypeRepo typeRepo;
    private final RepartitionRepository repRepo;
    private final FacultativeMapper affMapper;

    @PostMapping(path = "/affaire/create") @Validated({REG_AFF_GROUP.class})
    public ReglementDetailsResp createReglementAffaire(@PathVariable String typeReg, @RequestBody @Valid CreateReglementReq dto) throws UnknownHostException {
        return regService.createReglementAffaire(typeReg,dto);
    }

    @PutMapping(path = "/affaire/update") @Validated({REG_AFF_GROUP.class})
    public ReglementDetailsResp updateReglementAffaire(@RequestBody @Valid UpdateReglementReq dto) throws UnknownHostException {
        return regService.updateReglement(dto);
    }

    @PostMapping(path = "/sinistre/create") @Validated({REG_SIN_GROUP.class})
    public ReglementDetailsResp createReglementSinistre(@PathVariable String typeReg, @RequestPart @Valid CreateReglementReq dto) throws UnknownHostException {
        return regService.createReglementSinistre(typeReg,dto);
    }

    @PutMapping(path = "/sinistre/update") @Validated({REG_SIN_GROUP.class})
    public ReglementDetailsResp updateReglementSinistre(@RequestBody @Valid UpdateReglementReq dto) throws UnknownHostException {
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

    @GetMapping(path = "/sinistre/list/{affId}")
    public Page<ReglementListResp> searchReglementSinistre(@RequestParam(defaultValue = "") String key,
                                                   @PathVariable Long sinId,
                                                   @PathVariable String typeReg,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) throws UnknownHostException
    {
        return regService.searchReglement(key, null,sinId,typeReg, PageRequest.of(page, size));
    }

    @GetMapping(path = "/type-documents")
    public List<ReadTypeDTO> getTypeDocumentReglement(@RequestParam(defaultValue = "") String key, @PathVariable String typeReg, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        Type typeDocReg = typeRepo.findByUniqueCode("DOC_REG");
        if(typeDocReg == null) return new ArrayList<>();
        return typeRepo.findSousTypeOf(typeDocReg.getTypeId());
    }

    @GetMapping(path ="/details")
    public EtatComptableAffaire.DetailsEtatComptable getDetailCession(@RequestParam Long affId, @RequestParam Long cesId)
    {
        Long plaId = repRepo.getPlacementIdByAffIdAndCesId(affId, cesId).orElseThrow(()->new AppException("Placement introuvable"));
        return affMapper.getDetailsEtatComptable(plaId);
    }
}
