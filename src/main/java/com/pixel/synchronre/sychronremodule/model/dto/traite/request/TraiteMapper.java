package com.pixel.synchronre.sychronremodule.model.dto.traite.request;

import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TraiteMapper
{
    //@Mapping(target = "typeTraite", expression = "java(dto.getTypeId() == null ? null : new com.pixel.synchronre.typemodule.model.entities.Type(dto.getTypeId()))")

    TraiteNonProportionnel mapToTraite(TraiteReq dto);


}

//Comment réutiliser la méthode mapToConditionTraite pour mapper le champ conditions de Traite à partir du champ private List<ConditionTraiteReq> conditions de traiteReq