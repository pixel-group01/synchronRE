package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CedanteTraiteResp;
import com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CedanteTraiteMapper
{
    @Autowired protected IJwtService jwtService;
    @Mapping(target = "cedante", expression = "java(dto.getCedId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cedante(dto.getCedId()))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    public abstract CedanteTraite mapToCedanteTraite(CedanteTraiteReq dto);
}
