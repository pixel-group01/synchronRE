package com.pixel.synchronre.sychronRe.model.dto.mapper;


import com.pixel.synchronre.sychronRe.model.dto.request.BrancheReqDTO;
import com.pixel.synchronre.sychronRe.model.entities.Branche;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrancheMapper {

    Branche mapBrancheToBrancheReqDTO (BrancheReqDTO dto);
}
