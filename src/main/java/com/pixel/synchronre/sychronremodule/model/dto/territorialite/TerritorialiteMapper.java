package com.pixel.synchronre.sychronremodule.model.dto.territorialite;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dao.OrganisationPaysRepository;
import com.pixel.synchronre.sychronremodule.model.dao.PaysRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Territorialite;
import com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class TerritorialiteMapper
{
    @Autowired protected IJwtService jwtService;
    @Autowired protected PaysRepository paysRepo;
    @Autowired protected OrganisationPaysRepository orgPaysRepo;

    @Mapping(target = "paysList", expression = "java(paysRepo.getPaysByPaysCodes(dto.getPaysCodes()))")
    @Mapping(target = "organisationList", expression = "java(dto.getOrgCodes() == null ? \"\" : dto.getOrgCodes().stream().collect(java.util.stream.Collectors.joining(\", \")))")
    public abstract TerritorialiteResp mapToTerritorialiteResp(TerritorialiteReq dto, TraiteNonProportionnel traite);

    @Mapping(target = "traiteNonProportionnel", expression = "java(dto.getTraiteNPId() == null ? null : new com.pixel.synchronre.sychronremodule.model.entities.TraiteNonProportionnel(dto.getTraiteNPId()))")
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract Territorialite mapToTerritorialite(TerritorialiteReq dto);
}
