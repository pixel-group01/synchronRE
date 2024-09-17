package com.pixel.synchronre.sychronremodule.model.dto.association.response;

import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivitesResp {
    private Long couId;
    private String couLibelle;
}
