package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.CreateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.UpdateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CedMapper
{
    @Autowired protected CedRepo cedRepo;
    @Autowired protected IJwtService jwtService;
    @PersistenceContext private EntityManager entityManager;

    @Mapping(target = "cedStatut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "pays",  expression = "java(dto.getPaysCode()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Pays(dto.getPaysCode()))")
    @Mapping(target = "cedUserCreator",  expression = "java(jwtService.getConnectedUserId()==null? null : new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "cedFonCreator",  expression = "java(jwtService.getConnectedUserFunctionId()==null? null : new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    @Mapping(target = "cessionnaire", expression = "java(dto.getCedCesId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire(dto.getCedCesId()))")
    public abstract Cedante mapToCedente(CreateCedanteDTO dto);

    public Cedante mapToCedente(UpdateCedanteDTO dto)
    {
        Cedante cedante = cedRepo.findById(dto.getCedId()).orElseThrow(()->new AppException("Cedente introuvable"));
        entityManager.detach(cedante);
        BeanUtils.copyProperties(dto, cedante, "cedId", "createdAt", "updatedAt");
        return cedante;
    }

    @Mapping(target = "cesId", source = "cessionnaire.cesId")
    @Mapping(target = "cesNom", source = "cessionnaire.cesNom")
    @Mapping(target = "cesSigle", source = "cessionnaire.cesSigle")
    @Mapping(target = "cedStatut", expression = "java(ced.getCedStatut().getStaCode())")
    public abstract ReadCedanteDTO mapToReadCedenteDTO(Cedante ced);
}
