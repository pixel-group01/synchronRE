package com.pixel.synchronre.sychronRe.model.dto.projection;

import java.time.LocalDateTime;

public interface CessionnaireInfo {
    Long getCesId();

    String getCesAdressePostale();

    String getCesCellulaire();

    String getCesEmail();

    String getCesLibelle();

    String getCesSituationGeo();

    String getCesTelephone();

    String getCeslibAbrege();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
