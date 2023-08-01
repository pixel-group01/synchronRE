package com.pixel.synchronre.authmodule.model.dtos.appfunction;

import com.pixel.synchronre.authmodule.controller.repositories.PrvToFunctionAssRepo;
import com.pixel.synchronre.authmodule.controller.repositories.RoleToFunctionAssRepo;
import com.pixel.synchronre.authmodule.controller.repositories.UserRepo;
import com.pixel.synchronre.authmodule.controller.services.spec.IMenuMutatorService;
import com.pixel.synchronre.authmodule.controller.services.spec.IMenuReaderService;
import com.pixel.synchronre.authmodule.model.dtos.appprivilege.PrivilegeMapper;
import com.pixel.synchronre.authmodule.model.dtos.approle.RoleMapper;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class FncMapper
{
    @Autowired protected PrvToFunctionAssRepo ptfRepo;
    @Autowired protected RoleToFunctionAssRepo rtfRepo;
    @Autowired protected PrivilegeMapper prvMapper;
    @Autowired protected RoleMapper roleMapper;
    @Autowired protected IMenuReaderService menuService;
    @Autowired protected UserRepo userRepo;

    @Mapping(target = "user", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(dto.getUserId()))")
    @Mapping(target = "fncStatus", expression = "java(2)")
    @Mapping(target = "cesId", expression = "java(dto.getCesId() == null ? userRepo.getUserCesId(dto.getUserId()) : dto.getCesId())")
    @Mapping(target = "visibilityId", source = "cedId")
    public abstract AppFunction mapToFunction(CreateFncDTO dto);

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "privileges", expression = "java(ptfRepo.getFncPrivileges(fnc.getId()).stream().map(prvMapper::mapToReadPrivilegeDTO).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "roles", expression = "java(rtfRepo.getFncRoles(fnc.getId()).stream().map(roleMapper::mapToReadRoleDTO).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "menus", expression = "java(menuService.getMenusByFncId(fnc.getId()))")
    public abstract ReadFncDTO mapToReadFncDto(AppFunction fnc);
}
