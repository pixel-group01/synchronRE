package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteNPRepository;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.CreateRisqueCouvertReq;
import com.pixel.synchronre.sychronremodule.model.dto.risquecouvert.RisqueCouvertResp;
import com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RisqueMapper
{
    @Autowired protected IJwtService jwtService;
    @Autowired protected TraiteNPRepository traiteNPRepo;
    @Mapping(target = "couverture", expression = "java(dto.getCouId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Couverture(dto.getCouId()))")
    @Mapping(target = "traiteNonProportionnel", expression = "dto.getTraiteNPId() == null ? null : java(new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(dto.getTraiteNPId()))")
    @Mapping(target = "activite", expression = "dto.getActiviteId() == null ? null : java(new com.pixel.synchronre.sychronremodule.model.entities.Activite(dto.getActiviteId()))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract RisqueCouvert mapToRisqueCouvert(CreateRisqueCouvertReq dto);

    @Mapping(target = "couId", source = "couverture.couId")
    @Mapping(target = "couLibelle", source = "couverture.couLibelle")
    @Mapping(target = "traiId", source = "traiteNonProportionnel.traiId")
    @Mapping(target = "traiReference", source = "traiteNonProportionnel.traiReference")
    @Mapping(target = "activiteId", source = "activite.activiteId")
    @Mapping(target = "activiteLibelle", source = "activite.activiteLibelle")
    @Mapping(target = "staCode", source = "statut.staCode")
    @Mapping(target = "staLibelle", source = "statut.staLibelle")
    public abstract RisqueCouvertResp mapToRisqueCouvertResp(RisqueCouvert risqueCouvert);
}

