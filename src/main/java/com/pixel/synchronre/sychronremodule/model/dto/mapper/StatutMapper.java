package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dto.request.StatutReq;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatutMapper {
    Statut mapToStatut(StatutReq dto);
}
