package com.pixel.synchronre.sychronRe.controller;


import com.pixel.synchronre.sychronRe.model.dto.request.BanqueReqDTO;
import com.pixel.synchronre.sychronRe.model.entities.Banque;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banque")
@RequiredArgsConstructor
public class BanqueController {

      @PostMapping("/save")
      @ResponseStatus(HttpStatus.CREATED)
      public Banque saveAffaire(@RequestBody BanqueReqDTO banqueReqDTO){
          return null;
      }
}
