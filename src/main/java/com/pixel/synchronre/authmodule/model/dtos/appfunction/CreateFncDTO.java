package com.pixel.synchronre.authmodule.model.dtos.appfunction;

import com.pixel.synchronre.authmodule.model.dtos.appuser.ExistingUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateFncDTO
{
    private Long visibilityId;
    private String name;
    @ExistingUserId
    private Long userId;
    protected int fncStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;
    private Set<Long> roleIds;
    private Set<Long> prvIds;
}
