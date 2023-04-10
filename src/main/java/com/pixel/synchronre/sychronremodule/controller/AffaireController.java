package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.UpdateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceFacultative;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
@RequestMapping("/affaires")
@RequiredArgsConstructor
public class AffaireController
{
    private final IserviceFacultative facService;

    @PostMapping("/facultative/create")
    @ResponseStatus(HttpStatus.CREATED)
    public FacultativeDetailsResp saveAffaire(@RequestBody @Valid CreateFacultativeReq dto) throws MethodArgumentNotValidException, UnknownHostException {
        return facService.createFacultative(dto);
    }

    @PutMapping("/facultative/update")
    @ResponseStatus(HttpStatus.CREATED)
    public FacultativeDetailsResp saveAffaire(@RequestBody @Valid UpdateFacultativeReq dto) throws MethodArgumentNotValidException, UnknownHostException {
        return facService.updateFacultative(dto);
    }
}
