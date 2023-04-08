package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Facultative;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacultativeMapper {
    Facultative mapToFacultative(CreateFacultativeReq dto);
    Facultative mapToStatut(CreateStatutReq dto);
    StatutDetailsResp mapToStatutDetailsResp(Statut res);
}
