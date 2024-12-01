package com.pixel.synchronre.typemodule.controller.services;

import com.pixel.synchronre.sharedmodule.enums.PersStatus;
import com.pixel.synchronre.logmodule.controller.service.ILogService;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sharedmodule.utilities.StringUtils;
import com.pixel.synchronre.typemodule.controller.repositories.TypeParamRepo;
import com.pixel.synchronre.typemodule.controller.repositories.TypeRepo;
import com.pixel.synchronre.typemodule.model.constants.TypeActions;
import com.pixel.synchronre.typemodule.model.constants.TypeTables;
import com.pixel.synchronre.typemodule.model.dtos.*;
import com.pixel.synchronre.typemodule.model.entities.Type;
import com.pixel.synchronre.typemodule.model.entities.TypeParam;
import com.pixel.synchronre.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service @Transactional
@RequiredArgsConstructor
public class TypeService implements ITypeService
{
    private final TypeRepo typeRepo;
    private final TypeMapper typeMapper;
    private final TypeParamRepo typeParamRepo;
    private final ILogService logger;
    private final ObjectCopier<Type> typeCopier;
    private final ObjectCopier<TypeParam> typeParamCopier;
    @Override
    public Type createType(CreateTypeDTO dto) throws UnknownHostException {
        Type type = typeMapper.mapToType(dto);
        type = typeRepo.save(type);
        logger.logg(TypeActions.CREATE_TYPE, null, type, TypeTables.TYPE);
        return type;
    }

    @Override @Transactional
    public Type updateType(UpdateTypeDTO dto) throws UnknownHostException {
        Type loadedType = dto.getTypeId() == null ? null : typeRepo.findById(dto.getTypeId()).orElse(null);
        if(loadedType == null) return null;
        Type oldType = typeCopier.copy(loadedType);
        loadedType.setTypeGroup(Arrays.stream(TypeGroup.values()).filter(gr->gr.getGroupCode().equalsIgnoreCase(dto.getTypeGroup())).findFirst().orElse(null));
        loadedType.setName(dto.getName().toUpperCase(Locale.ROOT));
        loadedType.setUniqueCode(dto.getUniqueCode().toUpperCase(Locale.ROOT));
        logger.logg(TypeActions.UPDATE_TYPE, oldType, loadedType, TypeTables.TYPE);

        return loadedType;
    }

    @Override
    public void deleteType(Long typeId) throws UnknownHostException {
        Type loadedType = typeId == null ? null : typeRepo.findById(typeId).orElse(null);
        if(loadedType == null || loadedType.getStatus() == PersStatus.DELETED) return;
        Type oldType = typeCopier.copy(loadedType);
        loadedType.setStatus(PersStatus.DELETED);
        logger.logg(TypeActions.DELETE_TYPE, oldType, loadedType, TypeTables.TYPE);
    }

    @Override
    public void restoreType(Long typeId) throws UnknownHostException {
        Type loadedType = typeId == null ? null : typeRepo.findById(typeId).orElse(null);
        if(loadedType == null || loadedType.getStatus() == PersStatus.ACTIVE) return;
        Type oldType = typeCopier.copy(loadedType);
        loadedType.setStatus(PersStatus.ACTIVE);
        logger.logg(TypeActions.RESTORE_TYPE, oldType, loadedType, TypeTables.TYPE);
    }

    @Override @Transactional
    public void addSousType(TypeParamDTO dto) throws UnknownHostException {
        if(this.parentHasDistantSousType(dto.getChildId(), dto.getParentId())) return;
        if(typeParamRepo.alreadyExistsAndActive(dto.getParentId(), dto.getChildId())) return;
        if(typeParamRepo.alreadyExistsAndNotActive(dto.getParentId(), dto.getChildId()))
        {
            TypeParam typeParam = typeParamRepo.findByParentAndChild(dto.getParentId(), dto.getChildId());
            TypeParam oldTypeParam = typeParamCopier.copy(typeParam);
            typeParam.setStatus(PersStatus.ACTIVE);
            logger.logg(TypeActions.RESTORE_SUB_TYPE, oldTypeParam, typeParam, TypeTables.TYPE_PARAM);
            return;
        }
        TypeParam typeParam = typeMapper.mapToTypeParam(dto);
        typeParam.setStatus(PersStatus.ACTIVE);
        typeParam = typeParamRepo.save(typeParam);
        logger.logg(TypeActions.ADD_SUB_TYPE, null, typeParam, TypeTables.TYPE_PARAM);
    }


    @Override @Transactional
    public void setSousTypes(TypeParamsDTO dto)
    {
        List<Long> alreadyExistingSousTypeIds = typeRepo.findChildrenIds(dto.getParentId());
        Set<Long> newSousTypesToSetIds = Arrays.stream(dto.getChildIds()).filter(id0-> alreadyExistingSousTypeIds.stream().noneMatch(id0::equals)).collect(Collectors.toSet());
        Set<Long> sousTypesToRemoveIds = alreadyExistingSousTypeIds.stream().filter(id0-> Arrays.stream(dto.getChildIds()).noneMatch(id0::equals)).collect(Collectors.toSet());

        newSousTypesToSetIds.stream().map(id->new TypeParamDTO(id, dto.getParentId())).forEach(o-> {
            try {
                this.addSousType(o);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        sousTypesToRemoveIds.stream().map(id->new TypeParamDTO(id, dto.getParentId())).forEach(o-> {
            try {
                this.removeSousType(o);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    @Override @Transactional
    public void removeSousType(TypeParamDTO dto) throws UnknownHostException {
        if(typeParamRepo.alreadyExistsAndNotActive(dto.getParentId(), dto.getChildId())) return;
        if(typeParamRepo.alreadyExistsAndActive(dto.getParentId(), dto.getChildId()))
        {
            TypeParam typeParam = typeParamRepo.findByParentAndChild(dto.getParentId(), dto.getChildId());
            TypeParam oldTypeParam = typeParamCopier.copy(typeParam);
            typeParam.setStatus(PersStatus.DELETED);
            logger.logg(TypeActions.REMOVE_SUB_TYPE, oldTypeParam, typeParam, TypeTables.TYPE_PARAM);
            return;
        }
        TypeParam typeParam = typeMapper.mapToTypeParam(dto);
        typeParam.setStatus(PersStatus.DELETED);
        typeParam = typeParamRepo.save(typeParam);
        logger.logg(TypeActions.REMOVE_SUB_TYPE, null, typeParam, TypeTables.TYPE_PARAM);

    }

    @Override
    public boolean parentHasDirectSousType(Long parentId, Long childId)
    {
        return typeParamRepo.parentHasDirectSousType(parentId, childId);
    }

    @Override
    public boolean parentHasDistantSousType(Long parentId, Long childId)
    {
        if(parentHasDirectSousType(parentId, childId)) return true;
        if(!typeRepo.existsById(parentId) || !typeRepo.existsById(childId)) return false;
        return typeRepo.findActiveSousTypes(parentId).stream().anyMatch(st->parentHasDistantSousType(st.getTypeId(), childId));
    }

    @Override
    public List<Type> getPossibleSousTypes(Long parentId)
    {
        return typeRepo.findByTypeGroupAndStatus(typeRepo.findTypeGroupBTypeId(parentId), PersStatus.ACTIVE).stream().filter(t->!this.parentHasDistantSousType(t.getTypeId(), parentId) && !t.getTypeId().equals(parentId)).collect(Collectors.toList());
    }

    @Override
    public Type setSousTypesRecursively(Long typeId)
    {
        Type type = typeRepo.findById(typeId).orElse(null);
        if(type == null) return null;
        List<Type> sousTypes = typeRepo.findActiveSousTypes(typeId);
        type.setChildren(sousTypes);
        sousTypes.forEach(t->setSousTypesRecursively(t.getTypeId()));
        return type;
    }

    @Override
    public List<Type> getSousTypesRecursively(Long typeId)
    {
        Type type = typeRepo.findById(typeId).orElse(null);
        if(type == null) return null;
        return typeRepo.findActiveSousTypes(typeId).stream().flatMap(t-> Stream.concat(Stream.of(t), getSousTypesRecursively(t.getTypeId()).stream())).collect(Collectors.toList());
    }

    @Override
    public List<TypeGroup> getTypeGroups() {
        return Arrays.asList(TypeGroup.values());
    }

    @Override
    public List<ReadTypeDTO> getTypesByGroup(TypeGroup typeGroup)
    {
        return typeRepo.findByTypeGroup(typeGroup);
    }

    @Override
    public Page<Type> searchPageOfTypes(String key, String typeGroup, int pageNum, int pageSize)
    {
        if("".equals(key)) return typeRepo.searchPageOfTypes(PersStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
        Set<TypeGroup> typeGroups =  Arrays.asList(TypeGroup.values()).stream().filter(
                tg -> StringUtils.containsIgnoreCase(tg.getGroupCode(), key) ||
                StringUtils.containsIgnoreCase(tg.getGroupName(), key) ||
                StringUtils.containsIgnoreCase(tg.name(), key)).collect(Collectors.toSet());
        if(typeGroups.size() == 0) return typeRepo.searchPageOfTypes(key, PersStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
        return typeRepo.searchPageOfTypes(key, typeGroups, PersStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<Type> searchPageOfDeletedTypes(String key, String typeGroup, int pageNum, int pageSize)
    {
        if("".equals(key)) return typeRepo.searchPageOfTypes(PersStatus.DELETED, PageRequest.of(pageNum, pageSize));
        Set<TypeGroup> typeGroups =  Arrays.asList(TypeGroup.values()).stream().filter(tg ->StringUtils.containsIgnoreCase(tg.getGroupCode(), key)
                || StringUtils.containsIgnoreCase(tg.getGroupName(), key) || StringUtils.containsIgnoreCase(tg.name(), key)).collect(Collectors.toSet());
        if(typeGroups.size() == 0) return typeRepo.searchPageOfTypes(key, PersStatus.DELETED, PageRequest.of(pageNum, pageSize));
        return typeRepo.searchPageOfTypes(key, typeGroups, PersStatus.DELETED, PageRequest.of(pageNum, pageSize));
    }
}
