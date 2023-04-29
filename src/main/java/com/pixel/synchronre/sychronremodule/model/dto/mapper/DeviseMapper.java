package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.sychronremodule.model.dto.devise.request.CreateDeviseReq;
import com.pixel.synchronre.sychronremodule.model.dto.devise.request.UpdateDeviseReq;
import com.pixel.synchronre.sychronremodule.model.dto.devise.response.DeviseDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.Devise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviseMapper {

    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    Devise mapDeviseToDeviseReq(CreateDeviseReq dto);

    DeviseDetailsResp mapDeviseDetailsRespToDevise(Devise dev);

    Devise mapToUpdateDeviseReq(UpdateDeviseReq dto);
}
