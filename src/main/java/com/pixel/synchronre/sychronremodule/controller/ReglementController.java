package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.archivemodule.model.dtos.validator.OnRegUpload;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
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

    @PostMapping(path = "/create") @Validated({OnRegUpload.class})
    public ReglementDetailsResp createReglement(@PathVariable String typeReg, @RequestBody @Valid CreateReglementReq dto) throws UnknownHostException {
        return regService.createReglement(typeReg,dto);
    }

    @PutMapping(path = "/update")
    public ReglementDetailsResp updateReglement(@RequestBody @Valid UpdateReglementReq dto) throws UnknownHostException {
        return regService.updateReglement(dto);
    }

    @GetMapping(path = "/list/{affId}")
    public Page<ReglementListResp> searchReglement(@RequestParam(defaultValue = "") String key,
                                                   @PathVariable Long affId,
                                                   @PathVariable String typeReg,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) throws UnknownHostException
    {
        return regService.searchReglement(key, affId,typeReg, PageRequest.of(page, size));
    }

    @GetMapping(path = "/type-documents")
    public List<ReadTypeDTO> getTypeDocumentReglement(@RequestParam(defaultValue = "") String key, @PathVariable String typeReg, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException {
        Type typeDocReg = typeRepo.findByUniqueCode("DOC_REG");
        if(typeDocReg == null) return new ArrayList<>();
        return typeRepo.findSousTypeOf(typeDocReg.getTypeId());
    }
}
