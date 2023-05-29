package com.pixel.synchronre.authmodule.model.dtos.appfunction;

import com.pixel.synchronre.authmodule.model.dtos.appuser.ExistingUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateInitialFncDTO
{
    private String name;
    protected LocalDate startsAt;
    protected LocalDate endsAt;
    private Set<Long> roleIds;
    private Set<Long> prvIds;
}
