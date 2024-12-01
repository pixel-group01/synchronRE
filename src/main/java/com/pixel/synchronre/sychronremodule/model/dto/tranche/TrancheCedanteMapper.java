package com.pixel.synchronre.sychronremodule.model.dto.tranche;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.entities.TrancheCedante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TrancheCedanteMapper
{
    @Autowired
    protected IJwtService jwtService;
    @Mapping(target = "tranche", expression="java(dto.getTrancheId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Tranche(dto.getTrancheId()))")
    @Mapping(target = "cedante", expression="java(dto.getCedId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cedante(dto.getCedId()))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract TrancheCedante mapToTrancheCedante(TranchePrimeDto dto);
}
