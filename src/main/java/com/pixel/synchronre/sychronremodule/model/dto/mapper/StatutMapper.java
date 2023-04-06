package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.CreateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatutMapper {
    Statut mapToStatut(CreateStatutReq dto);
    StatutDetailsResp mapToStatutDetailsResp(Statut res);
}
