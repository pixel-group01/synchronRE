package com.pixel.synchronre.sychronRe.model.dto.projection;

import java.time.LocalDateTime;

public interface TypeReglementInfo {
    Long getTypRegId();

    LocalDateTime getCreatedAt();

    String getTypRegLibelle();

    String getTypRegLibelleAbrege();

    LocalDateTime getUpdatedAt();
}
