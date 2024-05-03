package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatutMapper {
    @Mapping(target = "staType", expression = "java(com.pixel.synchronre.sharedmodule.enums.TypeStatut.valueOf(dto.getStaType()))")
    Statut mapToStatut(CreateStatutReq dto);
    StatutDetailsResp mapToStatutDetailsResp(Statut res);
}
