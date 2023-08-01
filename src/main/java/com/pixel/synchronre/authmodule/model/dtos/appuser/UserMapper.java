package com.pixel.synchronre.authmodule.model.dtos.appuser;

import com.pixel.synchronre.authmodule.controller.services.spec.IFunctionService;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.CessionnaireRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper
{
    @Autowired protected CessionnaireRepository cesRepo;
    @Autowired protected CedRepo cedRepo;
    @Autowired protected IFunctionService functionService;

    @Mapping(target="active", expression="java(false)")
    @Mapping(target="notBlocked", expression="java(true)")
    public abstract AppUser mapToUser(CreateUserDTO dto);

    @Mapping(target="active", expression="java(true)")
    @Mapping(target="notBlocked", expression="java(true)")
    public abstract AppUser mapToUser(CreateActiveUserDTO dto);

    public abstract ReadUserDTO mapToReadUserDTO(AppUser user);

    @Mapping(target = "cedName", expression = "java(user.getVisibilityId() == null ? null : cedRepo.getCedNameById(user.getVisibilityId()))")
    @Mapping(target = "cedSigle", expression = "java(user.getVisibilityId() == null ? null : cedRepo.getCedSigleById(user.getVisibilityId()))")
    @Mapping(target = "cesName", expression = "java(user.getCesId() == null ? null: cesRepo.getCesNameById(user.getCesId()))")
    @Mapping(target = "cesSigle", expression = "java(user.getCesId() == null ? null: cesRepo.getCesSigleById(user.getCesId()))")
    @Mapping(target = "currentFunctionName", expression = "java(user.getUserId() == null ? null : functionService.getActiveCurrentFunctionName(user.getUserId()))")
    @Mapping(target = "statut", source = "statut.staLibelle")
    public abstract ListUserDTO mapToListUserDTO(AppUser user);
}
