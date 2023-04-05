package com.pixel.synchronre.sychronRe.model.dto.mapper;


import com.pixel.synchronre.sychronRe.model.dto.request.TypeReglementReqDTO;
import com.pixel.synchronre.sychronRe.model.entities.TypeReglement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TypeReglementMapper {


    TypeReglement mapToTypeReglementToTypeReglementReqDTO(TypeReglementReqDTO dto);
}
