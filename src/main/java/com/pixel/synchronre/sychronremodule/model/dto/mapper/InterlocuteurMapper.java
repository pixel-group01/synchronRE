package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.CedRepo;
import com.pixel.synchronre.sychronremodule.model.dao.InterlocuteurRepository;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.CreateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.UpdateCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.CreateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.request.UpdateInterlocuteurReq;
import com.pixel.synchronre.sychronremodule.model.dto.interlocuteur.response.InterlocuteurListResp;
import com.pixel.synchronre.sychronremodule.model.entities.Cedante;
import com.pixel.synchronre.sychronremodule.model.entities.Interlocuteur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class InterlocuteurMapper
{
    @Autowired protected InterlocuteurRepository intRepo;
    @Autowired protected IJwtService jwtService;
    @PersistenceContext private EntityManager entityManager;

    @Mapping(target = "statut", expression = "java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "intUserCreator",  expression = "java(jwtService.getConnectedUserId()==null? null : new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "intFonCreator",  expression = "java(jwtService.getConnectedUserFunctionId()==null? null : new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    @Mapping(target = "cessionnaire",  expression = "java(dto.getIntCesId()==null? null : new com.pixel.synchronre.sychronremodule.model.entities.Cessionnaire(dto.getIntCesId()))")
    public abstract Interlocuteur mapToInterlocuteur(CreateInterlocuteurReq dto);

    public Interlocuteur mapToInterlocuteur(UpdateInterlocuteurReq dto)
    {
        Interlocuteur interlocuteur = intRepo.findById(dto.getIntId()).orElseThrow(()->new AppException("Interlocuteur introuvable"));
        entityManager.detach(interlocuteur);
        BeanUtils.copyProperties(dto, interlocuteur, "intId", "createdAt", "updatedAt");
        return interlocuteur;
    }

    @Mapping(target = "statut", expression = "java(inter.getStatut().getStaCode())")
    @Mapping(target = "intCesId", expression = "java(inter.getCessionnaire().getCesId())")
    @Mapping(target = "cesNom", expression = "java(inter.getCessionnaire().getCesNom())")
    @Mapping(target = "cesSigle", expression = "java(inter.getCessionnaire().getCesSigle())")
    public abstract InterlocuteurListResp mapToInterlocuteurListResp(Interlocuteur inter);

}
