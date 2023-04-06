package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.sychronremodule.model.dto.request.BrancheReqDTO;
import com.pixel.synchronre.sychronremodule.model.entities.Branche;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrancheMapper {

    Branche mapBrancheToBrancheReqDTO (BrancheReqDTO dto);
}
