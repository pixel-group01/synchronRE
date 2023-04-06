package com.pixel.synchronre.authmodule.model.dtos.asignation;

import com.pixel.synchronre.authmodule.model.dtos.appfunction.ExistingFncId;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChangeFunctionValidityPeriodDTO
{
    @ExistingFncId
    private Long fncId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
