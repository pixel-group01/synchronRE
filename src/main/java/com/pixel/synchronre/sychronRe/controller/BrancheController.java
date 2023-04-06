package com.pixel.synchronre.sychronRe.controller;

import com.pixel.synchronre.sychronRe.model.dto.request.BrancheReqDTO;
import com.pixel.synchronre.sychronRe.model.entities.Branche;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/branche")
@RequiredArgsConstructor
public class BrancheController {

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Branche save(@RequestBody BrancheReqDTO brancheReqDTO){
        return  null;
    }

}
