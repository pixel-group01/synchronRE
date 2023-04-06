package com.pixel.synchronre.typemodule.model.dtos;

import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.entities.TypeParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TypeMapper
{
    @Mapping(target = "status", expression = "java(com.pixel.synchronre.sharedmodule.enums.PersStatus.ACTIVE)")
    @Mapping(target = "typeGroup", expression = "java(com.pixel.synchronre.typemodule.model.enums.TypeGroup.valueOf(dto.getTypeGroup()))")
    @Mapping(target = "uniqueCode", expression = "java(dto.getUniqueCode().toUpperCase())")
    @Mapping(target = "name", expression = "java(dto.getName().toUpperCase())")
    Type mapToType(CreateTypeDTO dto);

    Type mapToType(UpdateTypeDTO dto);

    @Mapping(target = "typeGroup", expression = "java(type.getTypeGroup().getGroupName())")
    ReadTypeDTO mapToReadTypeDTO(Type type);
    @Mapping(target = "parent.typeId", source = "dto.parentId")
    @Mapping(target = "child.typeId", source = "dto.childId")
    TypeParam mapToTypeParam(TypeParamDTO dto);
}