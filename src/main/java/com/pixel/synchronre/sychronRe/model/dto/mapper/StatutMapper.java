package com.pixel.synchronre.sychronRe.model.dto.mapper;

import com.pixel.synchronre.sychronRe.model.dto.request.StatutReq;
import com.pixel.synchronre.sychronRe.model.entities.Statut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatutMapper {
    Statut mapToStatut(StatutReq dto);
}
