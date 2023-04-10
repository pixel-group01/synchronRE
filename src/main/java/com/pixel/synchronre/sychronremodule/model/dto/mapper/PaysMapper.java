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
public interface PaysMapper
{
    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    Pays mapToPaysReq(CreatePaysReq dto);
    PaysDetailsResp mapToPaysDetails(Pays pa);
    Pays mapToUpdatePaysReq(UpdatePaysReq dto);
}
