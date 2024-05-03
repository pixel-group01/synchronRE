package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.enums.EnumDTO;
import com.pixel.synchronre.sychronremodule.model.entities.EXERCICE_RATTACHEMENT;
import com.pixel.synchronre.sychronremodule.model.entities.PERIODICITE;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enums")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class EnumController {

    @GetMapping("/periodicites")
    List<EnumDTO> getPeriodicites()
    {
        List<EnumDTO> periodicites = EnumUtils.getEnumList(PERIODICITE.class)
                .stream().map(p->new EnumDTO(p.getCode(),p.getLibelle()))
                .collect(Collectors.toList());
        return periodicites;
    }

    @GetMapping("/exercice-rattachement")
    List<EnumDTO> getExerciceRattachement()
    {
        List<EnumDTO> exoRattachements = EnumUtils.getEnumList(EXERCICE_RATTACHEMENT.class)
                .stream().map(p->new EnumDTO(p.getCode(),p.getLibelle()))
                .collect(Collectors.toList());
        return exoRattachements;
    }
}
