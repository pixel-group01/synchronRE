package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.request.CreateReglementReq;
import com.pixel.synchronre.sychronremodule.model.dto.reglement.response.ReglementDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Reglement;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptablesSinistre;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(componentModel = "spring")
public  abstract class ReglementMapper
{
    @Autowired protected IServiceCalculsComptables comptService;
    @Autowired protected IServiceCalculsComptablesSinistre sinComptaService;
    @Autowired protected TypeRepo typeRepo;
    @Autowired protected IJwtService jwtService;

    @Mapping(target = "affaire",  expression = "java(dto.getAffId()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    @Mapping(target = "sinistre", expression = "java(dto.getSinId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Sinistre(dto.getSinId()))")
    @Mapping(target = "regStatut", expression ="java(true)")
    //@Mapping(target = "typeReglement", expression = "java(typeRepo.findByUniqueCode(dto.getTypeReglement()))")
    @Mapping(target = "cessionnaire", expression = "java(dto.getCesId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire(dto.getCesId()))")
    @Mapping(target = "appUser", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "functionCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract Reglement mapToReglement(CreateReglementReq dto);


    @Mapping(target = "affCode", expression = "java(reg.getAffCode())")
    @Mapping(target = "affAssure", expression = "java(reg.getAffAssure())")
    @Mapping(target = "affActivite", expression = "java(reg.getAffActivite())")
    @Mapping(target = "affDateEffet", expression = "java(reg.getAffDateEffet())")
    @Mapping(target = "affDateEcheance", expression = "java(reg.getAffDateEcheance())")
    @Mapping(target = "cedNomFiliale", source = "affaire.cedante.cedNomFiliale")
    @Mapping(target = "cedSigleFiliale", source = "affaire.cedante.cedSigleFiliale")
    @Mapping(target = "userId", source = "appUser.userId")
    @Mapping(target = "typeReglement", source = "typeReglement.name")
    @Mapping(target = "dejaRegle", expression = "java(reg.getAffId() == null ? java.math.BigDecimal.ZERO : comptService.calculateDejaRegle(reg.getAffId()))")
    @Mapping(target = "resteARegler", expression = "java(reg.getAffId() == null ? null : comptService.calculateRestARegler(reg.getAffId()))")

    @Mapping(target = "mtAffaireDejaPaye", expression = "java(reg.getAffId() == null ? java.math.BigDecimal.ZERO : comptService.calculateDejaRegle(reg.getAffId()))")
    @Mapping(target = "mtAffaireRestantAPayer", expression = "java(reg.getAffId() == null ? null : comptService.calculateRestARegler(reg.getAffId()))")


    @Mapping(target = "sinId", source = "sinistre.sinId")
    @Mapping(target = "sinCode", source = "sinistre.sinCode")
    @Mapping(target = "sinMontant100", source = "sinistre.sinMontant100")
    @Mapping(target = "sinDateSurvenance", source = "sinistre.sinDateSurvenance")
    @Mapping(target = "sinDateDeclaration", source = "sinistre.sinDateDeclaration")
    @Mapping(target = "sinCommentaire", source = "sinistre.sinCommentaire")
    @Mapping(target = "mtSinDejaPaye", expression = "java(reg.getSinId() == null ? java.math.BigDecimal.ZERO : sinComptaService.calculateMtDejaPayeBySin(reg.getSinId()))")
    @Mapping(target = "mtSinResteAPayer", expression = "java(reg.getSinId()== null ? null : sinComptaService.calculateResteAPayerBySin(reg.getSinId()))")

    @Mapping(target = "mtSinDejaReverse", expression = "java(reg.getSinId() == null ? null : sinComptaService.calculateMtSinistreTotalDejaReverseBySin(reg.getSinId()))")
    @Mapping(target = "mtSinEnAttenteDeReversement", expression = "java(reg.getSinId() == null ? null : sinComptaService.calculateMtSinistreEnAttenteDeAReversement(reg.getSinId()))")
    public abstract ReglementDetailsResp mapToReglementDetailsResp(Reglement reg);

}

