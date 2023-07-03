package com.pixel.synchronre.authmodule.model.dtos.appfunction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pixel.synchronre.authmodule.model.dtos.appfunction.ExistingFncId;
import com.pixel.synchronre.authmodule.model.dtos.appuser.ExistingUserId;
import com.pixel.synchronre.authmodule.model.dtos.asignation.CoherentDates;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentDates
public class UpdateFncDTO
{
    @ExistingFncId
    private Long fncId;
    @ExistingUserId
    private Long userId;
    private Long visibilityId;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startsAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endsAt;
    private Set<Long> roleIds = new HashSet<>();
    private Set<Long> prvIds = new HashSet<>();
}
