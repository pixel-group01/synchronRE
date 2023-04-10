package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.CreatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.request.UpdatePaysReq;
import com.pixel.synchronre.sychronremodule.model.dto.pays.response.PaysDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Pays;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PaysMapper {

    @Autowired StatutRepository staRepo;

    @Mapping(target = "statut", expression = "java( staRepo.findByStaCode(\"ACT\"))")
    public abstract Pays mapToPaysReq(CreatePaysReq dto);

    public abstract PaysDetailsResp mapToPaysDetails(Pays pa);


    public abstract Pays mapToUpdatePaysReq(UpdatePaysReq dto);
}
