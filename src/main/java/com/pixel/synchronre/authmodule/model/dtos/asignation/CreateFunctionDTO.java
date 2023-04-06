package com.pixel.synchronre.authmodule.model.dtos.asignation;

import com.pixel.synchronre.authmodule.model.dtos.appuser.ExistingUserId;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentDates
public class CreateFunctionDTO
{
    @ExistingUserId
    private Long userId;
    private Long visibilityId;
    private String intitule;
    private LocalDate startsAt;
    private LocalDate endsAt;
    private Set<Long> roleIds;
    private Set<Long> prvIds;
}
