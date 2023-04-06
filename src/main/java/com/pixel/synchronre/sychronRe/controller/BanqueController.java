package com.pixel.synchronre.sychronRe.controller;


import com.pixel.synchronre.sychronRe.model.dto.projection.BanqueInfo;
import com.pixel.synchronre.sychronRe.model.dto.request.BanqueReqDTO;
import com.pixel.synchronre.sychronRe.model.entities.Banque;
import com.pixel.synchronre.sychronRe.service.implementation.BanqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banque")
@RequiredArgsConstructor
public class BanqueController {

    private final BanqueService iserviceBanque;

      @PostMapping("/save")
      @ResponseStatus(HttpStatus.CREATED)
      public Banque saveBanque(@RequestBody BanqueReqDTO banque){
           return iserviceBanque.saveBanque(banque);
      }

      @GetMapping("/all")
      public List<BanqueInfo> getAllBanque(){
          return iserviceBanque.getAllBanque();
      }
}
