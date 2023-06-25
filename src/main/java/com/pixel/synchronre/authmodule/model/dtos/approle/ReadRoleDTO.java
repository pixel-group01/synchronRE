package com.pixel.synchronre.authmodule.model.dtos.approle;

import com.pixel.synchronre.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadRoleDTO
{
    private Long roleId;
    private String roleCode;
    private String roleName;
    private Set<ReadPrvDTO> privileges;
}
