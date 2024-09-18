package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dto.limitesouscription.LimiteSouscriptionReq;
import com.pixel.synchronre.sychronremodule.model.dto.tranche.TrancheReq;
import com.pixel.synchronre.sychronremodule.model.entities.LimiteSouscription;
import com.pixel.synchronre.sychronremodule.model.entities.Tranche;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class LimiteSouscriptionMapper
{
    @Autowired protected IJwtService jwtService;

    @Mapping(target = "risqueCouvert", expression = "java(dto.getRisqueId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert(dto.getRisqueId()))")
    @Mapping(target = "categorie", expression = "java(dto.getCategorieId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Categorie(dto.getCategorieId() ))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract LimiteSouscription mapToLimiteSouscription(LimiteSouscriptionReq dto);
}