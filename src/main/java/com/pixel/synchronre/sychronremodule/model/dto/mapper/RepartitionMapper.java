package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dao.ParamCessionLegaleRepository;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateCesLegReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreatePartCedRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreatePlaRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RepartitionMapper {
    @Autowired protected TypeRepo typeRepo;
    @Autowired protected ParamCessionLegaleRepository pclRepo;
    public abstract Repartition mapToRepartition(CreateRepartitionReq dto);
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
    public abstract Repartition mapToPlaRepartition(CreatePlaRepartitionReq dto);
}
