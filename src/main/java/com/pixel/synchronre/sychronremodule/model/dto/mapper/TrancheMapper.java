package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class TrancheMapper
{
    @Autowired protected IJwtService jwtService;

    @Mapping(target = "risqueCouvert", expression = "java(dto.getRisqueId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert(dto.getRisqueId()))")
    @Mapping(target = "traiteNonProportionnel", expression = "java(dto.getTraiteNPId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(dto.getTraiteNPId() ))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    public abstract Tranche mapToTranche(TrancheReq dto);
}