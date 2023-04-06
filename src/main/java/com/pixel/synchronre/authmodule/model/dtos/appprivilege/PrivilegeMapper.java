package com.pixel.synchronre.authmodule.model.dtos.appprivilege;

import com.pixel.synchronre.authmodule.model.entities.AppPrivilege;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper
{
    @Mapping(target = "prvType.typeId", source = "typeId")
    AppPrivilege getAppPrivilege(CreatePrivilegeDTO dto);
    @Mapping(target = "prvTypeName", source = "prvType.name")
    ReadPrvDTO mapToReadPrivilegeDTO(AppPrivilege privilege);
}
