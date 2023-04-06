package com.pixel.synchronre.authmodule.model.dtos.approle;

import jakarta.persistence.Column;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadRoleDTO
{
    private Long roleId;
    private String roleCode;
    private String roleName;
}
