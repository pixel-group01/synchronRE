package com.pixel.synchronre.notificationmodule.controller.dao;

import com.pixel.synchronre.notificationmodule.model.entities.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailNotificationRepo extends JpaRepository<EmailNotification, Long>
{
    Optional<EmailNotification> findByToken(String token);

}