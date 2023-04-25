package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateCesLegReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreatePartCedRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreatePlaRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.service.interfac.IServiceCalculsComptables;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public abstract class RepartitionMapper {
    @Autowired protected TypeRepo typeRepo;
    @Autowired protected ParamCessionLegaleRepository pclRepo;
    @Autowired protected IServiceCalculsComptables comptaService;
    public abstract Repartition mapToRepartition(CreateRepartitionReq dto);


    private String affAssure;
    private String affActivite;
    private Long cesId;
    private String cesNom;
    private String cesSigle;
    private String cesEmail;
    private String cesTelephone;
    private BigDecimal affBesoinFac; //Reste à repartir après avoir fait la répartition
    private BigDecimal affTauxBesoinFac;

    @Mapping(target = "affId", source = "affaire.affId")
    @Mapping(target = "affCode", source = "affaire.affCode")
    @Mapping(target = "affAssure", source = "affaire.affAssure")
    @Mapping(target = "affActivite", source = "affaire.affActivite")
    @Mapping(target = "cesNom", source = "cessionnaire.cesNom")
    @Mapping(target = "cesSigle", source = "cessionnaire.cesSigle")
    @Mapping(target = "cesEmail", source = "cessionnaire.cesEmail")
    @Mapping(target = "cesTelephone", source = "cessionnaire.cesTelephone")
    @Mapping(target = "affBesoinFac", expression = "java(comptaService.calculateRestARepartir(res.getAffaire().getAffId()))")
    @Mapping(target = "affTauxBesoinFac", expression = "java(comptaService.calculateRestTauxARepartir(res.getAffaire().getAffId()))")
    public abstract RepartitionDetailsResp mapToRepartitionDetailsResp(Repartition res);

    @Mapping(target = "repStatut", expression = "java(true)")
    @Mapping(target = "type", expression = "java( typeRepo.findByUniqueCode(\"REP_CES_LEG\"))")
    @Mapping(target = "affaire", expression = "java(dto.getAffId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    @Mapping(target = "paramCessionLegale", expression = "java(dto.getParamCesLegalId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale(dto.getParamCesLegalId()))")
    public abstract Repartition mapToCesLegRepartition(CreateCesLegReq dto);

    @Mapping(target = "repStatut", expression = "java(true)")
    @Mapping(target = "type", expression = "java( typeRepo.findByUniqueCode(\"REP_CED\"))")
    @Mapping(target = "affaire", expression = "java(dto.getAffId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    public abstract Repartition mapToPartCedRepartition(CreatePartCedRepartitionReq dto);

    @Mapping(target = "repStatut", expression = "java(true)")
    @Mapping(target = "type", expression = "java( typeRepo.findByUniqueCode(\"REP_PLA\"))")
    @Mapping(target = "affaire", expression = "java(dto.getAffId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Affaire(dto.getAffId()))")
    @Mapping(target = "cessionnaire", expression = "java(dto.getCesId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire(dto.getCesId()))")
    public abstract Repartition mapToPlaRepartition(CreatePlaRepartitionReq dto);
}
