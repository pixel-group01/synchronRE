package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.request.CreateCessionnaireReq;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.response.CessionnaireDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesEmail;
import com.pixel.synchronre.sychronremodule.model.dto.cessionnaire.validator.UniqueCesTel;
import com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface CessionnaireMapper
{
    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    Cessionnaire mapToCessionnaire(CreateCessionnaireReq dto);
    CessionnaireDetailsResp mapToCessionnaireDetailsResp(Cessionnaire ces);
}



