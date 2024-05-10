package com.pixel.synchronre.sychronremodule.model.dto.mapper;


import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sharedmodule.exceptions.AppException;
import com.pixel.synchronre.sychronremodule.model.dao.SousLimiteRepository;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.CreateSousLimiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.request.UpdateSousLimite;
import com.pixel.synchronre.sychronremodule.model.dto.souslimite.response.SousLimiteDetailsResp;
import com.pixel.synchronre.sychronremodule.model.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PersistenceContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public abstract class SousLimiteMapper {
    @Autowired
    protected IJwtService jwtService;

    @Autowired
    protected SousLimiteRepository ssRepo;

    @PersistenceContext private EntityManager entityManager;


    @Mapping(target = "traiteNonProportionnel", expression ="java(dto.getTraiteNonProportionnelId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(dto.getTraiteNonProportionnelId()))")
    @Mapping(target = "risqueCouvert", expression ="java(dto.getRisqueCouvertId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.RisqueCouvert(dto.getRisqueCouvertId()))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract SousLimite mapToSousLimite(CreateSousLimiteReq dto);



    public SousLimite mapToSousLimiteUpdate (UpdateSousLimite dto){
        SousLimite ssLimite = ssRepo.findById(dto.getSousLimiteSouscriptionId()).orElseThrow(()->new AppException("Sous Limite introuvable"));
        entityManager.detach(ssLimite);
        BeanUtils.copyProperties(dto, ssLimite, "sousLimiteSouscriptionId","createdAt", "updatedAt");
        return ssLimite;
    }



    @Mapping(target = "sslimiteRisqueCouvertId", source = "risqueCouvert.risqueId")
    @Mapping(target = "sslimiteRisqueCouvertLibelle", source = "risqueCouvert.description")
    @Mapping(target = "sslimiteTraiteNPId", source = "traiteNonProportionnel.traiId")
    @Mapping(target = "sslimiteTraiLibelle", source = "traiteNonProportionnel.traiLibelle")
    @Mapping(target = "sslimiteTrancheId", source = "tranche.trancheId")
    @Mapping(target = "sslimiteTrancheLibelle", source = "tranche.trancheLibelle")
    @Mapping(target = "sslimiteStaCode", source = "statut.staCode")
    @Mapping(target = "sslimiteStaLibelle", source = "statut.staLibelle")
    @Mapping(target = "sslimiteUserCreatorEmail", expression = "java(jwtService.getJwtInfos().getUserEmail())")
    @Mapping(target = "sslimiteUserCreatorNomPrenom", expression = "java(jwtService.getJwtInfos().getNomPrenom())")
    @Mapping(target = "sslimiteFonCreatorName", expression = "java(jwtService.getJwtInfos().getFncName())")
    public abstract SousLimiteDetailsResp mapToSousLimiteResp (SousLimite SsLm);

}
