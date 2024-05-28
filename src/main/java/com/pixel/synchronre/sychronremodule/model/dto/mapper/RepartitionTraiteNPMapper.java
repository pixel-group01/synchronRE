package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dto.cedantetraite.CesLeg;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RepartitionTraiteNPMapper
{
    @Autowired protected IJwtService jwtService;
    @Autowired protected TypeRepo typeRepo;

    @Mapping(target = "repPrime", source = "cesLeg.pmd")
    @Mapping(target = "repTaux", source = "cesLeg.tauxCesLeg")
    @Mapping(target = "repStaCode", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "paramCessionLegale", expression = "java(cesLeg.getParamCesLegalId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale(cesLeg.getParamCesLegalId()))")
    @Mapping(target = "repStatut", expression = "java(true)")
    @Mapping(target = "type", expression = "java(typeRepo.findByUniqueCode(\"REP_CES_LEG_TNP\").orElseThrow(()->new com.pixel.synchronre.sharedmodule.exceptions.AppException(\"Type (REP_CES_LEG_TNP) introuvable\")))")
    @Mapping(target = "cedanteTraite", expression = "java(cedTraiId == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite(cedTraiId))")
    public abstract Repartition mapToCesLegRepartition(CesLeg cesLeg, Long cedTraiId);

    @Mapping(target = "traiteNonProportionnel", expression = "java(dto.getTraiteNpId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(dto.getTraiteNpId()))")
    @Mapping(target = "cessionnaire", expression = "java(dto.getCesId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire(dto.getCesId()))")
    @Mapping(target = "repTauxComCourt", source = "repTauxCourtier")
    @Mapping(target = "repStaCode", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "repStatut", expression = "java(true)")
    public abstract Repartition mapToPlacementTnp(PlacementTraiteNPReq dto);
}
