package com.pixel.synchronre.authmodule.model.dtos.asignation;

import com.pixel.synchronre.authmodule.model.dtos.appprivilege.ExistingPrivilegeId;
import com.pixel.synchronre.authmodule.model.dtos.approle.ExistingRoleId;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvToRoleDTO
{
    @ExistingRoleId
    private Long roleId;
    @ExistingPrivilegeId
    private Long privilegeId;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private boolean permanent;
}
