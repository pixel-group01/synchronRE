package com.pixel.synchronre.sychronremodule.model.dto.mouvement.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificationBody
{
    private long totalNotifications;
    private List<NotificationUnitaire> detailsNotifications;
}
