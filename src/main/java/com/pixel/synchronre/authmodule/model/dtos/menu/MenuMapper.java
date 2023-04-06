package com.pixel.synchronre.authmodule.model.dtos.menu;

import com.pixel.synchronre.authmodule.model.entities.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuMapper
{
    @Mapping(target = "status", expression = "java(com.pixel.synchronre.sharedmodule.enums.PersStatus.ACTIVE)")
    Menu mapToMenu(CreateMenuDTO dto);
}