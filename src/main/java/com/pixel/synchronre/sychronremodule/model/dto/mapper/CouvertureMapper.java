package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sychronremodule.model.dto.couverture.request.CreateCouvertureReq;
import com.pixel.synchronre.sychronremodule.model.dto.couverture.response.CouvertureDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Couverture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class CouvertureMapper {

    @Autowired
    protected StatutRepository staRepo;

    @Mapping(target = "statut", expression = "java( staRepo.findByStaCode(\"ACT\"))")
    @Mapping(target = "branche",  expression = "java(dto.getBranId()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Branche(dto.getBranId()))")
    public abstract Couverture mapCouvertureReqToCouverture(CreateCouvertureReq dto);

    @Mapping(target = "branId",  expression = "java(couv.getBranche().getBranId()==null? null : new Long(couv.getBranche().getBranId()))")
    public abstract CouvertureDetailsResp mapToCouvertureDetailsResp(Couverture couv);
}
