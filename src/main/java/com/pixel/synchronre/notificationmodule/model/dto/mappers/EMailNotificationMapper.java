package com.pixel.synchronre.notificationmodule.model.dto.mappers;

import com.pixel.synchronre.notificationmodule.model.entities.EmailNotification;
import com.pixel.synchronre.notificationmodule.model.dto.EmailNotificationDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EMailNotificationMapper
{
    EmailNotification mapToNotification(EmailNotificationDTO dto);
}
