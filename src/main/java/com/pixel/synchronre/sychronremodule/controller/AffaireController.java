package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
@RequestMapping("/affaire")
@RequiredArgsConstructor
public class AffaireController {
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public Affaire saveAffaire(@RequestBody @Valid Affaire privilege, BindingResult bindingResult) throws MethodArgumentNotValidException, UnknownHostException {
       // if(bindingResult.hasErrors()) throw new MethodArgumentNotValidException(null ,bindingResult);
        return null;
                //prvService.savePrivilege(privilege);
    }
}
