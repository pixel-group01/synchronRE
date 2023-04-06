package com.pixel.synchronre.authmodule.model.dtos.asignation;

import com.pixel.synchronre.authmodule.model.dtos.approle.ExistingRoleId;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvsToRoleDTO
{
    @ExistingRoleId
    private Long roleId;
    private Set<Long> prvIds = new HashSet<>();
    private LocalDate startsAt;
    private LocalDate endsAt;
    private boolean permanent;
}
