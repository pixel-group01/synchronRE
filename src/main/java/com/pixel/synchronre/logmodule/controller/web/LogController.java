package com.pixel.synchronre.logmodule.controller.web;

import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.logmodule.controller.service.LogService;
import com.pixel.synchronre.logmodule.model.dtos.mapper.LogMapper;
import com.pixel.synchronre.logmodule.model.dtos.response.ConnexionList;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class LogController {
    private final ILogService logService;
    private final LogMapper logMapper;

//    @GetMapping("/connexionList")
//    @ResponseStatus(HttpStatus.OK)
//    public ConnexionList getConnexionList(/*@PathVariable Long affId*/) throws MethodArgumentNotValidException, UnknownHostException {
//        //Affaire affaire = affRepo.findById(affId).orElseThrow(()->new AppException("Affaire introuvable"));
//        //return logMapper.mapConnexionListToLog();
//    }
}
