package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.CreateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract interface ParamCessionLegaleMapper
{
    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "pays",  expression = "java(dto.getPaysCode()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Pays(dto.getPaysCode()))")
    ParamCessionLegale mapParamCessionToParamCessionLegaleReq(CreateParamCessionLegaleReq dto);

    @Mapping(target = "paysCode",  expression = "java(param.getPays().getPaysCode()==null? null : new String(param.getPays().getPaysCode())) ")
    ParamCessionLegaleDetailsResp mapParamDetailsToParamCessionLegale(ParamCessionLegale param);
}
