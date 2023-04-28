package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtPlacementReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtRetourAffaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.mouvement.request.MvtSuivantAffaireReq;
import com.pixel.synchronre.sychronremodule.model.entities.Mouvement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MvtMapper
{
    @Autowired protected IJwtService jwtService;

    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"RET\"))")
    @Mapping(target = "affaire", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    @Mapping(target = "mvtUser", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "mvtFunction", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract Mouvement mapTomouvement(MvtRetourAffaireReq dto);

    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(dto.getStaCode()))")
    @Mapping(target = "affaire", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    @Mapping(target = "mvtUser", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "mvtFunction", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract Mouvement mapTomouvement(MvtSuivantAffaireReq dto);

    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(dto.getStaCode()))")
    @Mapping(target = "placement", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Repartition(dto.getPlaId()))")
    @Mapping(target = "mvtUser", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "mvtFunction", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract Mouvement mapTomouvement(MvtPlacementReq dto);
}
