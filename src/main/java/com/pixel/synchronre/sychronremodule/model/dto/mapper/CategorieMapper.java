package com.pixel.synchronre.sychronremodule.model.dto.mapper;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dao.OrganisationPaysRepository;
import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieReq;
import com.pixel.synchronre.sychronremodule.model.dto.categorie.CategorieResp;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteReq;
import com.pixel.synchronre.sychronremodule.model.dto.territorialite.TerritorialiteResp;
import com.pixel.synchronre.sychronremodule.model.entities.CategorieTraite;
import com.pixel.synchronre.sychronremodule.model.entities.Territorialite;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class CategorieMapper
{
    @Autowired protected IJwtService jwtService;
    @Autowired protected PaysRepository paysRepo;

    //@Mapping(target = "paysList", expression = "java(paysRepo.getPaysByPaysCodes(dto.getPaysCodes()))")
    public abstract CategorieResp mapToCategorieResp(CategorieReq dto, TraiteNonProportionnel traite);

    @Mapping(target = "traiteNonProportionnel", expression = "java(dto.getTraiteNPId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(dto.getTraiteNPId()))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract CategorieTraite mapToCategorieTraite(CategorieReq dto);
}
