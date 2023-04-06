package com.pixel.synchronre.authmodule.model.dtos.approle;

import com.pixel.synchronre.authmodule.model.entities.AppRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper
{
    AppRole mapToRole(CreateRoleDTO dto);
    ReadRoleDTO mapToReadRoleDTO(AppRole role);
}
