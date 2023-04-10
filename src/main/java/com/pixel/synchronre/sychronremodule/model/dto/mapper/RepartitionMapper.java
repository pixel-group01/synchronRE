package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dto.repartition.request.CreateRepartitionReq;
import com.pixel.synchronre.sychronremodule.model.dto.repartition.response.RepartitionDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Repartition;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepartitionMapper {
    Repartition mapToRepartition(CreateRepartitionReq dto);
    RepartitionDetailsResp mapToRepartitionDetailsResp(Repartition res);
}
