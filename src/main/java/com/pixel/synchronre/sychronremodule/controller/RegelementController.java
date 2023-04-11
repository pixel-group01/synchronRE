package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.service.interfac.IserviceReglement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repartitions")
@RequiredArgsConstructor
public class RegelementController {
    private final IserviceReglement regService;
}
