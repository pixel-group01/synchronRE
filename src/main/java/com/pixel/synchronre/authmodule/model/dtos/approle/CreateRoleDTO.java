package com.pixel.synchronre.authmodule.model.dtos.approle;

import lombok.*;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateRoleDTO
{
    @UniqueRoleCode
    private String roleCode;
    @UniqueRoleName
    private String roleName;
    private Set<Long> prvIds;
}
