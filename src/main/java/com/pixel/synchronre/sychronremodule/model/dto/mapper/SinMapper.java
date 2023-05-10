package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.request.CreateSinistreReq;
import com.pixel.synchronre.sychronremodule.model.dto.sinistre.response.SinistreDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Sinistre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class SinMapper
{
    @Autowired protected IJwtService jwtService;

    @Mapping(target = "userCreator", expression = "java(jwtService.getConnectedUserId() == null ? null : new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "functionCreator", expression = "java(jwtService.getConnectedUserFunctionId() == null ? null : new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    //@Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"SAI\"))")
    @Mapping(target = "affaire", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    public abstract Sinistre mapToSinistre(CreateSinistreReq dto);

    @Mapping(target = "affCode", source = "affaire.affCode")
    @Mapping(target = "affAssure", source = "affaire.affAssure")
    @Mapping(target = "affActivite", source = "affaire.affActivite")
    @Mapping(target = "affCapitalInitial", source = "affaire.affCapitalInitial")
    public abstract SinistreDetailsResp mapToSinistreDetailsResp(Sinistre sinistre);
}
