package com.pixel.synchronre.sychronRe.model.dto.projection;

import java.time.LocalDateTime;

public interface BanqueInfo {
    Long getBanId();

    String getBanLibelle();

    String getBanLibelleAbrege();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
