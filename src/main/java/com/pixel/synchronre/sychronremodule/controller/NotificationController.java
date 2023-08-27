package com.pixel.synchronre.sychronremodule.controller;

import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationBody;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.response.NotificationUnitaire;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController @ResponseStatus(HttpStatus.OK)
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final IServiceNotification notService;

    @GetMapping()
    public NotificationBody getNotifications() {
        return notService.getNotificationUnitaires();
    }
}
