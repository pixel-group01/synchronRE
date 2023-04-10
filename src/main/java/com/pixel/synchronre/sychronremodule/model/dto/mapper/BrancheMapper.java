package com.pixel.synchronre.sychronremodule.model.dto.mapper;




import com.pixel.synchronre.sychronremodule.model.dto.branche.request.CreateBrancheReq;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;

import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.branche.response.BrancheDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Branche;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface BrancheMapper
{
    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    Branche mapBrancheToBrancheReq(CreateBrancheReq dto);

    BrancheDetailsResp mapBrancheDetailsRespToBranche(Branche bran);
}
