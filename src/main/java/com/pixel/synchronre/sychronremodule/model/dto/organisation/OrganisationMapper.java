package com.pixel.synchronre.sychronremodule.model.dto.organisation;

import com.pixel.synchronre.authmodule.controller.services.spec.IJwtService;
import com.pixel.synchronre.sychronremodule.model.dao.OrganisationPaysRepository;
import com.pixel.synchronre.sychronremodule.model.entities.Organisation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class OrganisationMapper
{
    @Autowired protected IJwtService jwtService;
    @Autowired protected OrganisationPaysRepository orgPaysRepo;
    @Mapping(target = "statut", expression ="java(new com.pixel.synchronre.sychronremodule.model.entities.Statut(\"ACT\"))")
    @Mapping(target = "userCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppUser(jwtService.getConnectedUserId()))")
    @Mapping(target = "fonCreator", expression = "java(new com.pixel.synchronre.authmodule.model.entities.AppFunction(jwtService.getConnectedUserFunctionId()))")
    public abstract Organisation mapToOrgnaisation(OrganisationDTO dto);
    @Mapping(target = "staCode", source = "statut.staCode")
    @Mapping(target = "staLibelle", source = "statut.staLibelle")
    @Mapping(target = "paysList", expression = "java(orgPaysRepo.getPaysByOrgCodes(java.util.Collections.singletonList(dto.getOrganisationCode())) )")
    public abstract OrganisationDTO mapToOrgnaisationDTO(Organisation dto);
}
