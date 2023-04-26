package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.CreateExerciceReq;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.request.UpdateExerciceReq;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.exercice.response.ExerciceListResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.UpdateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceExercie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController
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

}
