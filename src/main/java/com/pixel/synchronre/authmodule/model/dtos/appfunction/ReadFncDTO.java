package com.pixel.synchronre.authmodule.model.dtos.appfunction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadFncDTO
{
    private Long visibilityId;
    private String name;
    private Long userId;
    private String email;
    protected int fncStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;
}
