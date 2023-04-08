package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.request.CreateParamCessionLegaleReq;
import com.pixel.synchronre.sychronremodule.model.dto.paramCessionLegale.response.ParamCessionLegaleDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.ParamCessionLegale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ParamCessionLegaleMapper {

    @Autowired
    protected StatutRepository staRepo;

    @Mapping(target = "statut", expression = "java(staRepo.findByStaCode(\"ACT\"))")
    @Mapping(target = "pays",  expression = "java(dto.getPaysId()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Pays(dto.getPaysId()))")
    @Mapping(target = "cedante",  expression = "java(dto.getCedId()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Cedante(dto.getCedId()))")
    public abstract ParamCessionLegale mapParamCessionToParamCessionLegaleReq(CreateParamCessionLegaleReq dto);

    @Mapping(target = "paysId",  expression = "java(param.getPays().getPaysId()==null? null : new Long(param.getPays().getPaysId())) ")
    @Mapping(target = "cedId",  expression = "java(param.getCedante().getCedId()==null? null : new Long(param.getCedante().getCedId()))")
    public  abstract ParamCessionLegaleDetailsResp mapParamDetailsToParamCessionLegale(ParamCessionLegale param);

}
