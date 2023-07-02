package com.pixel.synchronre.logmodule.controller.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.logmodule.controller.service.LogService;
import com.pixel.synchronre.logmodule.model.dtos.mapper.LogMapper;
import com.pixel.synchronre.logmodule.model.dtos.response.ConnexionList;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.time.LocalDate;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class LogController {
    private final ILogService logService;
    private final LogMapper logMapper;

    @GetMapping("/connexionList")
    public Page<ConnexionList> getConnexionList(@RequestParam(defaultValue = "") String key,
                                                @RequestParam(required = false) Long userId,
                                                @RequestParam(required = false) @JsonFormat(pattern = "dd/MM/yyyy") LocalDate debut,
                                                @RequestParam(required = false) @JsonFormat(pattern = "dd/MM/yyyy")LocalDate fin,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size)
    {
        return this.logService.getConnextionLogs(userId, debut, fin, PageRequest.of(page, size));
    }
}
