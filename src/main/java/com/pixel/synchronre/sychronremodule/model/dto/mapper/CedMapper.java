package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.CreateCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.ReadCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.UpdateCedenteDTO;
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
    protected StatutRepository staRepo;
    @Autowired protected CedRepo cedRepo;
    @PersistenceContext private EntityManager entityManager;

    @Mapping(target = "cedStatut", expression = "java(staRepo.findByStaCode(\"ACT\"))")
    public abstract Cedante mapToCedente(CreateCedenteDTO dto);

    public Cedante mapToCedente(UpdateCedenteDTO dto)
    {
        Cedante cedante = cedRepo.findById(dto.getCedId()).orElseThrow(()->new AppException("Cedente introuvable"));
        entityManager.detach(cedante);
        BeanUtils.copyProperties(dto, cedante, "cedId", "createdAt", "updatedAt");
        return cedante;
    }

    @Mapping(target = "cedStatut", expression = "java(ced.getCedStatut().getStaType().name())")
    public abstract ReadCedenteDTO mapToReadCedenteDTO(Cedante ced);
}
