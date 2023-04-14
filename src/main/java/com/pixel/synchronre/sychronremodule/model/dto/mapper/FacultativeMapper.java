package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.authmodule.model.entities.AppFunction;
import com.pixel.synchronre.authmodule.model.entities.AppUser;
import com.pixel.synchronre.sharedmodule.utilities.ObjectCopier;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.request.CreateFacultativeReq;
import com.pixel.synchronre.sychronremodule.model.dto.facultative.response.FacultativeDetailsResp;
import com.pixel.synchronre.sychronremodule.model.dto.statut.request.CreateStatutReq;
import com.pixel.synchronre.sychronremodule.model.dto.statut.response.StatutDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import com.pixel.synchronre.sychronremodule.service.interfac.IserviceAffaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.cert.X509Certificate;

@Mapper(componentModel = "spring")
public abstract class FacultativeMapper
{
    @Autowired protected IserviceAffaire affService;
    @Autowired protected IJwtService jwtService;

    public Facultative mapToFacultative(CreateFacultativeReq dto)
    {
        Long connectedUserId = jwtService.getConnectedUserId();
        Long connectedFncId = jwtService.getConnectedUserFunctionId();
        Affaire affaire = new Affaire();
        BeanUtils.copyProperties(dto, affaire);
        affaire.setCedante(dto.getCedenteId() == null ? null : new Cedante(dto.getCedenteId()));
        affaire.setCouverture(dto.getCouvertureId() == null ? null : new Couverture(dto.getCouvertureId()));
        affaire.setAffUserCreator(connectedUserId == null ? null : new AppUser(connectedUserId));
        affaire.setAffFonCreator(connectedFncId == null ? null : new AppFunction(connectedFncId));
        affaire.setAffVisibility(jwtService.getConnectedUserCedId());
        return new Facultative(affaire, dto.getFacNumeroPolice(), dto.getFacSmpLci(), dto.getFacPrime());
    }

    @Mapping(target = "cedenteId", expression = "java(fac.getCedante() == null ? null : fac.getCedante().getCedId())")
    @Mapping(target = "statutCode", expression = "java(fac.getStatut() == null ? null : fac.getStatut().getStaCode())")
    @Mapping(target = "couvertureId", expression = "java(fac.getCouverture() == null ? null : fac.getCouverture().getCouId())")
    @Mapping(target = "restARepartir", expression = "java(affService.calculateRestARepartir(fac.getAffId()))")
    @Mapping(target = "capitalDejaReparti", expression = "java(affService.calculateDejaRepartir(fac.getAffId()))")
    public abstract FacultativeDetailsResp mapToFacultativeDetailsResp(Facultative fac);
}
