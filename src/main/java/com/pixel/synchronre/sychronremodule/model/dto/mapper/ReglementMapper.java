package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(componentModel = "spring")
public  abstract class ReglementMapper
{
    @Autowired protected IServiceCalculsComptables comptService;
    @Mapping(target = "affaire",  expression = "java(dto.getAffId()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    @Mapping(target = "sinistre", expression = "java(dto.getSinId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Sinistre(dto.getSinId()))")
    @Mapping(target = "regStatut", expression ="java(true)")
    //@Mapping(target = "typeReglement", expression = "java(typeRepo.findByUniqueCode(dto.getTypeReglement()))")
    @Mapping(target = "cessionnaire", expression = "java(dto.getCesId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire(dto.getCesId()))")
    public abstract Reglement mapToReglement(CreateReglementReq dto);


    @Mapping(target = "affCode", source = "affaire.affCode")
    @Mapping(target = "affAssure", source = "affaire.affAssure")
    @Mapping(target = "affActivite", source = "affaire.affActivite")
    @Mapping(target = "affDateEffet", source = "affaire.affDateEffet")
    @Mapping(target = "affDateEcheance", source = "affaire.affDateEcheance")
    @Mapping(target = "cedNomFiliale", source = "affaire.cedante.cedNomFiliale")
    @Mapping(target = "cedSigleFiliale", source = "affaire.cedante.cedSigleFiliale")
    @Mapping(target = "userId", source = "appUser.userId")
    @Mapping(target = "typeReglement", source = "typeReglement.name")
    @Mapping(target = "dejaRegle", expression = "java(comptService.calculateDejaRegle(res.getAffaire().getAffId()))")
    @Mapping(target = "resteARegler", expression = "java(comptService.calculateRestARegler(res.getAffaire().getAffId()))")

    @Mapping(target = "sinId", source = "sinistre.sinId")
    @Mapping(target = "sinCode", source = "sinistre.sinCode")
    @Mapping(target = "sinMontant100", source = "sinistre.sinMontant100")
    @Mapping(target = "sinDateSurvenance", source = "sinistre.sinDateSurvenance")
    @Mapping(target = "sinDateDeclaration", source = "sinistre.sinDateDeclaration")
    @Mapping(target = "sinCommentaire", source = "sinistre.sinCommentaire")
    public abstract ReglementDetailsResp mapToReglementDetailsResp(Reglement res);
}

