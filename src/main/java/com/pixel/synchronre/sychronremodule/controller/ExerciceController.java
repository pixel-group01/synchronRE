package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.CreateExerciceReq;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.UpdateExerciceReq;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController @ResponseStatus(HttpStatus.OK)
@RequestMapping("/exercices")
@RequiredArgsConstructor
public class ExerciceController {
    private final IserviceExercie exoService;

    @GetMapping(path = "/list")
    public List<ExerciceListResp> searchExercice(@RequestParam(defaultValue = "") String key) throws UnknownHostException {
        return exoService.searchExercice(key);
    }

    @PostMapping(path = "/create")
    public ExerciceDetailsResp createExercice(@RequestBody @Valid CreateExerciceReq dto) throws UnknownHostException {
        return exoService.createExercice(dto);
    }

    @PutMapping(path = "/update")
    public ExerciceDetailsResp updateExercice(@RequestBody @Valid UpdateExerciceReq dto) throws UnknownHostException {
        return exoService.updateExercice(dto);
    }

    @PutMapping(path = "/activate/{exeCode}")
    public ExerciceDetailsResp updateExercice(@PathVariable Long exeCode) throws UnknownHostException {
        return exoService.activateExercice(exeCode);
    }

    @GetMapping(path = "/getCourantAndPlus1")
    public List<ExerciceDetailsResp> getCourantAndPlus1(){
        return exoService.getExerciceCourantAndPlus1();
    }
}