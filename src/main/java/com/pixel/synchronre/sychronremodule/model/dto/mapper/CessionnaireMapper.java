package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.CreateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CessionnaireMapper
{
    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    Cessionnaire mapToCessionnaire(CreateCessionnaireReq dto);
    CessionnaireDetailsResp mapToCessionnaireDetailsResp(Cessionnaire ces);

    @Mapping(target = "staLibelle", source = "statut.staLibelle")
    CessionnaireListResp mapToCessionnaireListResp(Cessionnaire ces);
}



