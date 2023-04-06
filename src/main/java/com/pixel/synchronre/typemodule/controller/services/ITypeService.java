package com.pixel.synchronre.typemodule.controller.services;


import com.pixel.synchronre.typemodule.model.dtos.CreateTypeDTO;
import com.pixel.synchronre.typemodule.model.dtos.TypeParamDTO;
import com.pixel.synchronre.typemodule.model.dtos.TypeParamsDTO;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import com.pixel.synchronre.typemodule.model.dtos.UpdateTypeDTO;
import org.springframework.data.domain.Page;

import java.net.UnknownHostException;
import java.util.List;

public interface ITypeService
{
    Type createType(CreateTypeDTO dto) throws UnknownHostException;
    Type updateType(UpdateTypeDTO dto) throws UnknownHostException;
    void deleteType(Long typeId) throws UnknownHostException;
    void addSousType(TypeParamDTO dto) throws UnknownHostException;
    void removeSousType(TypeParamDTO dto) throws UnknownHostException;
    void setSousTypes(TypeParamsDTO dto) throws UnknownHostException;
    boolean parentHasDirectSousType(Long parentId, Long childId);
    boolean parentHasDistantSousType(Long parentId, Long childId);

    List<Type> getPossibleSousTypes(Long parentId);

    Type setSousTypesRecursively(Long typeId);
    List<Type> getSousTypesRecursively(Long typeId);
    List<TypeGroup> getTypeGroups();

    Page<Type> searchPageOfTypes(String key, String typeGroup, int pageNum, int pageSize);
    Page<Type> searchPageOfDeletedTypes(String key, String typeGroup, int pageNum, int pageSize);

    void restoreType(Long idType) throws UnknownHostException;
}
