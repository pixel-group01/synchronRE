package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.sychronremodule.model.dao.BanqueRepository;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.CreateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface BanqueMapper
{
    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    Banque mapBanqueToBanqueReq(CreateBanqueReq dto);

    BanqueDetailsResp mapBanqueDetailsRespToBanque(Banque ban);
}
