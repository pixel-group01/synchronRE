package com.pixel.synchronre.authmodule.model.dtos.appuser;

import com.pixel.synchronre.authmodule.model.entities.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper
{

    @Mapping(target="active", expression="java(false)")
    @Mapping(target="notBlocked", expression="java(true)")
    @Mapping(target="creationDate", expression="java(java.time.LocalDateTime.now())")
    @Mapping(target="lastModificationDate", expression="java(java.time.LocalDateTime.now())")
    public abstract AppUser mapToUser(CreateUserDTO dto);

    @Mapping(target="active", expression="java(true)")
    @Mapping(target="notBlocked", expression="java(true)")
    @Mapping(target="creationDate", expression="java(java.time.LocalDateTime.now())")
    @Mapping(target="lastModificationDate", expression="java(java.time.LocalDateTime.now())")
    public abstract AppUser mapToUser(CreateActiveUserDTO dto);

    public abstract ReadUserDTO mapToReadUserDTO(AppUser user);
}