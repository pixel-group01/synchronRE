package com.pixel.synchronre.sychronRe.model.dto.mapper;


import com.pixel.synchronre.sychronRe.model.dto.request.BanqueReqDTO;
import com.pixel.synchronre.sychronRe.model.entities.Banque;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BanqueMapper {


    Banque mapToBanqueToBanqueReqDTO(BanqueReqDTO dto);
}
