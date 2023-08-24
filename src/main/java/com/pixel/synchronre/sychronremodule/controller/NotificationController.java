package com.pixel.synchronre.sychronremodule.controller;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.UpdateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutListResp;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceNotification;
import com.pixel.synchronre.sychronremodule.service.interfac.StatutIservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;


@RestController @ResponseStatus(HttpStatus.OK)
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final IServiceNotification notService;


    @GetMapping()
    public NotificationResp getNotifications() {
        return notService.getNotifications();
    }
}
