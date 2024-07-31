package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.dto.cedante.ReadCedanteDTO;
import com.pixel.synchronre.sychronremodule.model.dto.traite.response.TraiteNPResp;
import com.pixel.synchronre.sychronremodule.model.entities.Categorie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Mapper(componentModel = "spring")
public abstract class CategorieMapper
{
    @Autowired protected IJwtService jwtService;
    @Autowired protected PaysRepository paysRepo;//categorie, dto.getTraiteNPId(), dto.getCedIds()
    
    public abstract CategorieResp mapToCategorieResp(Categorie categorie, TraiteNPResp traite, List<ReadCedanteDTO> cedantes);

//    @Mapping(target = "categorie", expression = "java(categorieId == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Categorie(categorieId))")
//    @Mapping(target = "traiteNonProportionnel", expression = "java(traiteNpId == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(traiteNpId))")
//    @Mapping(target = "cedante", expression = "java(cedId == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.Cedante(cedId))")
//    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
//    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
//    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
//    public abstract CategorieCedante mapToCategorieCedante(Long categorieId, Long traiteNpId, Long cedId);

    @Mapping(target = "traiteNonProportionnel", expression = "java(dto.getTraiteNpId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(dto.getTraiteNpId()))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract Categorie mapToCategorie(CategorieReq dto);
}