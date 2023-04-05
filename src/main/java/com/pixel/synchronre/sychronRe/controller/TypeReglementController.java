package com.pixel.synchronre.sychronRe.controller;


import com.pixel.synchronre.sychronRe.model.dto.request.TypeReglementReqDTO;
import com.pixel.synchronre.sychronRe.model.entities.TypeReglement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/typReglement")
@RequiredArgsConstructor
public class TypeReglementController {


    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public TypeReglement save(@RequestBody TypeReglementReqDTO typeReglementReqDTO){
        return  null;
    }
}
