package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.StatutRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.CreateCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.ReadCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedente.UpdateCedenteDTO;
import com.pixel.synchronre.sychronremodule.model.entities.Cedente;
import com.pixel.synchronre.sychronremodule.model.entities.Statut;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PersistenceContext;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public abstract class CedMapper
{
    protected StatutRepository staRepo;
    @Autowired protected CedRepo cedRepo;
    @PersistenceContext private EntityManager entityManager;

    @Mapping(target = "cedStatut", expression = "java(staRepo.findByStaCode(\"ACT\"))")
    abstract Cedente mapToCedente(CreateCedenteDTO dto);

    Cedente mapToCedente(UpdateCedenteDTO dto)
    {
        Cedente cedente = cedRepo.findById(dto.getCedId()).orElseThrow(()->new AppException("Cedente introuvable"));
        entityManager.detach(cedente);
        BeanUtils.copyProperties(dto, cedente, "cedId", "createdAt", "updatedAt");
        return cedente;
    }

    @Mapping(target = "cedStatut", source = "cedStatut.name()")
    abstract ReadCedenteDTO mapToReadCedenteDTO(Cedente ced);
}
