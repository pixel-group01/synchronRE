package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sychronremodule.model.dao.TraiteNPRepository;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.CreateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.request.UpdateTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.Affaire;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TraiteNPMapper
{
    @Autowired protected IJwtService jwtService;
    @Autowired protected TraiteNPRepository traiteNPRepo;
    @Mapping(target = "traiEcerciceRattachement", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(com.pixel.synchronre.sychronremodule.model.enums.EXERCICE_RATTACHEMENT.class, dto.getTraiEcerciceRattachement()))")
    @Mapping(target = "traiPeriodicite", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(com.pixel.synchronre.sychronremodule.model.entities.PERIODICITE.class, dto.getTraiPeriodicite()))")
    @Mapping(target = "exercice", expression = "java(dto.getExeCode() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Exercice(dto.getExeCode()))")
    //@Mapping(target = "traiSource", expression = "java(dto.getTraiSourceRef() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(dto.getTraiSourceRef()))")
    @Mapping(target = "nature", expression = "java(dto.getNatCode() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Nature(dto.getNatCode()))")
    @Mapping(target = "traiDevise", expression ="java(dto.getDevCode() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Devise(dto.getDevCode()))")
    @Mapping(target = "traiCompteDevise", expression ="java(dto.getTraiCoursDevise() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Devise(dto.getTraiCompteDevCode()))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "traiSource", expression = "java(traiteNPRepo.findByRef(dto.getTraiSourceRef()))")
    @Mapping(target = "traiUserCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "traiFonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract TraiteNonProportionnel mapToTraiteNP(CreateTraiteNPReq dto);




    //@Mapping(target = "exeCode", source = "exercice.exeCode")
    @Mapping(target = "natCode", source = "nature.natCode")
    @Mapping(target = "devCode", source = "traiDevise.devCode")
    @Mapping(target = "traiCompteDevCode", source = "traiCompteDevise.devCode")
    public abstract UpdateTraiteNPReq mapToTraiteNP(TraiteNonProportionnel traiteNP);


    @Mapping(target = "traiEcerciceRattachement", source = "traiEcerciceRattachement.libelle")
    @Mapping(target = "traiPeriodicite", source = "traiPeriodicite.libelle")
    @Mapping(target = "exeCode", source = "exercice.exeCode")
    @Mapping(target = "traiSourceRef", source = "traiSource.traiReference")
    @Mapping(target = "traiSourceLibelle", source = "traiSource.traiLibelle")
    @Mapping(target = "natCode", source = "nature.natCode")
    @Mapping(target = "natLibelle", source = "nature.natLibelle")
    @Mapping(target = "devCode", source = "traiDevise.devCode")
    @Mapping(target = "traiCompteDevCode", source = "traiCompteDevise.devCode")
    @Mapping(target = "traiStaCode", source = "statut.staCode")
    @Mapping(target = "traiStaLibelle", source = "statut.staLibelle")
    @Mapping(target = "traiUserCreatorEmail", expression = "java(jwtService.getJwtInfos().getUserEmail())")
    @Mapping(target = "traiUserCreatorNomPrenom", expression = "java(jwtService.getJwtInfos().getNomPrenom())")
    @Mapping(target = "traiFonCreatorName", expression = "java(jwtService.getJwtInfos().getFncName())")
    public abstract TraiteNPResp mapToTraiteNPResp(TraiteNonProportionnel traiteNP);

}

