package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.compte.CompteTraiteDto;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.entities.Periode;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceCompte;
import com.pixel.synchronre.typemodule.controller.services.ITypeService;
import com.pixel.synchronre.typemodule.model.dtos.ReadTypeDTO;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.xml.xpath.XPath;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/comptes")
public class CompteController {
    private final IserviceCompte compteService;
    private final ITypeService typeService;

    @GetMapping(path = "/traites/{traiteNpId}")
    CompteTraiteDto getCompteTraite(@PathVariable Long traiteNpId){
        return compteService.getCompteTraite(traiteNpId);
    }

    @PostMapping(path = "/traites/save")
    CompteTraiteDto saveCompteTraite(@RequestBody CompteTraiteDto dto){
        return compteService.save(dto);
    }

    @GetMapping(path = "/periodicites")
    public List<ReadTypeDTO> getPeriodicites()
    {
        return typeService.getTypesByGroup(TypeGroup.PERIODICITE);
    }

    @GetMapping(path = "/periodes")
    public List<Periode> getPeriodes(@RequestParam(name = "exeCode") Long exeCode, @RequestParam(name = "typeId") Long typeId)
    {
        return compteService.getPeriode(exeCode, typeId);
    }
}
