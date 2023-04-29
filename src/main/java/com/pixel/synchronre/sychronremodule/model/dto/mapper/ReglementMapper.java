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

@Mapper(componentModel = "spring")
public  abstract class ReglementMapper
{
    @Autowired protected IServiceCalculsComptables comptService;
    @Mapping(target = "affaire",  expression = "java(dto.getAffId()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    //@Mapping(target = "typeReglement", expression = "java(typeRepo.findByUniqueCode(dto.getTypeReglement()))")
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
    @Mapping(target = "dejaRegle", source = "appUser.userId")
    @Mapping(target = "resteARegler", source = "typeReglement.name")
    public abstract ReglementDetailsResp mapToReglementDetailsResp(Reglement res);

    void boo(Reglement res)
    {
        comptService.calculateRestARegler(res.getAffaire().getAffId());
        comptService.calculateDejaRegle(res.getAffaire().getAffId());
    }
}

