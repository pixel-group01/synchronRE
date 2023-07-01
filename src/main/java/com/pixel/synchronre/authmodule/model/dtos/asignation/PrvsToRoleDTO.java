package com.pixel.synchronre.authmodule.model.dtos.asignation;

import com.pixel.synchronre.authmodule.model.dtos.approle.ExistingRoleId;
import com.pixel.synchronre.authmodule.model.dtos.approle.UniqueRoleCode;
import com.pixel.synchronre.authmodule.model.dtos.approle.UniqueRoleName;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueRoleCode @UniqueRoleName
public class PrvsToRoleDTO
{
    @ExistingRoleId
    private Long roleId;
    private String roleName;
    private String roleCode;
    private Set<Long> prvIds = new HashSet<>();
    private LocalDate startsAt;
    private LocalDate endsAt;
    private boolean permanent;
}
