package com.pixel.synchronre.sychronremodule.model.dto.traite.request;

import com.pixel.synchronre.sychronremodule.model.entities.ConditionTraite;
import com.pixel.synchronre.sychronremodule.model.entities.Traite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TraiteMapper
{
    @Mapping(target = "typeTraite", expression = "java(dto.getTypeId() == null ? null : new com.pixel.synchronre.typemodule.model.entities.Type(dto.getTypeId()))")
    @Mapping(target = "conditions", source = "conditions")
    Traite mapToTraite(TraiteReq dto);

    @Mapping(target = "traite", expression = "java(dto.getTraiteId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Traite(dto.getTraiteId()))")
    @Mapping(target = "cedante", expression = "java(dto.getCedId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cedante(dto.getTraiteId()))")
    @Mapping(target = "couverture", expression = "java(dto.getCouId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Couverture(dto.getTraiteId()))")
    @Mapping(target = "typeCondition", expression = "java(dto.getTypeId() == null ? null : new com.pixel.synchronre.typemodule.model.entities.Type(dto.getTypeId()))")
    ConditionTraite mapToConditionTraite(ConditionTraiteReq dto);

    default List<ConditionTraite> mapToConditionTraites(List<ConditionTraiteReq> conditionTraiteReqs) {
        return conditionTraiteReqs.stream()
                .map(this::mapToConditionTraite)
                .collect(Collectors.toList());
    }
}

//Comment réutiliser la méthode mapToConditionTraite pour mapper le champ conditions de Traite à partir du champ private List<ConditionTraiteReq> conditions de traiteReq