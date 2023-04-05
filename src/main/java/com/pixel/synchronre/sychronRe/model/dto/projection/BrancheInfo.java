package com.pixel.synchronre.sychronRe.model.dto.projection;

import java.time.LocalDateTime;

public interface BrancheInfo {
    Long getBranId();

    String getBranLibelle();

    String getBranLibelleAbrege();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
