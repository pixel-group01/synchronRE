package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.PlacementTraiteNPReq;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RepartitionTraiteNPMapper
{
    @Autowired protected IJwtService jwtService;//cedanteTraiteId


    @Mapping(target = "cedanteTraite", expression = "java(dto.getCedanteTraiteId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.CedanteTraite(dto.getCedanteTraiteId()))")
    @Mapping(target = "cessionnaire", expression = "java(dto.getCesId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire(dto.getCesId()))")
    @Mapping(target = "repTauxComCourt", source = "repTauxCourtier")
    @Mapping(target = "repStaCode", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    public abstract Repartition mapToPlacementTnp(PlacementTraiteNPReq dto);
}
