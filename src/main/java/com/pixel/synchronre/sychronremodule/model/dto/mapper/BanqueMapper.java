package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.sychronremodule.model.dao.BanqueRepository;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.banque.request.CreateBanqueReq;
import com.pixel.synchronre.sychronremodule.model.dto.banque.response.BanqueDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Banque;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BanqueMapper {

    @Autowired protected StatutRepository staRepo;

    @Mapping(target = "statut", expression = "java( staRepo.findByStaCode(\"ACT\"))")
    public abstract Banque mapBanqueToBanqueReq(CreateBanqueReq dto);

    public abstract BanqueDetailsResp mapBanqueDetailsRespToBanque(Banque ban);
}
